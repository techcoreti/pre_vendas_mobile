package com.techcore.appprevendastechcore;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.techcore.appprevendastechcore.Carrinho.Carrinho;
import com.techcore.appprevendastechcore.Configurar.Config;
import com.techcore.appprevendastechcore.Configurar.StsSys;
import com.techcore.appprevendastechcore.Databases.CarrinhoBD;
import com.techcore.appprevendastechcore.Vendedores.LoginDataVendas;
import com.techcore.appprevendastechcore.Vendedores.Activity_Vendedores;
import com.techcore.appprevendastechcore.Produtos.AListarProduto;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Carrega os dados do servidor
        LoadConfigServer();

        // Exibe o endereço atual do servidor
        TextView dspServidor = findViewById(R.id.dspServidor);
        dspServidor.setText("Servidor: " + StsSys.gUrlServer);

        Button button = findViewById(R.id.btnMainLstVendas);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent itent = new Intent(MainActivity.this, LoginDataVendas.class);
                startActivity(itent);
            }
        });

        Button btnProdutos = findViewById(R.id.btnMainProdutos);
        btnProdutos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CarrinhoBD DbHelper = CarrinhoBD.getInstance(MainActivity.this);
                SQLiteDatabase db   = DbHelper.getReadableDatabase();

                String[] cols       = new String[]{"ID"};
                Cursor c = db.query("tbPedidos",cols,"Status = ?", new String[]{"1"},null,null,null);

                if (c.getCount() < 1 ) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Atenção")
                            .setMessage("Não existe orçamento em aberto.")
                            .setNegativeButton("OK", null)
                            .show();
                }else{
                    StsSys.RefreshCarrinho = false;
                    Intent itent = new Intent(MainActivity.this, AListarProduto.class);
                    startActivity(itent);
                }
            }
        });

        Button btnCheckout = findViewById(R.id.btnMainCheckout);
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IniciarCheckout();
            }
        });

        Button btnNovo = findViewById(R.id.btnMainNovo);
        btnNovo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Atenção")
                        .setMessage("Deseja iniciar um novo pedido?")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // Exclue todos os pedidos existentes
                                ExcluirPedidos();

                                // Exclue todos os produtos
                                ExcluirProdutos();

                                // Inicia o novo pedido de vendas
                                IniciarPedido();

                                Intent itent = new Intent(MainActivity.this, AListarProduto.class);
                                startActivity(itent);
                            }
                        })
                        .setNegativeButton("Não", null)
                        .show();
            }
        });

        Button btnSair = findViewById(R.id.btnMainSair);
        btnSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Atenção")
                        .setMessage("Tem certeza que deseja sair")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                System.exit(0);
                            }
                        })
                        .setNegativeButton("Não", null)
                        .show();
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (StsSys.gCheckout){
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Atenção")
                            .setMessage("Não é possivel continuar, exitem orçamentos pendentes.")
                            .setNeutralButton("OK", null)
                            .show();
                }else {
                    StsSys.FecharSistema = false;

                    finish();

                    // Carrega a view de configuração
                    Intent itent = new Intent(MainActivity.this, Config.class);
                    startActivity(itent);
                }
            }
        });
    }

    public void LoadConfigServer(){
        CarrinhoBD DbHelper = CarrinhoBD.getInstance(MainActivity.this);
        SQLiteDatabase db   = DbHelper.getReadableDatabase();
        Cursor c = db.query("tbConfig",null,null,null,null,null,null);

        if (c.moveToFirst()){

            // Armazena nas variaveis as configurações
            StsSys.gCodigoFilial = c.getString(1);
            StsSys.gUrlServer    = c.getString(2);
        }else{
            StsSys.FecharSistema = true;

            this.finish();

            Intent itent = new Intent(this, Config.class);
            startActivity(itent);
        }
    }

    public void ExcluirPedidos(){
        CarrinhoBD DbHelper = CarrinhoBD.getInstance(MainActivity.this);
        SQLiteDatabase db  = DbHelper.getWritableDatabase();
        db.delete("tbPedidos", null, null);
        db.close();
    }

    public void ExcluirProdutos(){
        CarrinhoBD DbHelper = CarrinhoBD.getInstance(MainActivity.this);
        SQLiteDatabase db  = DbHelper.getWritableDatabase();
        db.delete("tbProdutos", null, null);
        db.close();
    }

    public void IniciarPedido(){
        CarrinhoBD DbHelper = CarrinhoBD.getInstance(MainActivity.this);
        SQLiteDatabase db   = DbHelper.getReadableDatabase();
        ContentValues vl    = new ContentValues();

        SimpleDateFormat sdff = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String dd = (sdff.format(new Date()));

        vl.put("Data", dd);
        vl.put("Status", 1);
        vl.put("CodigoFilial", StsSys.gCodigoFilial);
        vl.put("CodigoTerminal", StsSys.gCodigoTerminal);
        vl.put("CodigoPrecos", StsSys.gCodigoPrecos);

        db.insert("tbPedidos", null, vl);
    }

    public void IniciarCheckout(){
        CarrinhoBD DbHelper = CarrinhoBD.getInstance(MainActivity.this);
        SQLiteDatabase db   = DbHelper.getReadableDatabase();
        Cursor cur;

        /*
          Verifica se existe pedido em andamento, caso não
          inicia um novo pedido
         */
        String[] colsA = new String[]{"ID"};
        cur  = db.query("tbPedidos", colsA, "Status = ?", new String[]{"1"}, null, null, null);

        if (!cur.moveToFirst()) {
            // Exclue todos os pedidos existentes
            ExcluirPedidos();

            // Exclue todos os produtos
            ExcluirProdutos();

            // Inicia o novo pedido de vendas
            IniciarPedido();

            // Remove o vendedor antes de abrir
            StsSys.RefreshCarrinho = false;

            Intent itent = new Intent(MainActivity.this, AListarProduto.class);
            startActivity(itent);
            cur.close();

        }else {

            /*
              Verifica se já foi informado o codigo do vendedor se não
              armazena na variavel os dados do vendedor
             */
            if (StsSys.gIDVendedor == 0){
                Intent itent = new Intent(MainActivity.this, Activity_Vendedores.class);
                startActivity(itent);
            } else {

                /*
                  Verifica se já existem produtos inseridos no pedido
                  se não houver vai direto para lista de produtos
                 */
                if (GetProdutos() <= 0) {
                    Intent itent = new Intent(MainActivity.this, AListarProduto.class);
                    startActivity(itent);
                } else {
                    Intent itent = new Intent(MainActivity.this, Carrinho.class);
                    startActivity(itent);
                }
            }
            cur.close();
            db.close();

            // Coleta objetos não mais usados
            System.gc();
        }
    }

    public int GetProdutos(){
        CarrinhoBD DbHelper = CarrinhoBD.getInstance(MainActivity.this);
        SQLiteDatabase db   = DbHelper.getReadableDatabase();

        String[] colunas = new String[]{"ProdutoCodigo"};
        Cursor c = db.query("tbProdutos",colunas,null,null,null,null,null);

        return c.getCount();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Atenção")
                .setMessage("Tem certeza que deseja sair?")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);
                    }
                }).setNegativeButton("Não", null)
                .show();
    }

}
