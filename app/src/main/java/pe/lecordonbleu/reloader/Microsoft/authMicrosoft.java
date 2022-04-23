package pe.lecordonbleu.reloader.Microsoft;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.microsoft.identity.client.AuthenticationResult;
import com.microsoft.identity.client.IAccount;
import com.microsoft.identity.client.exception.MsalException;

import java.util.List;

import pe.lecordonbleu.reloader.ui.MenuPrincipal;
import pe.lecordonbleu.reloader.R;

public class authMicrosoft extends AppCompatActivity implements MSALAuthenticationCallback {

    private final static String TAG = authMicrosoft.class.getSimpleName();

    private ProgressDialog progress;
    private ListView listEvents;
    private LinearLayout panelSignIn;
    private LinearLayout panelEvents;
    private LinearLayout panelLoadEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_microsoft);

        listEvents = findViewById(R.id.list_events);
        panelSignIn = findViewById(R.id.panel_signIn);
        panelEvents = findViewById(R.id.panel_events);
        panelLoadEvent = findViewById(R.id.panel_loadEvent);


        (findViewById(R.id.btn_signIn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSignin();
            }
        });

        (findViewById(R.id.btn_signOut)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSignout();
            }
        });

        (findViewById(R.id.btn_loadEvent)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLoadEvents();
            }
        });

        setPanelVisibility(true, false, false);


    }

    private void onSignout() {
        AuthenticationController authController = AuthenticationController.getInstance(this);
        authController.signout();

        setPanelVisibility(true, false, false);
    }

    private void onSignin() {
        AuthenticationController authController = AuthenticationController.getInstance(this);
        authController.doAcquireToken(this, this);
    }

    @Override
    public void onMsalAuthSuccess(AuthenticationResult authenticationResult) {

        IAccount user = authenticationResult.getAccount();


        Toast.makeText(authMicrosoft.this, "Hello " + user.getUsername()
                + " (" + user.getAccountIdentifier() + user.getHomeAccountIdentifier() + user.getEnvironment() + ")!", Toast.LENGTH_LONG
        ).show();

        setPanelVisibility(false, true, false);


    }

    @Override
    public void onMsalAuthError(MsalException exception) {
        Log.e(TAG, "Error authenticated", exception);

    }

    @Override
    public void onMsalAuthError(Exception exception) {
        Log.e(TAG, "Error authenticated", exception);

    }

    @Override
    public void onMsalAuthCancel() {

        Log.d(TAG, "Cancel authenticated");

    }


    private void onLoadEvents() {

        Intent intent = new Intent(authMicrosoft.this, MenuPrincipal.class);
        startActivity(intent);
//        MSGraphServiceController graphController = new MSGraphServiceController(this);
//
//        progress = ProgressDialog.show(this, "Loading", "Loading first 20 events...");
//        SettableFuture<List<String>> events = graphController.getEvents();
//        Futures.addCallback(events, new FutureCallback<List<String>>() {
//            @Override
//            public void onSuccess(final List<String> result) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(authMicrosoft.this, "Loaded events success!", Toast.LENGTH_LONG).show();
//                        progress.dismiss();
//                        bindEvents(result);
//                    }
//                });
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//
//            }
//        });
    }


    private void setPanelVisibility(Boolean showSignIn, Boolean showLoadEvents, Boolean showList) {
        panelSignIn.setVisibility(showSignIn ? View.VISIBLE : View.GONE);
        panelLoadEvent.setVisibility(showLoadEvents ? View.VISIBLE : View.GONE);
        panelEvents.setVisibility(showList ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (AuthenticationController.getInstance(this).getPublicClient() != null) {
            AuthenticationController.getInstance(this).getPublicClient().handleInteractiveRequestRedirect(requestCode, resultCode, data);
        }
    }

    private void bindEvents(List<String> events) {
        setPanelVisibility(false, false, true);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_expandable_list_item_1,
                events);
        listEvents.setAdapter(adapter);
    }

    public void onSignin(View view) {
    }

    public void onSignout(View view) {
    }
}
