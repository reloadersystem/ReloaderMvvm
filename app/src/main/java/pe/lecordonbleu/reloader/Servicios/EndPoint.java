package pe.lecordonbleu.reloader.Servicios;


import okhttp3.ResponseBody;
import pe.lecordonbleu.reloader.data.model.FichaEstudiante;
import pe.lecordonbleu.reloader.data.model.FichaList;
import pe.lecordonbleu.reloader.entidades.RegisterFicha;
import pe.lecordonbleu.reloader.entidades.RegisterQR;
import pe.lecordonbleu.reloader.entidades.RegisterUser;
import pe.lecordonbleu.reloader.entidades.RegisterUserCorreo;
import pe.lecordonbleu.reloader.entidades.ResponseAuth;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Reloader on 03/03/22.
 */

public interface EndPoint {

    @POST("logueoEstudiante")
    Call<ResponseAuth> postVerUsuario(@Body RegisterUser user);

    @POST("logueoEstudianteCorreo")
    Call<ResponseAuth> postVerUsuarioCorreo(@Body RegisterUserCorreo user);


    @POST("estudianteQr")
    Call<ResponseBody> postVerUsuarioQR(@Body RegisterQR user);

    @POST("estudianteFicha")
    Call<FichaList> postFichaEstudiante(@Body RegisterFicha ficha);

}

