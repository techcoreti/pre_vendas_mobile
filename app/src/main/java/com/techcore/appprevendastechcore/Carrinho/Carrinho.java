package com.techcore.appprevendastechcore.Carrinho;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.techcore.appprevendastechcore.Configurar.StsSys;
import com.techcore.appprevendastechcore.Databases.CarrinhoBD;
import com.techcore.appprevendastechcore.Modelos.Cupons;
import com.techcore.appprevendastechcore.Modelos.ProdutosCart;
import com.techcore.appprevendastechcore.R;
import com.techcore.appprevendastechcore.Produtos.AListarProduto;

import java.lang.reflect.Array;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Carrinho extends AppCompatActivity {
    private List<ProdutosCart> arraylist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrinho);

        // Carrega todos os produtos já inseridos
        loadProdutos();

        // Carrega os dados do pedido
        loadPedido();

        Button btnDesc = findViewById(R.id.btnDesconto);
        btnDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                  Checa se o carrinos esta vazio e refaz o calculo do valor
                  total dos produtos
                 */
                IniciarDesconto();
            }
        });

        Button btnFinish = findViewById(R.id.btnCarFinalizar);
        btnFinish.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CarrinhoBD DbHelper = CarrinhoBD.getInstance(Carrinho.this);
                SQLiteDatabase db   = DbHelper.getReadableDatabase();

                /*
                  Antes de continuar, checa se existem produtos no carrinho
                  Caso não impede de continuar com o fechamento
                 */
                String[] cols = new String[]{"ID"};
                Cursor cur    = db.query("tbProdutos", cols, null, null, null, null, null);

                if (cur.moveToFirst()) {

                    Intent intent = new Intent(Carrinho.this, Car_Fechamento.class);
                    startActivity(intent);

                } else {
                    new AlertDialog.Builder(Carrinho.this)
                            .setTitle("Atenção")
                            .setMessage("Não existem produtos no orçamento")
                            .setNegativeButton("OK", null)
                            .show();
                }
                cur.close();
            }
        });

        Button btnCancel = findViewById(R.id.btnCancelar);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Carrinho.this)
                        .setTitle("Atenção")
                        .setMessage("Tem certeza que cancelar esta venda?")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Exclue o pedido
                                ExcluirPedidos(Carrinho.this);

                                // Exclue os produtos
                                ExcluirProdutos(Carrinho.this);

                                // Remove o vendedor
                                StsSys.gIDVendedor = 0;

                                // Fecha a view
                                finish();
                            }
                        })
                        .setNegativeButton("Não", null)
                        .show();
            }
        });

        Button btnCarProdutos = findViewById(R.id.btnCarProdutos);
        btnCarProdutos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StsSys.RefreshCarrinho = true;
                finish();
                Intent intent = new Intent(Carrinho.this, AListarProduto.class);
                startActivity(intent);
            }
        });
    }

    public void ExcluirPedidos(Context context){
        CarrinhoBD DbHelper = CarrinhoBD.getInstance(context);
        SQLiteDatabase db  = DbHelper.getWritableDatabase();
        db.delete("tbPedidos", null, null);
        db.close();
    }

    public void ExcluirProdutos(Context context){
        CarrinhoBD DbHelper = CarrinhoBD.getInstance(context);
        SQLiteDatabase db  = DbHelper.getWritableDatabase();
        db.delete("tbProdutos", null, null);
        db.close();
    }

    public void loadProdutos(){
        CarrinhoBD DbHelper = CarrinhoBD.getInstance(this);
        SQLiteDatabase dbR  = DbHelper.getReadableDatabase();
        Cursor cur;

        TextView dspQtdeItens = findViewById(R.id.dspQtdeItens);

        /*
          Carrega todos os produtos já inseridos
         */
        String[] colunas = new String[]{
                "ID",
                "ProdutoCodigo",
                "ProdutoNome",
                "ProdutoUnidade",
                "ProdutoQuantidade",
                "ProdutoValorTotal"
        };
        cur = dbR.query("tbProdutos",colunas,null,null,null,null,"ID");

        if (cur.moveToFirst()) {
            do {
                ProdutosCart pr = new ProdutosCart();
                pr.setIdTabela(cur.getString(0));
                pr.setProdutoCodigo(cur.getString(1));
                pr.setProdutoNome(cur.getString(2));
                pr.setProdutoUnidade(cur.getString(3));
                pr.setProdutoQuantidade(cur.getDouble(4));
                pr.setProdutoValorTotal(cur.getDouble(5));

                // Acrescenta o produto no array
                arraylist.add(pr);

            } while (cur.moveToNext());

            // Exibe a quantidade de produtos no carrinho
            dspQtdeItens.setText(String.format(new Locale("pt","BR"), "%03d",cur.getCount()));

            // Pass results to ListViewAdapter Class
            Car_Adpater adapterListView = new Car_Adpater(Carrinho.this, arraylist, this);

            // Binds the Adapter to the ListView
            ListView listView = findViewById(R.id.lsCarrinho);
            listView.setAdapter(adapterListView);
        }
        cur.close();
    }

    public void loadPedido(){
        NumberFormat nm = NumberFormat.getCurrencyInstance(new Locale("pt","BR"));
        nm.setMaximumFractionDigits(2);

        TextView vlrSemDesconto = findViewById(R.id.dspValSemDesc);
        TextView vlrComDesconto = findViewById(R.id.txtTotalPedido);
        TextView vlrDoDesconto  = findViewById(R.id.txtValDesc);

        CarrinhoBD DbHelper = CarrinhoBD.getInstance(this);
        SQLiteDatabase db  = DbHelper.getWritableDatabase();
        Cursor cur;

        /*
          Carrega os dados do pedido
         */
        String[] colunas = new String[]{"ID","ValorSD","Desconto","ValorCD"};
        cur = db.query("tbPedidos",colunas,null,null,null,null,null);

        if (cur.moveToFirst()){
            StsSys.gCodigoPedido = cur.getInt(0);
            vlrSemDesconto.setText(nm.format(cur.getDouble(1)));
            vlrDoDesconto.setText(nm.format(cur.getDouble(2)));
            vlrComDesconto.setText(nm.format(cur.getDouble(3)));
        }
        db.close();
    }

    public void IniciarDesconto(){
        CarrinhoBD DbHelper = CarrinhoBD.getInstance(this);
        SQLiteDatabase db   = DbHelper.getReadableDatabase();
        Cursor cur;

        /*
          Checa se existem produtos no carrinho
         */
        String[] colunas = new String[]{"ID"};
        cur = db.query("tbProdutos",colunas,null,null,null,null,null);

        if (cur.moveToFirst()) {

            String[] cols = new String[]{"Sum(ProdutoValorTotal) as Total"};
            cur = db.query("tbProdutos",cols,null,null,null,null,null);
            cur.moveToFirst();

            StsSys.vlrSemDesconto = cur.getDouble(0);
            StsSys.mActivity = Carrinho.this;

            Intent itent = new Intent(Carrinho.this, Car_Desconto.class);
            startActivity(itent);

        }else {
            new AlertDialog.Builder(Carrinho.this)
                    .setTitle("Atenção")
                    .setMessage("Não existem produtos no orçamento")
                    .setNegativeButton("OK", null)
                    .show();
        }

        cur.close();
    }

    @Override
    public void onBackPressed() {
        StsSys.gIDVendedor = 0;
        finish();
    }
}
