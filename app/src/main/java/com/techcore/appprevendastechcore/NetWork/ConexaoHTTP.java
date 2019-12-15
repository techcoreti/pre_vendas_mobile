package com.techcore.appprevendastechcore.NetWork;
import com.google.gson.Gson;
import com.techcore.appprevendastechcore.Configurar.StsSys;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;

public class ConexaoHTTP {
    private static Retrofit retrofit;
    private static final String BASE_URL = StsSys.getgUrlServer();

    public static Retrofit getRetrofitHTTP(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(new Gson()))
                    .build();
        }
        return retrofit;
    }

}
