package com.techcore.appprevendastechcore.Vendedores;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import com.techcore.appprevendastechcore.Configurar.StsSys;
import com.techcore.appprevendastechcore.Interfaces.Ler_DadosRemotos;
import com.techcore.appprevendastechcore.Listar_Vendas.lstViewVendas;
import com.techcore.appprevendastechcore.Modelos.LoginVendedor;
import com.techcore.appprevendastechcore.R;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.techcore.appprevendastechcore.NetWork.ConexaoHTTP.getRetrofitHTTP;

public class LoginDataVendas extends AppCompatActivity implements View.OnClickListener {

    /*
        Variaveis globais
     */
    private int mYear, mMonth, mDay;
    private EditText txtDataA, txtDataB, txtUser, txtSenha;
    private Button btnDataA, btnDataB, btnSearch;
    private String Url;

    // Conexão com o servidor rest
    Retrofit retrofit = getRetrofitHTTP();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_data_vendas);

        /*
            Campos das datas
         */
        txtDataA = findViewById(R.id.txtDataA);
        txtDataB = findViewById(R.id.txtDataB);
        txtUser  = findViewById(R.id.txtUser);
        txtSenha = findViewById(R.id.txtSenha);

        /*
            Botão para inserir a data inicial
         */
        btnDataA = findViewById(R.id.btnDataA);
        btnDataB = findViewById(R.id.btnDataB);
        btnSearch = findViewById(R.id.btnSearch);

        /*
            Clique dos botões
         */
        btnDataA.setOnClickListener(this);
        btnDataB.setOnClickListener(this);
        btnSearch.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        if (view == btnDataA){

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                                String res = String.format(Locale.getDefault(),"%02d", dayOfMonth) + "/" +
                                        String.format(Locale.getDefault(),"%02d", (monthOfYear + 1)) + "/" +
                                        String.format(Locale.getDefault(),"%04d", year);

                                txtDataA.setText(res);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }

        if (view == btnDataB){

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            String res = String.format(Locale.getDefault(),"%02d", dayOfMonth) + "/" +
                                         String.format(Locale.getDefault(),"%02d", (monthOfYear + 1)) + "/" +
                                         String.format(Locale.getDefault(),"%04d", year);

                            txtDataB.setText(res);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }

        if (view == btnSearch){

            if (txtUser.getText().toString().equals("")) {
                txtUser.requestFocus();
                txtUser.setError("Informe o usuário.");
                return;
            }

            if (txtSenha.getText().toString().equals("")){
                txtSenha.requestFocus();
                txtSenha.setError("Informe a sua senha.");
                return;
            }

            if (txtDataA.getText().toString().equals("")){
                txtDataA.requestFocus();
                txtDataA.setError("Informe a data incial.");
                return;
            }

            if (txtDataB.getText().toString().equals("")){
                txtDataB.requestFocus();
                txtDataB.setError("Informe a data final.");
                return;
            }

            // Checa o usuário e senha informados
            ChecarLogin(txtUser.getText().toString(), txtSenha.getText().toString());        }
    }

    public void ChecarLogin(String sUser, String sSenha){

        /*
            Efetua o login do vendedor
         */
        Url = StsSys.getgUrlServer() + "/mobile/LoginVendedor?id_emp=" + StsSys.getgCodigoFilial() + "&vnd_user=" + sUser + "&vnd_senha=" + sSenha;
        Ler_DadosRemotos retorno = retrofit.create(Ler_DadosRemotos.class);

        final Call<List<LoginVendedor>> ViewVendas = retorno.getLoginVendedor(Url);
        ViewVendas.enqueue(new Callback<List<LoginVendedor>>() {
            @Override
            public void onResponse(@NonNull Call<List<LoginVendedor>> call, @NonNull Response<List<LoginVendedor>> response) {

                if (response.isSuccessful()) {

                    List<LoginVendedor> VerVendas = response.body();

                    if (VerVendas != null) {

                        for (LoginVendedor w : VerVendas) {
                            if (w.getResposta().equals("ErroLogin")){
                                Toast.makeText(LoginDataVendas.this, "Usuário e ou senha incorretos, tente novamente", Toast.LENGTH_LONG).show();
                            }else{
                                Intent itent = new Intent(LoginDataVendas.this, lstViewVendas.class);
                                itent.putExtra("vnd_id", String.valueOf(w.getID()));
                                itent.putExtra("vnd_apelido", w.getVND_APELIDO());
                                itent.putExtra("dta_inicio", txtDataA.getText().toString());
                                itent.putExtra("dta_final", txtDataB.getText().toString());
                                startActivity(itent);
                            }
                        }

                    } else {
                        Toast.makeText(LoginDataVendas.this, "Erro: não foi retornado nenhum resultado", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(LoginDataVendas.this, "Erro: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<LoginVendedor>> call, Throwable t) {

                if(ViewVendas.isCanceled()){
                    Log.i("Debug", "Listagem cancelada");
                }else{
                    Log.i("Debug", "Houve um erro no acesso");
                }

            }

        });

    }

}
