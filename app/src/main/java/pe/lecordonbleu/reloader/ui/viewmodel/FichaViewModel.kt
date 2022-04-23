package pe.lecordonbleu.reloader.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import pe.lecordonbleu.reloader.domain.Repo
import pe.lecordonbleu.reloader.vo.Resource


class FichaViewModel(private val repo: Repo) : ViewModel() {


    val fetchFichaList = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(repo.getFichaList()))
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }
}