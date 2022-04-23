package pe.lecordonbleu.reloader.domain

import pe.lecordonbleu.reloader.data.model.FichaList
import pe.lecordonbleu.reloader.entidades.RegisterFicha
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface WebService {

//    @POST("estudianteFicha")
//    fun postFichaEstudiante(@Body ficha: RegisterFicha): Call<FichaEstudiante>

    @POST("estudianteFicha")
    suspend fun postFichaEstudiante(@Body ficha: RegisterFicha): FichaList


}