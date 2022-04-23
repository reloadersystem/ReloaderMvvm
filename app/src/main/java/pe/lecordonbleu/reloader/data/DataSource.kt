package pe.lecordonbleu.reloader.data


import pe.lecordonbleu.reloader.data.model.FichaList
import pe.lecordonbleu.reloader.domain.WebService
import pe.lecordonbleu.reloader.entidades.RegisterFicha

class DataSource(private val webService: WebService) {

    suspend fun getListFicha(registerFicha: RegisterFicha): FichaList {
        return webService.postFichaEstudiante(registerFicha)
    }
}