package pe.lecordonbleu.reloader.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.microsoft.identity.client.IAccount;
import com.microsoft.identity.client.PublicClientApplication;

import java.util.List;

import pe.lecordonbleu.reloader.R;
import pe.lecordonbleu.reloader.ui.VistasFragment.QRFragment;
import pe.lecordonbleu.reloader.ui.VistasFragment.SendFragment;
import pe.lecordonbleu.reloader.ui.VistasFragment.ShareFragment;
import pe.lecordonbleu.reloader.utils.MisPreferencias;

public class InicioActivity extends AppCompatActivity {

    private BottomNavigationView mNavigationBottom;

    QRFragment qrFragment;
    InicioFragment item1Fragment;
    SendFragment item2Fragment;
    ShareFragment item3Fragment;

    private PublicClientApplication sampleApp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_principal);

        getSupportActionBar().setTitle(getString(R.string.login_title));

        mNavigationBottom = findViewById(R.id.bottom_principal);

        item1Fragment = new InicioFragment();
        qrFragment = new QRFragment();
        item2Fragment = new SendFragment();
        item3Fragment = new ShareFragment();


        sampleApp = null;
        if (sampleApp == null) {
            sampleApp = new PublicClientApplication(
                    this.getApplicationContext(),
                    R.raw.auth_config);
        }


        setFragment(item1Fragment);

        mNavigationBottom.setOnNavigationItemSelectedListener(menuItem -> {

            switch (menuItem.getItemId()) {

                case R.id.navigation_item1:
                    setFragment(item1Fragment);
                    return true;

                case R.id.navigation_item2:
                    setFragment(qrFragment);
                    return true;

                case R.id.navigation_item3:
                    setFragment(item2Fragment);
                    return true;

                case R.id.navigation_item4:
                    setFragment(item3Fragment);
                    return true;

                default:
                    return false;
            }

        });
    }

    private void setFragment(Fragment itemFragment) {
        FragmentTransaction fragmentTransaction = InicioActivity.this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frLayoutContain, itemFragment);
        fragmentTransaction.commit();
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

                    Intent intent = new Intent(InicioActivity.this, SplashActivity.class);
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
                        ftransaction.replace(R.id.frLayoutContain, fragmentPrincipal);
                        ftransaction.commit();
                    }
                }
                break;
            }

        }
        return super.onOptionsItemSelected(item);
    }


}
