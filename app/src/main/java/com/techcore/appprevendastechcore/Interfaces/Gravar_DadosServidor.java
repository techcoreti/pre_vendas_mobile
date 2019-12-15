package com.techcore.appprevendastechcore.Interfaces;
import com.techcore.appprevendastechcore.Modelos.PostPedido;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Gravar_DadosServidor {
    @FormUrlEncoded
    @POST("/mobile/GravarVenda/")
    Call<List<PostPedido>> GravarVenda(
            @Field("Codigo") String Codigo,
            @Field("CodigoEmpresa") String CodigoEmpresa,
            @Field("CodigoVendedor") int CodigoVendedor,
            @Field("vlrSemDesconto") float vlrSemDesconto,
            @Field("vlrDoDesconto") float vlrDoDesconto,
            @Field("vlrComDesconto") float vlrComDesconto);

    @FormUrlEncoded
    @POST("/mobile/GravarProdutosVenda")
    Call<List<PostPedido.PostProdutos>> GravarProdutosVenda(
            @Field("CodigoVenda") String CodigoVenda,
            @Field("CodigoProduto") String CodigoProduto,
            @Field("QtdeProduto") float QtdeProduto);

}
