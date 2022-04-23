package pe.lecordonbleu.reloader.Azure

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface ServicesApiInterface {

    @GET("\$value")
    fun getImageMicrosoft(@Header("Authorization") authHeader: String,
                          @Header("content-type") conteType: String): Call<ResponseBody>


}
