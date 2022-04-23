package pe.lecordonbleu.reloader.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FichaEstudiante(

        @SerializedName("url_pcs_docu")
        val url_pcs_docu: String = "",

        @SerializedName("pcs_docu_nombre")
        val pcs_docu_nombre: String = "",

        @SerializedName("flag_val")
        val flag_val: Int = 0) : Parcelable


data class FichaList(val getfichaList: List<FichaEstudiante> = listOf())
