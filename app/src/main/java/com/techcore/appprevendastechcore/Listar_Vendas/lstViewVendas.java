package com.techcore.appprevendastechcore.Listar_Vendas;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.techcore.appprevendastechcore.Configurar.StsSys;
import com.techcore.appprevendastechcore.Interfaces.Ler_DadosRemotos;
import com.techcore.appprevendastechcore.Modelos.ListarPreVendas;
import com.techcore.appprevendastechcore.R;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.techcore.appprevendastechcore.NetWork.ConexaoHTTP.getRetrofitHTTP;

public class lstViewVendas extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ProgressDialog dialogo;
    private ListView listView;
    private ADListarVendas adapterListView = null;
    private List<ListarPreVendas> arraylist = new ArrayList<>();
    private String Url;

    // Conexão com o servidor rest
    Retrofit retrofit = getRetrofitHTTP();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.lst_view_vendas);

        // Pega a referência do listview para inserir o click
        listView = findViewById(R.id.lstViewVendas);
        listView.setOnItemClickListener(this);

        Intent itent = getIntent();
        Bundle prms  = itent.getExtras();

        if (prms != null){

            /*
                Exibe o vendedor no título da tela
             */
            TextView txtTitulo = findViewById(R.id.txtLstVendedorApelido);
            txtTitulo.setText(prms.getString("vnd_apelido"));

            dialogo = ProgressDialog.show(lstViewVendas.this, "Aguarde",
                    "Carregando lista de vendas...");

            Url = StsSys.getgUrlServer() + "/mobile/ListarPreVendas?id_emp=" + StsSys.getgCodigoFilial() +"&id_vend="+ prms.getString("vnd_id") +"&aData="+ prms.getString("dta_inicio") +"&bData=" + prms.getString("dta_final");
            Ler_DadosRemotos retorno = retrofit.create(Ler_DadosRemotos.class);

            final Call<List<ListarPreVendas>> ViewVendas = retorno.getPreVendas(Url);
            ViewVendas.enqueue(new Callback<List<ListarPreVendas>>() {
                @Override
                public void onResponse(@NonNull Call<List<ListarPreVendas>> call, @NonNull Response<List<ListarPreVendas>> response) {

                    if (response.isSuccessful()) {

                        List<ListarPreVendas> VerVendas = response.body();

                        if (VerVendas.get(0).getLocalizado().equals("SIM")) {

                            for (ListarPreVendas w : VerVendas) {
                                ListarPreVendas objVendas = new ListarPreVendas(
                                        w.getDATA_VENDA(),
                                        w.getPRE_CODIGO_VENDAS(),
                                        w.getPRE_VLR_SUBTOTAL(),
                                        w.getPRE_VLR_DESCONTO(),
                                        w.getPRE_VLR_TOTAL(),
                                        w.getPRE_STATUS(),
                                        w.getLocalizado(),
                                        w.getCupValor(),
                                        w.getCupStatus()
                                );

                                arraylist.add(objVendas);
                            }

                        } else {
                            Toast.makeText(lstViewVendas.this, "Não foi encontrado nenhuma venda no período informado.", Toast.LENGTH_LONG).show();
                            dialogo.dismiss();
                            finish();
                        }

                        // Pass results to ListViewAdapter Class
                        adapterListView = new ADListarVendas(lstViewVendas.this, arraylist);

                        // Binds the Adapter to the ListView
                        listView.setAdapter(adapterListView);

                    } else {
                        Toast.makeText(lstViewVendas.this, "Erro: " + response.code(), Toast.LENGTH_LONG).show();
                    }

                    // finaliza o progrees
                    dialogo.dismiss();
                }

                @Override
                public void onFailure(Call<List<ListarPreVendas>> call, Throwable t) {

                    if(ViewVendas.isCanceled()){
                        Log.i("Debug", "Listagem cancelada");
                    }else{
                        Log.i("Debug", "Houve um erro no acesso");
                    }

                    // finaliza o progrees
                    dialogo.dismiss();
                }

            });
        }else{
            Toast.makeText(lstViewVendas.this, "Erro ao criar a lista de parâmetros recebidos.", Toast.LENGTH_LONG).show();
        }

        Button btnLstVoltar = findViewById(R.id.btnLstVendasVoltar);
        btnLstVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
