package com.techcore.appprevendastechcore.Produtos;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.techcore.appprevendastechcore.Carrinho.Carrinho;
import com.techcore.appprevendastechcore.Configurar.StsSys;
import com.techcore.appprevendastechcore.Interfaces.Ler_DadosRemotos;
import com.techcore.appprevendastechcore.Modelos.CarregarProdutos;
import com.techcore.appprevendastechcore.R;
import com.techcore.appprevendastechcore.Vendedores.Lista_Vendedores;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.techcore.appprevendastechcore.NetWork.ConexaoHTTP.getRetrofitHTTP;

public class AListarProduto extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private ListView listView;
    private EditText editsearch;
    private ProgressDialog dialogo;
    private AdapterListView adapterListView = null;
    List<CarregarProdutos> arraylist = new ArrayList<>();
    String Url;

    // Conexão com o servidor rest
    Retrofit retrofit = getRetrofitHTTP();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_produto);

        // Pega a referência do listview para inserir o click
        listView = findViewById(R.id.lsView);
        listView.setOnItemClickListener(this);

        dialogo = ProgressDialog.show(AListarProduto.this, "Aguarde",
                "Carregando os produtos...");

        Url = StsSys.getgUrlServer() + "/mobile/ler_produtos?id_empresa=" + StsSys.gCodigoFilial;

        Ler_DadosRemotos retorno = retrofit.create(Ler_DadosRemotos.class);
        Call<List<CarregarProdutos>> produtos = retorno.listarProdutos(Url);
        produtos.enqueue(new Callback<List<CarregarProdutos>>() {

            @Override
            public void onResponse(Call<List<CarregarProdutos>> call, Response<List<CarregarProdutos>> response) {

                if (response.isSuccessful()) {
                    List<CarregarProdutos> produtos = response.body();

                    if (produtos != null) {
                        for (CarregarProdutos w : produtos) {
                            CarregarProdutos objetoEscola = new CarregarProdutos(
                                    w.getID(),
                                    w.getPRD_CODIGO(),
                                    w.getPRD_DESCRICAO(),
                                    w.getPRD_UNIDADE(),
                                    w.getPRD_VALOR(),
                                    R.drawable.semfotos,
                                    w.getSize()
                            );

                            arraylist.add(objetoEscola);
                        }
                    } else {
                        Toast.makeText(AListarProduto.this, "Erro: não foi retornado nenhum resultado", Toast.LENGTH_LONG).show();
                    }

                    // Pass results to ListViewAdapter Class
                    adapterListView = new AdapterListView(AListarProduto.this, arraylist);

                    // Binds the Adapter to the ListView
                    listView.setAdapter(adapterListView);

                    // finaliza o progrees
                    dialogo.dismiss();

                } else {
                    Toast.makeText(AListarProduto.this, "Erro: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<CarregarProdutos>> call, Throwable t) {
                Toast.makeText(AListarProduto.this, "Erro ao carregar a lista de produtos. Erro:" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });

        Button btnPrdSair = findViewById(R.id.btnPrdSair);
        btnPrdSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (StsSys.RefreshCarrinho) {
                    Intent intent = new Intent(AListarProduto.this, Carrinho.class);
                    startActivity(intent);
                    finish();
                }else{
                    finish();
                }
            }
        });

        editsearch = findViewById(R.id.search);
        editsearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = editsearch.getText().toString();

                // Se houver conteudo efetua a busca
                if (adapterListView != null) {
                    adapterListView.filter(text);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }
        });
    }

    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // Pega o item que foi selecionado.
        CarregarProdutos item = adapterListView.getItem(arg2);
        editsearch = findViewById(R.id.search);
        editsearch.setText("");

        Intent itent = new Intent(this, DadosProduto.class);
        Bundle params = new Bundle();

        params.putString("keyProduto", item.getPRD_DESCRICAO());
        params.putInt("keyCodigo", item.getPRD_CODIGO());
        params.putString("keyUnidade", item.getPRD_UNIDADE());
        params.putDouble("keyValor", item.getPRD_VALOR());
        params.putInt("keySize", item.getSize());

        // Colocar aqui o id do tributo
        itent.putExtras(params);
        startActivity(itent);

    }
}
