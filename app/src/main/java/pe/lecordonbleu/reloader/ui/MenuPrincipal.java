package pe.lecordonbleu.reloader.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;
import com.microsoft.identity.client.IAccount;
import com.microsoft.identity.client.PublicClientApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pe.lecordonbleu.reloader.R;
import pe.lecordonbleu.reloader.entidades.RegisterMenu;
import pe.lecordonbleu.reloader.ui.VistasFragment.QRFragment;
import pe.lecordonbleu.reloader.ui.VistasFragment.SendFragment;
import pe.lecordonbleu.reloader.ui.VistasFragment.ShareFragment;
import pe.lecordonbleu.reloader.utils.MisPreferencias;

public class MenuPrincipal extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;

    private String dataCarrera;
    private String dataMenu;
    private String dataPerfil;
    private String mensaje;
    private String id_estud;
    private String email;

    QRFragment qrFragment;
    InicioFragment item1Fragment;
    SendFragment item2Fragment;
    ShareFragment item3Fragment;

    private String usuario_email;
    ArrayList<RegisterMenu> menuArrayList;

    Fragment fragmentBase = null;

    private PublicClientApplication sampleApp;

    //autoUpdate
    private static final int REQ_CODE_VERSION_UPDATE = 530;
    private AppUpdateManager appUpdateManager;
    private InstallStateUpdatedListener installStateUpdatedListener;
    private static final String TAG_UPDATE = "VERIFY_UPDATE";

    private BottomNavigationView mNavigationBottom;
    private TextView txtAbrevPerfil, txtTitletoolbar;
    private ImageView imgMenuToolbar;


    DrawerLayout drawer;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        txtAbrevPerfil = findViewById(R.id.txt_perfiltoolbar);
        txtTitletoolbar = findViewById(R.id.txt_titletoolbar);

        txtTitletoolbar.setText(R.string.login_title);
        imgMenuToolbar = findViewById(R.id.img_menutoolbar);

        menuArrayList = new ArrayList<>();

        checkForAppUpdate();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mNavigationBottom = findViewById(R.id.bottom_menuprincipal);
        TextView txtNombre = navigationView.getHeaderView(0).findViewById(R.id.txt_nombre);
        txtAbrevPerfil.setOnClickListener(v -> {
            navigationImage();

        });

        imgMenuToolbar.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(MenuPrincipal.this, v);
            popupMenu.getMenuInflater().inflate(R.menu.menu_principal, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {

                int id = item.getItemId();

                if (id == R.id.action_cesion) {

                    List<IAccount> accounts = null;

                    try {
                        accounts = sampleApp.getAccounts();

                        if (accounts == null) {
                            /* We have no accounts */

                        } else if (accounts.size() == 1) {
                            /* We have 1 account */
                            /* Remove from token cache */
                            sampleApp.removeAccount(accounts.get(0));
                            //updateSignedOutUI();

                        } else {
                            /* We have multiple accounts */
                            for (int i = 0; i < accounts.size(); i++) {
                                sampleApp.removeAccount(accounts.get(i));
                            }
                        }

                        Toast.makeText(getBaseContext(), "Signed Out!", Toast.LENGTH_SHORT)
                                .show();

                        MisPreferencias.eliminarValor(getApplicationContext());

                        Intent intent = new Intent(MenuPrincipal.this, SplashActivity.class);
                        startActivity(intent);
                        finish();

                    } catch (IndexOutOfBoundsException e) {
                        Log.d("SigInOut", "User at this position does not exist: " + e.toString());
                    }

                } else if (id == R.id.action_inicio) {
                    fragmentBase = new InicioFragment();
                    mNavigationBottom.setSelectedItemId(R.id.navigation_item1);
                    BaseFragment();
                }
                return false;
            });
            popupMenu.show();
        });

        fragmentBase = new
                InicioFragment();
        BaseFragment();


        SharedPreferences sharpref = getSharedPreferences("SharePreferenceICBL", MODE_PRIVATE);

        if (sharpref.contains("id_estud")) {

            id_estud = MisPreferencias.obtenerValor(getApplicationContext(), "id_estud");
            dataCarrera = MisPreferencias.obtenerValor(getApplicationContext(), "dataCarrera");
            dataMenu = MisPreferencias.obtenerValor(getApplicationContext(), "dataMenu");
            dataPerfil = MisPreferencias.obtenerValor(getApplicationContext(), "dataPerfil");
            mensaje = MisPreferencias.obtenerValor(getApplicationContext(), "mensaje");
            email = MisPreferencias.obtenerValor(getApplicationContext(), "email");


            try {
                JSONArray jsonArrayCarrera = new JSONArray(dataCarrera);
                //Log.v("dataCarrera", String.valueOf(jsonArrayCarrera));

                JSONArray jsonArrayMenu = new JSONArray(dataMenu);
                Log.v("dataMenu", String.valueOf(jsonArrayMenu));

                for (int idx = 0; idx < jsonArrayMenu.length(); idx++) {
                    JSONObject arrayItem = jsonArrayMenu.getJSONObject(idx);

                    String icono = arrayItem.getString("icono");
                    String textoMenuAbrev = arrayItem.getString("textoMenuAbrev");
                    String textoMenu = arrayItem.getString("textoMenu");
                    int idMenu = arrayItem.getInt("idMenu");
                    int orden = arrayItem.getInt("orden");
                    int nivel = arrayItem.getInt("nivel");
                    int idMenuPadre = arrayItem.getInt("idMenuPadre");


                    RegisterMenu registerMenu = new RegisterMenu();
                    registerMenu.setIcono(icono);
                    registerMenu.setTextoMenuAbrev(textoMenuAbrev);
                    registerMenu.setTextoMenu(textoMenu);
                    registerMenu.setIdMenu(idMenu);
                    registerMenu.setOrden(orden);
                    registerMenu.setNivel(nivel);
                    registerMenu.setIdMenuPadre(idMenuPadre);
                    menuArrayList.add(registerMenu);
                }

                Menu nav_Menu = navigationView.getMenu();

                int code = nav_Menu.getItem(0).getItemId();
                // Log.v("codeID", String.valueOf(code));


//                for (int m = 0; m < menuArrayList.size(); m++) {
//
//                    int nivelmenu = menuArrayList.get(m).getNivel();
//
//                    if (nivelmenu == 1) {
//                        String menuTitle = menuArrayList.get(m).getTextoMenu();
//                        nav_Menu.getItem(m).setTitle(menuTitle);
//                        nav_Menu.getItem(m).setVisible(true);
//                    }
//                }

                JSONObject jsonObjectPerfil = new JSONObject(dataPerfil);
                int id_estud = jsonObjectPerfil.getInt("id_estud");
                Log.v("dataPerfil", String.valueOf(jsonObjectPerfil));

                String nombre = jsonObjectPerfil.getString("personaNombre");
                String apePaterno = jsonObjectPerfil.getString("personaPaterno");
                String apeMaterno = jsonObjectPerfil.getString("personaMaterno");

                txtNombre.setText(nombre + " " + apePaterno + " " + apeMaterno);
                txtAbrevPerfil.setText(nombre.substring(0, 1));

                // setTitle(String.format("%s / %s", tema + " - Page " + (currentpage + 1), contadorpag));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        sampleApp = null;
        if (sampleApp == null) {
            sampleApp = new PublicClientApplication(
                    this.getApplicationContext(),
                    R.raw.auth_config);
        }

        mNavigationBottom.setOnNavigationItemSelectedListener(menuItem ->

        {
            switch (menuItem.getItemId()) {

                case R.id.navigation_item1:

                    fragmentBase = new InicioFragment();
                    BaseFragment();
                    return true;

                case R.id.navigation_item2:
                    fragmentBase = new QRFragment();
                    BaseFragment();
                    return true;

                case R.id.navigation_item3:

                    fragmentBase = new SendFragment();
                    BaseFragment();
                    return true;

                case R.id.navigation_item4:
                    fragmentBase = new ShareFragment();
                    BaseFragment();
                    return true;

                default:
                    return false;
            }

        });

    }

    private void navigationImage() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {

            case R.id.action_cesion: {

                List<IAccount> accounts = null;

                try {
                    accounts = sampleApp.getAccounts();

                    if (accounts == null) {
                        /* We have no accounts */

                    } else if (accounts.size() == 1) {
                        /* We have 1 account */
                        /* Remove from token cache */
                        sampleApp.removeAccount(accounts.get(0));
                        //updateSignedOutUI();

                    } else {
                        /* We have multiple accounts */
                        for (int i = 0; i < accounts.size(); i++) {
                            sampleApp.removeAccount(accounts.get(i));
                        }
                    }

                    Toast.makeText(getBaseContext(), "Signed Out!", Toast.LENGTH_SHORT)
                            .show();

                    MisPreferencias.eliminarValor(getApplicationContext());

                    Intent intent = new Intent(MenuPrincipal.this, SplashActivity.class);
                    startActivity(intent);
                    finish();

                } catch (IndexOutOfBoundsException e) {
                    Log.d("SigInOut", "User at this position does not exist: " + e.toString());
                }
                break;
            }

            case R.id.action_inicio: {
                Fragment fragmentPrincipal = new InicioFragment();
                FragmentManager fmanager = this.getSupportFragmentManager();
                if (fmanager != null) {
                    FragmentTransaction ftransaction = fmanager.beginTransaction();
                    if (ftransaction != null) {
                        ftransaction.replace(R.id.contentPrincipalMenu, fragmentPrincipal);
                        ftransaction.commit();
                    }
                }
                break;
            }

        }
        return super.onOptionsItemSelected(item);
    }


    private void BaseFragment() {
        FragmentManager fmanager = this.getSupportFragmentManager();
        if (fmanager != null) {
            FragmentTransaction ftransaction = fmanager.beginTransaction();
            if (ftransaction != null) {
                ftransaction.replace(R.id.contentPrincipalMenu, fragmentBase);
//                ftransaction.addToBackStack("");
                ftransaction.commit();
            }
        }
    }

    private void checkForAppUpdate() {
        // Creates instance of the manager.
        appUpdateManager = AppUpdateManagerFactory.create(getApplicationContext());

        // Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        // Create a listener to track request state updates.
        installStateUpdatedListener = new InstallStateUpdatedListener() {
            @Override
            public void onStateUpdate(InstallState installState) {
                // Show module progress, log state, or install the update.
                if (installState.installStatus() == InstallStatus.DOWNLOADED)
                    // After the update is downloaded, show a notification
                    // and request user confirmation to restart the app.
                    popupSnackbarForCompleteUpdateAndUnregister();
            }
        };

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                    // Request the update.
                    if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {

                        // Before starting an update, register a listener for updates.
                        appUpdateManager.registerListener(installStateUpdatedListener);
                        // Start an update.
                        startAppUpdateFlexible(appUpdateInfo);
                    } else if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                        // Start an update.
                        startAppUpdateImmediate(appUpdateInfo);
                    }
                }

            }
        });
    }

    private void startAppUpdateImmediate(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.IMMEDIATE,
                    // The curren
                    this,
                    // Include a request code to later monitor this update request.
                    MenuPrincipal.REQ_CODE_VERSION_UPDATE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    private void startAppUpdateFlexible(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.FLEXIBLE,
                    // The current activity making the update request.
                    this,
                    // Include a request code to later monitor this update request.
                    MenuPrincipal.REQ_CODE_VERSION_UPDATE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
            unregisterInstallStateUpdListener();
        }
    }

    private void popupSnackbarForCompleteUpdateAndUnregister() {
        Snackbar snackbar =
                Snackbar.make(findViewById(R.id.drawer_layout), "New app is Ready", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Install", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appUpdateManager.completeUpdate();
            }
        });
        snackbar.setActionTextColor(getResources().getColor(R.color.colorAccent));
        snackbar.show();

        unregisterInstallStateUpdListener();
    }

    private void checkNewAppVersionState() {
        appUpdateManager
                .getAppUpdateInfo()
                .addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
                    @Override
                    public void onSuccess(AppUpdateInfo appUpdateInfo) {
                        //FLEXIBLE:
                        // If the update is downloaded but not installed,
                        // notify the user to complete the update.
                        if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                            popupSnackbarForCompleteUpdateAndUnregister();
                        }

                        //IMMEDIATE:
                        if (appUpdateInfo.updateAvailability()
                                == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                            // If an in-app update is already running, resume the update.
                            startAppUpdateImmediate(appUpdateInfo);
                        }
                    }
                });
    }

    private void unregisterInstallStateUpdListener() {
        if (appUpdateManager != null && installStateUpdatedListener != null)
            appUpdateManager.unregisterListener(installStateUpdatedListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case REQ_CODE_VERSION_UPDATE:
                if (resultCode != RESULT_OK) { //RESULT_OK / RESULT_CANCELED / RESULT_IN_APP_UPDATE_FAILED
                    Log.d(TAG_UPDATE, "Update flow failed! Result code: " + resultCode);
                    // If the update is cancelled or fails,
                    // you can request to start the update again.
                    unregisterInstallStateUpdListener();
                }

                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkNewAppVersionState();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterInstallStateUpdListener();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        //String menuSelected = String.valueOf(menuItem.getTitle());
        //switch (menuItem.getItemId()) {
        //case "MI QR": {
        switch (menuItem.getItemId()) {

            case R.id.nav_slideshow:
                Uri uri = Uri.parse("https://www.ilcb.edu.pe/noticias");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
