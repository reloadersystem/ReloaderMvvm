package pe.lecordonbleu.reloader.Servicios;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MethodWs {

    public static Retrofit getConfiguration() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        String baseroot = Constantes.INSTANCE.getBASE_ROOT_INTRANET();
        String root = Constantes.INSTANCE.getURL_BASE_INTRANET();

        String url = String.format("%s%s", baseroot, root);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;

    }
}