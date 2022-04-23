package pe.lecordonbleu.reloader.domain

import pe.lecordonbleu.reloader.data.model.FichaList

interface Repo {

    suspend fun getFichaList(): FichaList
}