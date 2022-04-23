package pe.lecordonbleu.reloader.domain

import pe.lecordonbleu.reloader.data.DataSource
import pe.lecordonbleu.reloader.data.model.FichaList
import pe.lecordonbleu.reloader.entidades.RegisterFicha

class RepoImpl(private val dataSource: DataSource,  private val registerFicha: RegisterFicha) : Repo {
    override suspend fun getFichaList(): FichaList = dataSource.getListFicha(registerFicha)
}

