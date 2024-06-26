package pe.lecordonbleu.reloader.Azure;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.microsoft.identity.client.AuthenticationCallback;
import com.microsoft.identity.client.AuthenticationResult;
import com.microsoft.identity.client.IAccount;
import com.microsoft.identity.client.PublicClientApplication;
import com.microsoft.identity.client.exception.MsalClientException;
import com.microsoft.identity.client.exception.MsalException;
import com.microsoft.identity.client.exception.MsalServiceException;
import com.microsoft.identity.client.exception.MsalUiRequiredException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import pe.lecordonbleu.reloader.R;
import pe.lecordonbleu.reloader.Servicios.Constantes;
import pe.lecordonbleu.reloader.Servicios.EndPoint;
import pe.lecordonbleu.reloader.Servicios.MethodWs;
import pe.lecordonbleu.reloader.entidades.RegisterUserCorreo;
import pe.lecordonbleu.reloader.entidades.ResponseAuth;
import pe.lecordonbleu.reloader.ui.MenuPrincipal;
import pe.lecordonbleu.reloader.utils.ConnectionDetector;
import pe.lecordonbleu.reloader.utils.MisPreferencias;
import retrofit2.Call;
import retrofit2.Callback;

public class MicrosoftLogin extends AppCompatActivity {

    final static String SCOPES[] = {"https://graph.microsoft.com/User.Read"};
    final static String MSGRAPH_URL = "https://graph.microsoft.com/v1.0/me";
    final static String CLIENT_ID = "e12cac02-0384-4494-8d68-e5c839011e8c";

    //final static String MSGRAPH_URL_PHOTO = "https://graph.microsoft.com/v1.0/me/photo/$value";

    final static String MSGRAPH_URL_PHOTO = "https://graph.microsoft.com/beta/me/photo/$value";

    /* UI & Debugging Variables */
    private static final String TAG = MicrosoftLogin.class.getSimpleName();
    Button callGraphButton;
    Button signOutButton;

    JsonObjectRequest request = null;

    JsonObjectRequest photorequest = null;

    private Bitmap loadedImage;

    String TOKEN = "";

    /* Azure AD Variables */
    private PublicClientApplication sampleApp;
    private AuthenticationResult authResult;

    String a, b, c, d, e, f, g, h;

    Button loging;
    CircleImageView imageView;
    String tokenInit = "";

    ConnectionDetector conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_azure);

        conn = new ConnectionDetector(getApplicationContext());

        callGraphButton = (Button) findViewById(R.id.callGraph);
        signOutButton = (Button) findViewById(R.id.clearCache);
        imageView = findViewById(R.id.img_photo);

        loging = findViewById(R.id.loging);

        callGraphButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (conn.isConnected()) {
                    onCallGraphClicked();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.action_internet), Toast.LENGTH_SHORT).show();
                }
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onSignOutClicked();
            }
        });

        loging.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (conn.isConnected()) {

                    final String usuario = "06954236@cordonbleu.edu.pe";
                    int uneg = 2;
                    String nivel = "e";

                    RegisterUserCorreo registerUserCorreo = new RegisterUserCorreo(
                            usuario,
                            uneg,
                            nivel);

                    EndPoint endPoint = MethodWs.getConfiguration().create(EndPoint.class);
                    Call<ResponseAuth> responseBodyEmail = endPoint.postVerUsuarioCorreo(registerUserCorreo);
                    responseBodyEmail.enqueue(new Callback<ResponseAuth>() {
                        @Override
                        public void onResponse(Call<ResponseAuth> call, retrofit2.Response<ResponseAuth> response) {
                            if (response.code() == Constantes.INSTANCE.getSUCCESS()) {
                                try {

                                    ResponseAuth responsedata = response.body();

                                    int flag = responsedata.flag;

                                    if (flag == 0) {
                                        Toast.makeText(getApplicationContext(), " usuario o contraseña incorrecto", Toast.LENGTH_SHORT).show();
                                    } else if (flag == 1) {

                                        String dataCarrera = responsedata.dataCarrera;
                                        String dataMenu = responsedata.dataMenu;
                                        String dataPerfil = responsedata.dataPerfil;
                                        String mensaje = responsedata.mensaje;

                                        JSONObject jsonObjectPerfil = new JSONObject(dataPerfil);
                                        String id_estud = String.valueOf(jsonObjectPerfil.getInt("id_estud"));

                                        MisPreferencias.guardarValor(getApplicationContext(), "savelogin", "2");
                                        MisPreferencias.guardarValor(getApplicationContext(), "id_estud", id_estud);
                                        MisPreferencias.guardarValor(getApplicationContext(), "dataCarrera", dataCarrera);
                                        MisPreferencias.guardarValor(getApplicationContext(), "dataMenu", dataMenu);
                                        MisPreferencias.guardarValor(getApplicationContext(), "dataPerfil", dataPerfil);
                                        MisPreferencias.guardarValor(getApplicationContext(), "mensaje", mensaje);
                                        MisPreferencias.guardarValor(getApplicationContext(), "email", usuario);


                                        // InicioFragment.userID(id_estud);

                                        Intent i = new Intent(MicrosoftLogin.this, MenuPrincipal.class);
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


//                Intent intent = new Intent(MicrosoftLogin.this, MenuPrincipal.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);

                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.action_internet), Toast.LENGTH_SHORT).show();
                }

            }
        });

        /* Configure your sample app and save state for this activity */
        sampleApp = null;
        if (sampleApp == null) {
            sampleApp = new PublicClientApplication(
                    this.getApplicationContext(),
                    R.raw.auth_config);
        }

        /* Attempt to get a user and acquireTokenSilent
         * If this fails we do an interactive request
         */
        List<IAccount> accounts = null;

        try {
            accounts = sampleApp.getAccounts();

            if (accounts != null && accounts.size() == 1) {
                /* We have 1 account */

                sampleApp.acquireTokenSilentAsync(SCOPES, accounts.get(0), getAuthSilentCallback());
            } else {
                /* We have no account or >1 account */
            }
        } catch (IndexOutOfBoundsException e) {
            Log.d(TAG, "Account at this position does not exist: " + e.toString());
        }

    }

    //
    // Core Identity methods used by MSAL7
    // ==================================
    // onActivityResult() - handles redirect from System browser
    // onCallGraphClicked() - attempts to get tokens for graph, if it succeeds calls graph & updates UI
    // onSignOutClicked() - Signs account out of the app & updates UI
    // callGraphAPI() - called on successful token acquisition which makes an HTTP request to graph
    //

    /* Handles the redirect from the System Browser */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        sampleApp.handleInteractiveRequestRedirect(requestCode, resultCode, data);
    }

    /* Use MSAL to acquireToken for the end-user
     * Callback will call Graph api w/ access token & update UI
     */
    private void onCallGraphClicked() {
        sampleApp.acquireToken(getActivity(), SCOPES, getAuthInteractiveCallback());
    }

    /* Clears an account's tokens from the cache.
     * Logically similar to "sign out" but only signs out of this app.
     */
    private void onSignOutClicked() {

        /* Attempt to get a account and remove their cookies from cache */
        List<IAccount> accounts = null;

        try {
            accounts = sampleApp.getAccounts();

            if (accounts == null) {
                /* We have no accounts */

            } else if (accounts.size() == 1) {
                /* We have 1 account */
                /* Remove from token cache */
                sampleApp.removeAccount(accounts.get(0));
                updateSignedOutUI();

            } else {
                /* We have multiple accounts */
                for (int i = 0; i < accounts.size(); i++) {
                    sampleApp.removeAccount(accounts.get(i));
                }
            }

            Toast.makeText(getBaseContext(), "Signed Out!", Toast.LENGTH_SHORT)
                    .show();

            MisPreferencias.eliminarValor(getApplicationContext());

            imageView.setVisibility(View.GONE);


        } catch (IndexOutOfBoundsException e) {
            Log.d(TAG, "User at this position does not exist: " + e.toString());
        }
    }

    /* Use Volley to make an HTTP request to the /me endpoint from MS Graph using an access token */
    private void callGraphAPI() {
        Log.d(TAG, "Starting volley request to graph");

        /* Make sure we have a token to send to graph */
        if (authResult.getAccessToken() == null) {
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        JSONObject parameters = new JSONObject();

        try {
            parameters.put("key", "value");
        } catch (Exception e) {
            Log.d(TAG, "Failed to put parameters: " + e.toString());
        }


        request = new JsonObjectRequest(Request.Method.GET, MSGRAPH_URL,
                parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                /* Successfully called graph, process data and send to UI */
                //  Log.d(TAG, "Response: " + response.toString());
                //data completa de  entrega al  hacer peticion


                try {

//                    Log.d(TAG, "displayName: " + response.get("displayName"));
//                    Log.d(TAG, "jobTitle: " + response.get("jobTitle"));
//                    Log.d(TAG, "givenName: " + response.get("givenName"));
//                    Log.d(TAG, "mail: " + response.get("mail"));
//                    Log.d(TAG, "officeLocation: " + response.get("officeLocation"));
//                    Log.d(TAG, "surname: " + response.get("surname"));
//                    Log.d(TAG, "mobilePhone: " + response.get("mobilePhone"));
//                    Log.d(TAG, "id: " + response.get("id"));

                    a = "" + response.get("displayName");
                    b = "" + response.get("jobTitle");
                    // c = "givenName: " + response.get("givenName");
                    d = "" + response.get("mail");
                    e = "Localización: " + response.get("officeLocation");
                    //f = "surname: " + response.get("surname");
                    g = "mobilePhone: " + response.get("mobilePhone");
                    //h = "id: " + response.get("id");


                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                Log.d(TAG,"url" + request.getUrl());
                updateGraphUI(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error: " + error.toString());
            }//fin
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + authResult.getAccessToken());
                return headers;
            }
        };

        Log.d(TAG, "Adding HTTP GET to Queue, Request: " + request.toString());

        request.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    // Helper methods manage UI updates
    // ================================
    // updateGraphUI() - Sets graph response in UI
    // updateSuccessUI() - Updates UI when token acquisition succeeds
    // updateSignedOutUI() - Updates UI when app sign out succeeds
    //

    /* Sets the graph response */
    private void updateGraphUI(JSONObject graphResponse) {
        TextView graphText = (TextView) findViewById(R.id.graphData);
        //graphText.setText(graphResponse.toString());

//        graphText.setText(a + " \n" + b + " \n" + c + " \n" + d + " \n" + e + " \n" + f + " \n" + g + " \n" + h);
        graphText.setText(a + " \n" + b + " \n" + d + " \n" + e + " \n" + g);
    }

    /* Set the UI for successful token acquisition data */
    private void updateSuccessUI() {
        callGraphButton.setVisibility(View.GONE);
        signOutButton.setVisibility(View.VISIBLE);
        loging.setVisibility(View.VISIBLE);
        findViewById(R.id.welcome).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.welcome)).setText("Welcome, " +
                authResult.getAccount().getUsername());
        tokenInit = authResult.getAccessToken();

        //https://docs.microsoft.com/en-us/graph/api/profilephoto-get?view=graph-rest-1.0#path-parameters

        //Log.v("tokenInit", tokenInit);
        ImageService(tokenInit);
        findViewById(R.id.graphData).setVisibility(View.VISIBLE);
    }

    private void ImageService(String tokenData) {

        ServicesApiInterface microsoftWS = HelperWSMicrosoftKt.getConfiguration().create(ServicesApiInterface.class);
        Call<ResponseBody> response = microsoftWS.getImageMicrosoft(Constantes.INSTANCE.getAUTH() + tokenData, "image/jpeg");

        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    Bitmap bmp = BitmapFactory.decodeStream(response.body().byteStream());
                    imageView.setVisibility(View.VISIBLE);
                    imageView.setImageBitmap(bmp);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    /* Set the UI for signed out account */
    private void updateSignedOutUI() {
        callGraphButton.setVisibility(View.VISIBLE);
        signOutButton.setVisibility(View.INVISIBLE);
        loging.setVisibility(View.INVISIBLE);
        findViewById(R.id.welcome).setVisibility(View.INVISIBLE);
        findViewById(R.id.graphData).setVisibility(View.INVISIBLE);
        ((TextView) findViewById(R.id.graphData)).setText("No Data");
    }

    //
    // App callbacks for MSAL
    // ======================
    // getActivity() - returns activity so we can acquireToken within a callback
    // getAuthSilentCallback() - callback defined to handle acquireTokenSilent() case
    // getAuthInteractiveCallback() - callback defined to handle acquireToken() case
    //

    public Activity getActivity() {
        return this;
    }

    /* Callback used in for silent acquireToken calls.
     * Looks if tokens are in the cache (refreshes if necessary and if we don't forceRefresh)
     * else errors that we need to do an interactive request.
     */
    private AuthenticationCallback getAuthSilentCallback() {
        return new AuthenticationCallback() {
            @Override
            public void onSuccess(AuthenticationResult authenticationResult) {
                /* Successfully got a token, call graph now */
                Log.d(TAG, "Successfully authenticated");

                /* Store the authResult */
                authResult = authenticationResult;

                /* call graph */
                callGraphAPI();

                /* update the UI to post call graph state */
                updateSuccessUI();
            }

            @Override
            public void onError(MsalException exception) {
                /* Failed to acquireToken */
                Log.d(TAG, "Authentication failed: " + exception.toString());

                if (exception instanceof MsalClientException) {
                    /* Exception inside MSAL, more info inside MsalError.java */
                } else if (exception instanceof MsalServiceException) {
                    /* Exception when communicating with the STS, likely config issue */
                } else if (exception instanceof MsalUiRequiredException) {
                    /* Tokens expired or no session, retry with interactive */
                }
            }

            @Override
            public void onCancel() {
                /* User canceled the authentication */
                Log.d(TAG, "User cancelled login.");
            }
        };
    }

    /* Callback used for interactive request.  If succeeds we use the access
     * token to call the Microsoft Graph. Does not check cache
     */
    private AuthenticationCallback getAuthInteractiveCallback() {
        return new AuthenticationCallback() {
            @Override
            public void onSuccess(AuthenticationResult authenticationResult) {
                /* Successfully got a token, call graph now */
                Log.d(TAG, "Successfully authenticated");
                Log.d(TAG, "ID Token: " + authenticationResult.getIdToken());

                /* Store the auth result */
                authResult = authenticationResult;

                /* call graph */


                callGraphAPI();


                /* update the UI to post call graph state */
                updateSuccessUI();
            }

            @Override
            public void onError(MsalException exception) {
                /* Failed to acquireToken */
                Log.d(TAG, "Authentication failed: " + exception.toString());

                if (exception instanceof MsalClientException) {
                    /* Exception inside MSAL, more info inside MsalError.java */
                } else if (exception instanceof MsalServiceException) {
                    /* Exception when communicating with the STS, likely config issue */
                }
            }

            @Override
            public void onCancel() {
                /* User canceled the authentication */
                Log.d(TAG, "User cancelled login.");
            }
        };


    }


}