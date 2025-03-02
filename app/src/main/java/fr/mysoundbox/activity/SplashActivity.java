package fr.mysoundbox.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

import fr.mysoundbox.R;

/**
 * Activité Splashscreen pendant 2 secondes
 * <p>
 * Author: Jonathan B.
 * Created: 18/05/2018
 * Last Updated: 18/05/2018
 */
@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    /**
     * Attributs
     */
    private Timer timer;
    private ProgressBar spinner;

    /**
     * Création de l'activité
     *
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String INFO = "[INFO]";
        Log.e(INFO, "=> SplashActivity");
        // Initialisation du layout
        initLayout();
        // Initialise l'affichage
        setDisplay();
    }

    /**
     * Focus sur l'activité
     */
    @Override
    protected void onResume() {
        super.onResume();
        // Début du timer (2sec) avant de lancer la suite
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Redirige vers l'activité Login
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                closeActivity();
            }
        }, 2000);
    }

    /**
     * Lors de la fermeture de l'activité, stop le timer
     */
    @Override
    protected void onStop() {
        super.onStop();
        if (timer != null) {
            timer.cancel();
        }
    }

    /**
     * Initialise le layout et récupère les éléments
     */
    private void initLayout() {
        // Charge le layout
        setContentView(R.layout.activity_splash);
        // Change la couleur de la barre de navigation
        initNavigationBarColor();
        // Récupère les éléments du layout
        spinner = findViewById(R.id.loadingCircle);
    }

    /**
     * Change la couleur de la barre de navigation
     */
    @SuppressWarnings("deprecation")
    private void initNavigationBarColor() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setNavigationBarColor(getResources().getColor(android.R.color.transparent));
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    /**
     * Initialise l'affichage
     */
    private void setDisplay() {
        // Affiche la Loading Bar
        spinner.setVisibility(View.VISIBLE);
    }

    /**
     * Ferme l'activité
     */
    private void closeActivity() {
        finish();
    }

}
