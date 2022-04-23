package pe.lecordonbleu.reloader.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;
import pe.lecordonbleu.reloader.Azure.MicrosoftLogin;
import pe.lecordonbleu.reloader.R;
import pe.lecordonbleu.reloader.Servicios.Constantes;
import pe.lecordonbleu.reloader.Servicios.EndPoint;
import pe.lecordonbleu.reloader.Servicios.MethodWs;
import pe.lecordonbleu.reloader.entidades.RegisterUser;
import pe.lecordonbleu.reloader.entidades.ResponseAuth;
import pe.lecordonbleu.reloader.utils.ConnectionDetector;
import pe.lecordonbleu.reloader.utils.MisPreferencias;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


//token: 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

/**
 * Created by Reloader on 03/03/22.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    Button btnIngresar;
    EditText edtUsuario;
    EditText edtContrasenia;
    ImageView img_microsoft;
    CheckBox chk_saveUser;
    private int chkStatus = 0;
    ConnectionDetector conn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        conn = new ConnectionDetector(getApplicationContext());

        getSupportActionBar().setTitle(getString(R.string.login_title));

        btnIngresar = findViewById(R.id.btnLogin);
        edtUsuario = findViewById(R.id.edt_usuario);
        img_microsoft = findViewById(R.id.img_microsoft);
        edtContrasenia = findViewById(R.id.edt_contrasenia);
        chk_saveUser = findViewById(R.id.chk_saveUser);
        img_microsoft.setOnClickListener(this);
        btnIngresar.setOnClickListener(this);

        chk_saveUser.setOnCheckedChangeListener(this);

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btnLogin: {

                if (conn.isConnected()) {
                    final String usuario = edtUsuario.getText().toString();
                    String contrasena = edtContrasenia.getText().toString();
                    int uneg = 2;
                    String nivel = "e";

                    RegisterUser registerUser = new RegisterUser(
                            usuario,
                            contrasena,
                            uneg,
                            nivel);

                    EndPoint endPoint = MethodWs.getConfiguration().create(EndPoint.class);
                    Call<ResponseAuth> responseBodyCall = endPoint.postVerUsuario(registerUser);
                    responseBodyCall.enqueue(new Callback<ResponseAuth>() {
                        @Override
                        public void onResponse(Call<ResponseAuth> call, Response<ResponseAuth> response) {

                            if (response.code() == Constantes.INSTANCE.getSUCCESS()) {

                                new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Informativo")
                                        .setContentText("Bienvenido");

                                try {

                                    ResponseAuth responsedata = response.body();

                                    int flag = responsedata.flag;

                                    if (flag == 0) {
                                        Toast.makeText(getApplicationContext(), getString(R.string.msg_error_login), Toast.LENGTH_SHORT).show();
                                    } else if (flag == 1) {

                                        if (chkStatus == 1) {
                                            MisPreferencias.guardarValor(getApplicationContext(), "savelogin", "1");
                                        }

                                        String dataCarrera = responsedata.dataCarrera;
                                        String dataMenu = responsedata.dataMenu;
                                        String dataPerfil = responsedata.dataPerfil;
                                        String mensaje = responsedata.mensaje;

                                        JSONObject jsonObjectPerfil = new JSONObject(dataPerfil);
                                        String id_estud = String.valueOf(jsonObjectPerfil.getInt("id_estud"));

                                        //InicioFragment.userID(id_estud);

                                        MisPreferencias.guardarValor(getApplicationContext(), "id_estud", id_estud);
                                        MisPreferencias.guardarValor(getApplicationContext(), "dataCarrera", dataCarrera);
                                        MisPreferencias.guardarValor(getApplicationContext(), "dataMenu", dataMenu);
                                        MisPreferencias.guardarValor(getApplicationContext(), "dataPerfil", dataPerfil);
                                        MisPreferencias.guardarValor(getApplicationContext(), "mensaje", mensaje);
                                        MisPreferencias.guardarValor(getApplicationContext(), "email", usuario);


                                        Intent i = new Intent(LoginActivity.this, MenuPrincipal.class);
//                                    i.putExtra("dataCarrera", dataCarrera);
//                                    i.putExtra("dataMenu", dataMenu);
//                                    i.putExtra("dataPerfil", dataPerfil);
//                                    i.putExtra("mensaje", mensaje);
//                                    i.putExtra("email", usuario);

                                        startActivity(i);
                                        finish();

                                    }


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseAuth> call, Throwable t) {

                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.action_internet), Toast.LENGTH_SHORT).show();
                }
                break;
            }

            case R.id.img_microsoft: {
                Intent i = new Intent(LoginActivity.this, MicrosoftLogin.class);
                startActivity(i);
                finish();
                break;
            }

        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (chk_saveUser.isChecked() == true) {
            chkStatus = 1;
        } else {
            chkStatus = 0;
        }
    }
}