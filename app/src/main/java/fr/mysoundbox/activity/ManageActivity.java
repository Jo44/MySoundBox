package fr.mysoundbox.activity;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import fr.mysoundbox.R;
import fr.mysoundbox.activity.dialog.CustomDialogConfirmation;
import fr.mysoundbox.bean.Sample;
import fr.mysoundbox.controller.MusicController;
import fr.mysoundbox.exception.TechnicalException;

/**
 * Activité Manage qui permet d'accèder à l'activité de modification des différents samples ou de tous les ré-initialiser
 * <p>
 * Author: Jonathan B.
 * Created: 19/05/2018
 * Last Updated: 24/05/2018
 */
public class ManageActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * Attributs
     */
    // Attributs de classe
    private MusicController musicCtrl;
    private List<Sample> listSample;

    // Attributs d'IHM
    private Button backMainButton = null;
    private List<Button> listButton = null;
    private Button sampleChange1 = null;
    private Button sampleChange2 = null;
    private Button sampleChange3 = null;
    private Button sampleChange4 = null;
    private Button sampleChange5 = null;
    private Button sampleChange6 = null;
    private Button sampleChange7 = null;
    private Button sampleChange8 = null;
    private Button sampleChange9 = null;
    private Button sampleChange10 = null;
    private Button sampleChange11 = null;
    private Button sampleChange12 = null;
    private Button allReinitButton = null;

    /**
     * Création de l'activité
     *
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String INFO = "[INFO]";
        Log.e(INFO, "=> ManageActivity");
        // Initialisation du controller
        musicCtrl = new MusicController();
        // Initialisation du layout
        initLayout();
        // Initialise la liste des buttons
        initListButtons();
        // Initialisation des listeners
        initListeners();
    }

    /**
     * Démarrage de l'activité
     */
    @Override
    protected void onStart() {
        super.onStart();
        // Récupère les MusicDataFile
        initMusicDataFiles();
        // Initialisation des samples
        initSamples();
        // Initialisation de l'affichage
        initDisplay();
        // Initialisation de l'audio
        initAudio();
    }

    /**
     * Initialise le layout et récupère les éléments
     */
    private void initLayout() {
        // Charge le layout
        setContentView(R.layout.activity_manage);
        // Change la couleur de la barre de navigation
        initNavigationBarColor();
        // Récupère les éléments du layout
        backMainButton = findViewById(R.id.backMainButton);
        sampleChange1 = findViewById(R.id.sampleChange1);
        sampleChange2 = findViewById(R.id.sampleChange2);
        sampleChange3 = findViewById(R.id.sampleChange3);
        sampleChange4 = findViewById(R.id.sampleChange4);
        sampleChange5 = findViewById(R.id.sampleChange5);
        sampleChange6 = findViewById(R.id.sampleChange6);
        sampleChange7 = findViewById(R.id.sampleChange7);
        sampleChange8 = findViewById(R.id.sampleChange8);
        sampleChange9 = findViewById(R.id.sampleChange9);
        sampleChange10 = findViewById(R.id.sampleChange10);
        sampleChange11 = findViewById(R.id.sampleChange11);
        sampleChange12 = findViewById(R.id.sampleChange12);
        allReinitButton = findViewById(R.id.allReinitButton);
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
     * Initialise la liste des toggle buttons
     */
    private void initListButtons() {
        listButton = new ArrayList<>();
        listButton.add(sampleChange1);
        listButton.add(sampleChange2);
        listButton.add(sampleChange3);
        listButton.add(sampleChange4);
        listButton.add(sampleChange5);
        listButton.add(sampleChange6);
        listButton.add(sampleChange7);
        listButton.add(sampleChange8);
        listButton.add(sampleChange9);
        listButton.add(sampleChange10);
        listButton.add(sampleChange11);
        listButton.add(sampleChange12);
    }

    /**
     * Initialise les listeners
     */
    private void initListeners() {
        // Bouton 'Retour'
        backMainButton.setOnClickListener(this);
        // Bouton 'Modifier' des différents samples
        // Toggle Buttons
        for (Button button : listButton) {
            if (button != null) {
                button.setOnClickListener(this);
            }
        }
        // Bouton 'Tout ré-initialiser'
        allReinitButton.setOnClickListener(this);
    }

    /**
     * Initialisation des deux fichiers MusicDataFile (celui par défaut et celui personnalisé)
     */
    private void initMusicDataFiles() {
        try {
            // Initialisation des MusicDataFiles
            musicCtrl.initMusicDataFiles(this);
        } catch (TechnicalException tex) {
            Log.e("ERROR", "Impossible d'obtenir le MusicDataDefaultFile par défaut ou le MusicDataFile personnalisé !");
            Log.e("ERROR", "Fermeture de l'application !");
            // Ferme l'activité
            closeActivity();
        }
    }

    /**
     * Initialisation de la liste des 12 samples utilisés
     */
    private void initSamples() {
        listSample = musicCtrl.getSamples();
        if (listSample == null || listSample.size() != 12) {
            Log.e("ERROR", "Impossible de récupérer les 12 samples !");
            Log.e("ERROR", "Fermeture de l'application !");
            // Ferme l'activité
            closeActivity();
        }
    }

    /**
     * Initialisation de l'affichage
     */
    private void initDisplay() {
        // Pour chaque sample ..
        int i = 0;
        for (Sample sample : listSample) {
            // Affichage le nom sur le bouton associé
            listButton.get(i).setText(sample.name());
            i++;
        }
    }

    /**
     * Initialise l'Audio Manager
     */
    private void initAudio() {
        // Définis le volume 'Media' du périphérique par défaut (au lieu de la sonnerie)
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    /**
     * Lors du clic sur un bouton, traitement en fonction du bouton
     *
     * @param view View
     */
    @Override
    public void onClick(View view) {
        // Bouton 'Retour'
        if (view == backMainButton) {
            // Ferme l'activité
            closeActivity();
        }
        // Boutons 'Modifier'
        if (view == sampleChange1) {
            // Nouvelle activité 'Change'
            Intent changeIntent1 = new Intent(ManageActivity.this, ChangeActivity.class);
            // Charge le sampleId dans le bundle
            changeIntent1.putExtra("sampleId", 1);
            // Démarre l'activité
            startActivity(changeIntent1);
        }
        if (view == sampleChange2) {
            // Nouvelle activité 'Change'
            Intent changeIntent2 = new Intent(ManageActivity.this, ChangeActivity.class);
            // Charge le sampleId dans le bundle
            changeIntent2.putExtra("sampleId", 2);
            // Démarre l'activité
            startActivity(changeIntent2);
        }
        if (view == sampleChange3) {
            // Nouvelle activité 'Change'
            Intent changeIntent3 = new Intent(ManageActivity.this, ChangeActivity.class);
            // Charge le sampleId dans le bundle
            changeIntent3.putExtra("sampleId", 3);
            // Démarre l'activité
            startActivity(changeIntent3);
        }
        if (view == sampleChange4) {
            // Nouvelle activité 'Change'
            Intent changeIntent4 = new Intent(ManageActivity.this, ChangeActivity.class);
            // Charge le sampleId dans le bundle
            changeIntent4.putExtra("sampleId", 4);
            // Démarre l'activité
            startActivity(changeIntent4);
        }
        if (view == sampleChange5) {
            // Nouvelle activité 'Change'
            Intent changeIntent5 = new Intent(ManageActivity.this, ChangeActivity.class);
            // Charge le sampleId dans le bundle
            changeIntent5.putExtra("sampleId", 5);
            // Démarre l'activité
            startActivity(changeIntent5);
        }
        if (view == sampleChange6) {
            // Nouvelle activité 'Change'
            Intent changeIntent6 = new Intent(ManageActivity.this, ChangeActivity.class);
            // Charge le sampleId dans le bundle
            changeIntent6.putExtra("sampleId", 6);
            // Démarre l'activité
            startActivity(changeIntent6);
        }
        if (view == sampleChange7) {
            // Nouvelle activité 'Change'
            Intent changeIntent7 = new Intent(ManageActivity.this, ChangeActivity.class);
            // Charge le sampleId dans le bundle
            changeIntent7.putExtra("sampleId", 7);
            // Démarre l'activité
            startActivity(changeIntent7);
        }
        if (view == sampleChange8) {
            // Nouvelle activité 'Change'
            Intent changeIntent8 = new Intent(ManageActivity.this, ChangeActivity.class);
            // Charge le sampleId dans le bundle
            changeIntent8.putExtra("sampleId", 8);
            // Démarre l'activité
            startActivity(changeIntent8);
        }
        if (view == sampleChange9) {
            // Nouvelle activité 'Change'
            Intent changeIntent9 = new Intent(ManageActivity.this, ChangeActivity.class);
            // Charge le sampleId dans le bundle
            changeIntent9.putExtra("sampleId", 9);
            // Démarre l'activité
            startActivity(changeIntent9);
        }
        if (view == sampleChange10) {
            // Nouvelle activité 'Change'
            Intent changeIntent10 = new Intent(ManageActivity.this, ChangeActivity.class);
            // Charge le sampleId dans le bundle
            changeIntent10.putExtra("sampleId", 10);
            // Démarre l'activité
            startActivity(changeIntent10);
        }
        if (view == sampleChange11) {
            // Nouvelle activité 'Change'
            Intent changeIntent11 = new Intent(ManageActivity.this, ChangeActivity.class);
            // Charge le sampleId dans le bundle
            changeIntent11.putExtra("sampleId", 11);
            // Démarre l'activité
            startActivity(changeIntent11);
        }
        if (view == sampleChange12) {
            // Nouvelle activité 'Change'
            Intent changeIntent12 = new Intent(ManageActivity.this, ChangeActivity.class);
            // Charge le sampleId dans le bundle
            changeIntent12.putExtra("sampleId", 12);
            // Démarre l'activité
            startActivity(changeIntent12);
        }
        // Bouton 'Tout ré-initialiser'
        if (view == allReinitButton) {
            allReinitDialog();
        }
    }

    /**
     * Dialog perso de confirmation de la ré-initialisation
     */
    private void allReinitDialog() {
        // Initialisation du listener
        // Ré-initialise le fichier MusicDataFile
        CustomDialogConfirmation.myOnClickListener confirmationListener = this::allReinit;
        // Création du dialog
        CustomDialogConfirmation confirmationDialog = new CustomDialogConfirmation(this, confirmationListener, getString(R.string.reinit_samples_msg));
        confirmationDialog.show();
    }

    /**
     * Ré-initialise le fichier MusicDataFile personnalisé
     */
    private void allReinit() {
        // Ré-initialise le fichier MusicDataFile
        boolean reset = musicCtrl.resetMusicDataFile(this);
        if (reset) {
            Log.e("INFO", "Nouveau MusicDataFile initialisé");
            // Affiche le toast 'Ré-initialisation effectuée'
            Toast.makeText(this, R.string.reinit_success, Toast.LENGTH_LONG).show();
            // Met à jour la liste des samples
            initSamples();
            // Met à jour l'affichage
            initDisplay();
        } else {
            Log.e("ERROR", "Impossible de ré-initialiser le MusicDataFile !");
            // Affiche le toast 'Ré-initialisation échouée'
            Toast.makeText(this, R.string.reinit_fail, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Ferme l'activité
     */
    private void closeActivity() {
        finish();
    }

}
