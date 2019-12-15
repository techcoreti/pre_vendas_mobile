package com.techcore.appprevendastechcore.Configurar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.techcore.appprevendastechcore.Databases.CarrinhoBD;
import com.techcore.appprevendastechcore.Interfaces.Ler_DadosRemotos;
import com.techcore.appprevendastechcore.MainActivity;
import com.techcore.appprevendastechcore.Modelos.LoginConfig;
import com.techcore.appprevendastechcore.R;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.techcore.appprevendastechcore.NetWork.ConexaoHTTP.getRetrofitHTTP;

public class Config extends Activity {
    private ProgressDialog  dialogo;

    private CarrinhoBD DbHelper = CarrinhoBD.getInstance(this);
    private SQLiteDatabase dbI  = DbHelper.getWritableDatabase();

    TextView CodigoFilial;
    TextView UrlServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurar);

        CodigoFilial = findViewById(R.id.txtCodigo);
        UrlServer = findViewById(R.id.txtUrlServer);

        Button button = findViewById(R.id.btnEnviar);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                if (CodigoFilial.getText().toString().isEmpty()) {
                    new AlertDialog.Builder(Config.this)
                        .setTitle("Atenção")
                        .setMessage("Informe o Código da Empresa.")
                        .setNeutralButton("OK", null)
                        .show();
                    CodigoFilial.requestFocus();

                } else {

                    if (UrlServer.getText().toString().isEmpty()) {
                        new AlertDialog.Builder(Config.this)
                            .setTitle("Atenção")
                            .setMessage("Informe o endereço do servidor")
                            .setNeutralButton("OK", null)
                            .show();
                        UrlServer.requestFocus();

                    }else{
                        getConfiguracoes(UrlServer.getText().toString(), CodigoFilial.getText().toString());
                    }
                }
            }
        });

        Button btnSair = findViewById(R.id.btnMainSair);
        btnSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

                if (!StsSys.FecharSistema){
                    Intent intent = new Intent(Config.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    public void getConfiguracoes(final String urlServer, final String codigoFilial) {

        // Antes de conectar informa o endereço digitado
        StsSys.gUrlServer = urlServer;

        // Conexão com o servidor rest
        Retrofit retrofit = getRetrofitHTTP();

        /*
          Efetua o resgate das configurações
         */
        String url = urlServer + "/mobile/dados_empresa?emp_codigo=" + codigoFilial;

        Ler_DadosRemotos Config = retrofit.create(Ler_DadosRemotos.class);
        Call<List<LoginConfig>> call = Config.getConfig(url);
        call.enqueue(new Callback<List<LoginConfig>>() {
            @Override
            public void onResponse(Call<List<LoginConfig>> call, Response<List<LoginConfig>> response) {
                if (response.isSuccessful()) {

                    if (response.code() == 200) {
                        List<LoginConfig> retorno = response.body();

                        if (retorno != null) {
                            for (LoginConfig w : retorno) {
                                switch (w.getRetorno()) {
                                    case 100:
                                        new AlertDialog.Builder(Config.this)
                                                .setTitle("Atenção")
                                                .setMessage("Os dados informados estão incorretos")
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Log.i("Config_Init","Erro de preenchimento.");
                                                    }
                                                })
                                                .show();
                                        break;
                                    case 200:
                                        // Grava os dados no banco de dados local
                                        dialogo = ProgressDialog.show(Config.this, "Aguarde",
                                                "Cadastrando o terminal...");

                                        // Zera as configurações do servidor
                                        CarrinhoBD DbHelper = CarrinhoBD.getInstance(Config.this);
                                        SQLiteDatabase db = DbHelper.getWritableDatabase();
                                        db.delete("tbConfig", null, null);

                                        ContentValues val = new ContentValues();
                                        val.put("CodigoFilial", codigoFilial);
                                        val.put("UrlServer", urlServer);
                                        dbI.insert("tbConfig", null, val);

                                        // Armazena os dados na classe StsSys
                                        StsSys.gCodigoFilial = codigoFilial;
                                        StsSys.gUrlServer = urlServer;

                                        dialogo.dismiss();

                                        new AlertDialog.Builder(Config.this)
                                                .setTitle("TechCore-PDV")
                                                .setMessage("Terminal cadastrado com sucesso.")
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        finish();
                                                        ReloadMain();
                                                    }
                                                })
                                                .show();

                                }
                            }
                        }

                    }else{
                        Toast.makeText(
                                Config.this,
                                "Erro ao negociar com o servidor: " + response.code(),
                                Toast.LENGTH_LONG
                        ).show();
                    }

                } else {
                    Toast.makeText(
                            Config.this,
                            "Erro ao conectar no servidor: " + response.code(),
                            Toast.LENGTH_LONG
                    ).show();
                }
            }

            @Override
            public void onFailure(Call<List<LoginConfig>> call, Throwable t) {
                Toast.makeText(Config.this, "Erro geral: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }

            private void ReloadMain(){
                Intent intent = new Intent(Config.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}

