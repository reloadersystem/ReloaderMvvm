package pe.lecordonbleu.reloader.Azure

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import pe.lecordonbleu.reloader.Servicios.Constantes
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun getConfiguration(): Retrofit {

    val interceptor = HttpLoggingInterceptor()
    interceptor.level = HttpLoggingInterceptor.Level.BODY
    val okHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

    val root = Constantes.AUTH_PHOTO

    return Retrofit.Builder()
            .baseUrl(root)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

}