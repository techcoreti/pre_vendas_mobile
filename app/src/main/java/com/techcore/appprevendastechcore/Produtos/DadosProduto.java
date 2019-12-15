package com.techcore.appprevendastechcore.Produtos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.techcore.appprevendastechcore.Databases.CarrinhoBD;
import com.techcore.appprevendastechcore.R;

import java.text.NumberFormat;
import java.util.Locale;

public class DadosProduto extends Activity {
    private String produto, codigo, unidade;
    private double vValor, vTotal ,Quant;

    NumberFormat dcfA = NumberFormat.getCurrencyInstance(new Locale("pt","BR"));

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dados_produto);

        // Altera o formato de exibição dos valore numericos
        TextView edtUnidade = findViewById(R.id.txtUnidade);
        TextView edtCodigo  = findViewById(R.id.txtCodigo);
        TextView edtProduto = findViewById(R.id.txtProduto);
        TextView txtValor   = findViewById(R.id.txtValor);

        Intent it = getIntent();
        if (it != null){
            Bundle params  = it.getExtras();
            if (params != null){

                // Recupera o nome do produto
                produto = params.getString("keyProduto");
                edtProduto.setText(produto);

                // Recupera o código do produto
                codigo = String.format(new Locale("pt","BR"), "%06d", params.getInt("keyCodigo"));
                edtCodigo.setText(codigo);

                // Recupera a unidade comercial do produto
                unidade = params.getString("keyUnidade");
                edtUnidade.setText(unidade);

                // Recupera o valor comercial do produto
                vValor = params.getDouble("keyValor");
                String fValor = dcfA.format(vValor);
                txtValor.setText(fValor);
            }
        }

        Button btnComprar = findViewById(R.id.btnComprar);
        btnComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView edtQuant   = findViewById(R.id.txtQuant);
                if (edtQuant.getText().toString().isEmpty()){
                    new AlertDialog.Builder(DadosProduto.this)
                            .setTitle("Atenção")
                            .setMessage("Informe a quantidade")
                            .setNeutralButton("OK", null)
                            .show();
                    edtQuant.requestFocus();
                }else{
                    CarrinhoBD DbHelper = CarrinhoBD.getInstance(DadosProduto.this);
                    SQLiteDatabase db   = DbHelper.getReadableDatabase();

                    ContentValues val = new ContentValues();
                    val.put("ProdutoCodigo",     codigo);
                    val.put("ProdutoNome",       produto);
                    val.put("ProdutoValor",      vValor);
                    val.put("ProdutoQuantidade", Quant);
                    val.put("ProdutoValorTotal", vTotal);
                    val.put("ProdutoUnidade",    unidade);

                    db.insert("tbProdutos", null, val);
                    finish();

                    AjustaPedido();

                }
            }
        });

        Button btnQtdePrdCancelar = findViewById(R.id.btnQtdePrdCancelar);
        btnQtdePrdCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        TextView edtQuant   = findViewById(R.id.txtQuant);
        edtQuant.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                TextView edtQuant = findViewById(R.id.txtQuant);
                TextView edtTotal = findViewById(R.id.txtValTotal);

                if (edtQuant.getText().toString().isEmpty()){
                    Quant = 0;
                    vTotal = (vValor * Quant);
                    edtTotal.setText(dcfA.format(vTotal));
                }else {
                    Quant  = Double.parseDouble(edtQuant.getText().toString().replace(',','.'));
                    vTotal = (vValor * Quant);
                    edtTotal.setText(dcfA.format(vTotal));
                }
            }
        });
    }

    public void AjustaPedido(){
        CarrinhoBD DbHelper = CarrinhoBD.getInstance(DadosProduto.this);
        SQLiteDatabase dbW  = DbHelper.getWritableDatabase();
        SQLiteDatabase dbR  = DbHelper.getReadableDatabase();

        String[] cols = new String[]{"Sum(ProdutoValorTotal) as Total"};
        Cursor cur    = dbR.query("tbProdutos", cols, null, null, null, null, null);
        cur.moveToFirst();

        ContentValues val = new ContentValues();
        val.put("ValorSD",  cur.getDouble(0));
        val.put("Desconto", 0);
        val.put("ValorCD",  cur.getDouble(0));
        dbW.update("tbPedidos",val,null,null);

        cur.close();

    }

}