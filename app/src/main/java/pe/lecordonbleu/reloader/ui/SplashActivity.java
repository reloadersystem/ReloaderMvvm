package pe.lecordonbleu.reloader.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.transition.Transition;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import pe.lecordonbleu.reloader.Azure.MicrosoftLogin;
import pe.lecordonbleu.reloader.R;
import pe.lecordonbleu.reloader.utils.MisPreferencias;


public class SplashActivity extends AppCompatActivity {
    Animation animation;
    TextView tvSplash;
    ImageView ivLogo;

    private Transition transition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ivLogo = findViewById(R.id.ivLogo);
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        ivLogo.startAnimation(animation);


        animation.setAnimationListener(new Animation.AnimationListener() {
            private static final long DURATION_TRANSITION = 1000;

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                SharedPreferences sharpref = getSharedPreferences("SharePreferenceICBL", MODE_PRIVATE);

                if (sharpref.contains("savelogin")) {

                    int estadoLogin = Integer.parseInt(MisPreferencias.obtenerValor(getApplicationContext(), "savelogin"));

                    if (estadoLogin == 1) {
                        Intent intent = new Intent(SplashActivity.this, MenuPrincipal.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else if (estadoLogin == 2) {
                        Intent intent = new Intent(SplashActivity.this, MicrosoftLogin.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }


                } else {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
}
