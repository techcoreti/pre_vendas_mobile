package com.techcore.appprevendastechcore.Carrinho;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.techcore.appprevendastechcore.Configurar.StsSys;
import com.techcore.appprevendastechcore.Databases.CarrinhoBD;
import com.techcore.appprevendastechcore.Interfaces.Gravar_DadosServidor;
import com.techcore.appprevendastechcore.MainActivity;
import com.techcore.appprevendastechcore.Modelos.PostPedido;
import com.techcore.appprevendastechcore.R;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.techcore.appprevendastechcore.NetWork.ConexaoHTTP.getRetrofitHTTP;


public class Car_Fechamento extends AppCompatActivity {
    String CodigoVenda;

    DecimalFormat dcfA = new DecimalFormat("#,##0.00");
    DecimalFormatSymbols custom = new DecimalFormatSymbols();

    // Tratamento dos botões da activity
    Button btnCancelar;

    // Conexão com o servidor
    Retrofit retrofit = getRetrofitHTTP();

    // Banco de dados local
    CarrinhoBD DbHelper = CarrinhoBD.getInstance(this);
    SQLiteDatabase db = DbHelper.getReadableDatabase();
    Cursor cur;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fechamento);

        // Desabilita o layout do código da venda
        ConstraintLayout layCodigo = findViewById(R.id.layCodigo);
        layCodigo.setVisibility(View.INVISIBLE);

        custom.setDecimalSeparator(',');
        custom.setGroupingSeparator('.');
        dcfA.setDecimalFormatSymbols(custom);

        // Recupera os dados do pedido
        String[] cols = new String[]{
                "ValorSD",
                "Desconto",
                "ValorCD",
                "Vendedor",
                "CodigoDesconto",
        };

        cur = db.query("tbPedidos", cols, null, null, null, null, null);
        cur.moveToFirst();

        StsSys.ValorSD = cur.getFloat(0);
        StsSys.ValorDS = cur.getFloat(1);
        StsSys.ValorCD = cur.getFloat(2);
        StsSys.gCodigoVendedor = cur.getString(3);
        StsSys.gCodigoDesconto = cur.getString(4);
        cur.close();

        TextView txtVlrSemDesconto = findViewById(R.id.txtVlrSemDesconto);
        TextView txtVlrDoDesconto  = findViewById(R.id.tztVlrDoDesconto);
        TextView txtVlrComDesconto = findViewById(R.id.txtVlrComDesconto);
        TextView txtVendedor = findViewById(R.id.textView11);
        txtVendedor.setText(StsSys.gApelidoVendedor);

        txtVlrSemDesconto.setText(dcfA.format(StsSys.ValorSD));
        txtVlrDoDesconto.setText(dcfA.format(StsSys.ValorDS));
        txtVlrComDesconto.setText(dcfA.format(StsSys.ValorCD));

        Button button = findViewById(R.id.btnFinish);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(Car_Fechamento.this)
                        .setTitle("Atenção")
                        .setMessage("Tem certeza que deseja confirmar esta venda?")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Gera o código da venda
                                Random gerador = new Random();

                                CodigoVenda = Integer.toString(gerador.nextInt(999)) + gerador.nextInt(999);
                                CodigoVenda += StsSys.gIDVendedor;

                                TextView txtCodigo = findViewById(R.id.txtCodigo);
                                txtCodigo.setText(CodigoVenda);

                                // Tenta enviar o pedido para o servidor
                                GravarPedido();
                            }
                        })
                        .setNegativeButton("Não", null)
                        .show();
            }
        });

        btnCancelar = findViewById(R.id.btnBack);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    // Gravar a venda no servidor
    public void GravarPedido() {

        Gravar_DadosServidor myConnect = retrofit.create(Gravar_DadosServidor.class);
        Call<List<PostPedido>> call = myConnect.GravarVenda(
                CodigoVenda,
                StsSys.gCodigoFilial,
                StsSys.gIDVendedor,
                StsSys.ValorSD,
                StsSys.ValorDS,
                StsSys.ValorCD);

        call.enqueue(new Callback<List<PostPedido>>() {
            @Override
            public void onResponse(Call<List<PostPedido>> call, Response<List<PostPedido>> response) {
                if (response.isSuccessful()) {
                    List<PostPedido> retorno = response.body();

                    if (retorno != null) {
                        for (PostPedido Status : retorno) {
                            if (Status.getStatus().equals("OK")) {
                                Log.i("Status", "OK");
                                GravarProdutosVenda();
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<PostPedido>> call, Throwable t) {
                Log.i("Status", t.getMessage());
                Toast.makeText(
                        Car_Fechamento.this,
                        "Erro ao contactar o servidor, tente novamente.",
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }

    // Grava os produtos da venda no servidor
    public void GravarProdutosVenda() {

        String[] cols = new String[]{
                "ProdutoCodigo",
                "ProdutoQuantidade"};

        cur = db.query("tbProdutos", cols, null, null, null, null, null);

        if (cur.moveToFirst()) {
            do {
                Gravar_DadosServidor myConnect = retrofit.create(Gravar_DadosServidor.class);
                Call<List<PostPedido.PostProdutos>> call = myConnect.GravarProdutosVenda(
                        CodigoVenda,
                        cur.getString(0),
                        cur.getFloat(1)
                );

                call.enqueue(new Callback<List<PostPedido.PostProdutos>>() {
                    @Override
                    public void onResponse(Call<List<PostPedido.PostProdutos>> call, Response<List<PostPedido.PostProdutos>> response) {
                        if (response.isSuccessful()) {

                            List<PostPedido.PostProdutos> retorno = response.body();

                            if (retorno != null) {
                                for (PostPedido.PostProdutos Status : retorno) {
                                    if (Status.getStatus().equals("OK")) {
                                        Toast.makeText(
                                                Car_Fechamento.this,
                                                "Venda gerada com sucesso",
                                                Toast.LENGTH_LONG
                                        ).show();

                                        // Finaliza a venda e ajusta alguns dados
                                        FinalizarVenda();

                                    } else {
                                        Toast.makeText(Car_Fechamento.this, "Erro ao efetuar a venda, tente novamente", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }

                        } else {
                            Toast.makeText(
                                    Car_Fechamento.this,
                                    "Erro ao enviar os dados para o servidor, se o problema persistir contate o suporte técnico",
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<PostPedido.PostProdutos>> call, Throwable t) {
                        Log.i("Status", t.getMessage());
                        Toast.makeText(
                                Car_Fechamento.this,
                                "Erro ao contactar o servidor, tente novamente.",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });

            } while (cur.moveToNext());

        }

        cur.close();
    }

    // Ajusta o activity
    @SuppressLint("SetTextI18n")
    private void FinalizarVenda(){
        // Efetua a baixa do orçamento no banco de dados local
        BaixarOrcamento();

        // Exibe o layout do código da venda
        ConstraintLayout layCodigo = findViewById(R.id.layCodigo);
        layCodigo.setVisibility(View.VISIBLE);

        // Altera o caption do botão voltar
        btnCancelar = findViewById(R.id.btnBack);
        btnCancelar.setText("Fechar");

        // Desabilita o botão confirmar
        ConstraintLayout layConfirmar = findViewById(R.id.layBtnConfirma);
        layConfirmar.setVisibility(View.INVISIBLE);
    }

    // Efetua a baixa da venda no banco de dados local
    public void BaixarOrcamento() {
        ContentValues val = new ContentValues();
        val.put("Status", 2);
        db.update("tbPedidos", val, null, null);
    }

    // Trata o pressionamento do botão voltar do dispositivo
    public void onBackPressed() {
        if (btnCancelar.getText().equals("Fechar")){
            FecharActivitys();
        }else {
            new AlertDialog.Builder(Car_Fechamento.this)
                    .setTitle("Atenção")
                    .setMessage("Tem certeza que deseja sair?")
                    .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).setNegativeButton("Não", null)
                    .show();
        }
    }

    // Fecha todas as Activitys
    public void FecharActivitys() {
        StsSys.gIDVendedor = 0;

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        // Coleta objetos não mais usados
        System.gc();
    }
}
