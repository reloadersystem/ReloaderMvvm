package pe.lecordonbleu.reloader.vo

import com.google.gson.GsonBuilder
import pe.lecordonbleu.reloader.domain.WebService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    val webService by lazy{

        Retrofit.Builder()
                .baseUrl("http://200.123.1.66:8080/saa-rest/webresources/intranetSAA/")
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build().create(WebService::class.java)


    }
}