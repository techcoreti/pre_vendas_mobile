package com.techcore.appprevendastechcore.Vendedores;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.techcore.appprevendastechcore.Carrinho.Carrinho;
import com.techcore.appprevendastechcore.Configurar.StsSys;
import com.techcore.appprevendastechcore.Interfaces.Ler_DadosRemotos;
import com.techcore.appprevendastechcore.Modelos.Vendedores;
import com.techcore.appprevendastechcore.R;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.techcore.appprevendastechcore.NetWork.ConexaoHTTP.getRetrofitHTTP;

public class Activity_Vendedores extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private ProgressDialog dialogo;
    private ListView listView;
    private Adapter_Vendedores adapterListView = null;
    List<Vendedores> arraylist = new ArrayList<>();
    String Url;

    // Conexão com o servidor rest
    Retrofit retrofit = getRetrofitHTTP();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_vendedores);

        /*
          Carega a lista devendedores disponiveis
         */
        listView = findViewById(R.id.lsVendedores);
        listView.setOnItemClickListener(this);

        ListarVendedores();
    }

    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

        // Pega o item que foi selecionado.
        Vendedores item = adapterListView.getItem(arg2);

        // Armazena os dados do vendedor para uso posterior
        StsSys.gIDVendedor        = item.getID();
        StsSys.gCodigoVendedor    = item.getFUN_CODIGO();
        StsSys.gMargemMaxDesconto = item.getDSC_MARGEM();
        StsSys.gApelidoVendedor   = item.getFUN_APELIDO();

        Intent itent = new Intent(this, Carrinho.class);
        startActivity(itent);
        this.finish();
    }

    public void ListarVendedores(){

        /*
          Efetua o resgate dos vendedores
         */
        dialogo = ProgressDialog.show(Activity_Vendedores.this, "Aguarde",
                "Carregando a lista de Vendedores...");

        // Url completa
        Url = StsSys.gUrlServer + "/mobile/listar_vendedores?emp_codigo=" +  StsSys.gCodigoFilial;

        Ler_DadosRemotos vendedores = retrofit.create(Ler_DadosRemotos.class);
        Call<List<Vendedores>> callvendedor = vendedores.getVendedores(Url);
        callvendedor.enqueue(new Callback<List<Vendedores>>() {
            @Override
            public void onResponse(Call<List<Vendedores>> call, Response<List<Vendedores>> response) {
                if (response.isSuccessful()) {

                    if (response.code() == 200) {
                        List<Vendedores> objVendedores = response.body();

                        if (objVendedores != null) {
                            for (Vendedores w : objVendedores) {
                                Vendedores vd = new Vendedores(
                                    w.getID(),
                                    w.getFUN_CODIGO(),
                                    w.getFUN_NOME(),
                                    w.getFUN_APELIDO(),
                                    w.getDSC_MARGEM()
                                );

                                arraylist.add(vd);
                            }
                        }

                        // Envia o resultado para a classe Adapter_Vendedores (Adapter)
                        adapterListView = new Adapter_Vendedores(Activity_Vendedores.this, arraylist);

                        // Binds the Adapter to the ListView
                        listView.setAdapter(adapterListView);

                        // finaliza o progrees
                        dialogo.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Vendedores>> call, Throwable t) {
                Toast.makeText(
                        Activity_Vendedores.this,
                        "Erro ao carregar os Produtos Erro:" + t.getMessage(),
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }


}

