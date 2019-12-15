package com.techcore.appprevendastechcore.Interfaces;
import com.techcore.appprevendastechcore.Modelos.ListarPreVendas;
import com.techcore.appprevendastechcore.Modelos.LoginConfig;
import com.techcore.appprevendastechcore.Modelos.LoginVendedor;
import com.techcore.appprevendastechcore.Modelos.Vendedores;
import com.techcore.appprevendastechcore.Modelos.CarregarProdutos;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface Ler_DadosRemotos {

    @GET
    Call<List<Vendedores>> getVendedores(@Url String url);

    @GET
    Call<List<CarregarProdutos>> listarProdutos(@Url String url);

    @GET
    Call<List<LoginConfig>> getConfig(@Url String url);

    @GET
    Call<List<ListarPreVendas>> getPreVendas(@Url String url);

    @GET
    Call<List<LoginVendedor>> getLoginVendedor(@Url String url);
}

