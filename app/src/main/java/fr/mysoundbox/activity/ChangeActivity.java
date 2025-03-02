package fr.mysoundbox.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import fr.mysoundbox.R;
import fr.mysoundbox.activity.dialog.CustomDialogConfirmation;
import fr.mysoundbox.activity.dialog.CustomDialogInfos;
import fr.mysoundbox.bean.Sample;
import fr.mysoundbox.controller.MusicController;
import fr.mysoundbox.exception.TechnicalException;
import fr.mysoundbox.tools.MusicDataTools;

/**
 * Activité Change qui permet à l'utilisateur de modifier le sample associé au bouton voulu
 * <p>
 * Author: Jonathan B.
 * Created: 22/05/2018
 * Last Updated: 31/05/2018
 */
public class ChangeActivity extends AppCompatActivity implements View.OnClickListener {

    private MusicController musicCtrl;
    private int sampleId;
    private MediaPlayer mp = null;
    private boolean saved = true;
    private Sample currentSample;

    // Attributs d'IHM
    private View changeAnchor;
    private Button backManageButton;
    private Button infosButton;
    private EditText name;
    private TextView filename;
    private Button fileBrowse;
    private Button fileTest;
    private Button fileDefault;
    private Button saveButton;
    private ActivityResultLauncher<Intent> audioBrowseLauncher;

    /**
     * Création de l'activité
     *
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String INFO = "[INFO]";
        Log.e(INFO, "=> ChangeActivity");
        // Récupère les paramètres du bundle
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            sampleId = extras.getInt("sampleId");
        }
        // Initialisation du controller
        musicCtrl = new MusicController();
        // Initialisation du layout
        initLayout();
        // Initialisation des listeners
        initListeners();
        // Récupère les MusicDataFile
        initMusicDataFiles();
        // Récupère le sample actif
        initSample();
        // Initialisation de l'affichage
        initDisplay();
        // Init du bouton retour
        initBackButton();
        // Init de l'exploreur de fichiers
        initFileBrowser();
    }

    /**
     * Démarrage de l'activité
     */
    @Override
    protected void onStart() {
        super.onStart();
        // Initialisation de l'audio
        initAudio();
    }

    /**
     * Fermeture de l'activité
     */
    @Override
    protected void onStop() {
        super.onStop();
        // Fermeture de l'audio
        closeAudio();
    }

    /**
     * Initialise le layout et récupère les éléments
     */
    private void initLayout() {
        // Charge le layout
        setContentView(R.layout.activity_change);
        // Change la couleur de la barre de navigation
        initNavigationBarColor();
        // Récupère les éléments du layout
        changeAnchor = findViewById(R.id.changeAnchor);
        backManageButton = findViewById(R.id.backManageButton);
        infosButton = findViewById(R.id.infosButton);
        name = findViewById(R.id.name);
        filename = findViewById(R.id.filename);
        fileBrowse = findViewById(R.id.fileBrowse);
        fileTest = findViewById(R.id.fileTest);
        fileDefault = findViewById(R.id.fileDefault);
        saveButton = findViewById(R.id.saveButton);
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
     * Initialise les listeners
     */
    private void initListeners() {
        // Bouton 'Retour'
        backManageButton.setOnClickListener(this);
        // Bouton 'Infos'
        infosButton.setOnClickListener(this);
        // Bouton 'Parcourir'
        fileBrowse.setOnClickListener(this);
        // Bouton 'Tester'
        fileTest.setOnClickListener(this);
        // Bouton 'Par défaut'
        fileDefault.setOnClickListener(this);
        // Bouton 'Sauvegarder'
        saveButton.setOnClickListener(this);
    }

    /**
     * Initialisation des deux fichiers MusicDataFile (celui par défaut et celui personnalisé)
     */
    private void initMusicDataFiles() {
        try {
            // Initialisation des MusicDataFiles
            musicCtrl.initMusicDataFiles(this);
        } catch (TechnicalException tex) {
            Log.e("ERROR", "Impossible d'obtenir le MusicDataFile et le MusicDataDefaultFile !");
            Log.e("ERROR", "Fermeture de l'activé !!");
            // Ferme l'activité
            closeActivity();
        }
    }

    /**
     * Initialise le sample actif
     */
    private void initSample() {
        currentSample = musicCtrl.getSample(sampleId);
        if (currentSample == null) {
            Log.e("ERROR", "Impossible de récupérer le sample !");
            Log.e("ERROR", "Fermeture de l'activé !");
            // Ferme l'activité
            closeActivity();
        }
    }

    /**
     * Initialise l'affichage
     */
    private void initDisplay() {
        // Initialise le focus sur l'ancre (invisible) pour éviter le focus sur l'EditText
        changeAnchor.setFocusableInTouchMode(true);
        changeAnchor.requestFocus();

        // Récupération des infos du sample
        String sampleName = currentSample.name();
        String sampleFilename = currentSample.filename();

        // Remplis les champs
        name.setText(sampleName);
        filename.setText(sampleFilename);
    }

    /**
     * Initialise le bouton de retour de l'appareil
     */
    private void initBackButton() {
        OnBackPressedDispatcher dispatcher = getOnBackPressedDispatcher();
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Dialog de confirmation de clôture
                confirmExit();
            }
        };
        dispatcher.addCallback(this, callback);
    }

    /**
     * Initialise l'exploreur de fichiers
     */
    private void initFileBrowser() {
        // Initialisation du ActivityResultLauncher
        audioBrowseLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Uri audioUri = data.getData();
                            if (audioUri != null) {
                                // Traitez l'URI du fichier audio sélectionné
                                handleAudioUri(audioUri);
                            }
                        }
                    }
                }
        );
    }

    /**
     * Traite l'URI du fichier audio sélectionné
     *
     * @param audioUri Uri
     */
    private void handleAudioUri(Uri audioUri) {

        // Prendre des permissions persistantes sur l'URI
        if ("content".equals(audioUri.getScheme())) {
            try {
                // Prendre des permissions de lecture persistantes
                getContentResolver().takePersistableUriPermission(
                        audioUri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                );
            } catch (SecurityException e) {
                Log.e("ChangeActivity", "Impossible de prendre des permissions persistantes: " + e.getMessage());
            }
        }

        // Récupère le nom  et le nom du fichier
        String resultName = getFileName(audioUri);
        String resultFilename = MusicDataTools.getFileName(this, audioUri);

        // Met à jour le nom et le nom du fichier sélectionné
        name.setText(resultName);
        filename.setText(resultFilename);

        // Met à jour le sample actuel
        currentSample = new Sample(sampleId, resultName, resultFilename, audioUri);

        // Modifie la variable 'saved'
        saved = false;

        // Focus sur le nom
        name.requestFocus();
    }

    /**
     * Initialise l'Audio Manager
     */
    private void initAudio() {
        // Définis le volume 'Media' du périphérique par défaut (au lieu de la sonnerie)
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    /**
     * Ferme le MediaPlayer si besoin
     */
    private void closeAudio() {
        // Ferme le MediaPlayer si besoin
        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;
        }
    }

    /**
     * Lors du clic sur un bouton, traitement en fonction du bouton
     *
     * @param view View
     */
    @Override
    public void onClick(View view) {
        // Bouton 'Retour'
        if (view == backManageButton) {
            // Dialog de confirmation de clôture
            confirmExit();
        }
        // Bouton 'Infos'
        if (view == infosButton) {
            infosDialog();
        }
        // Bouton 'Parcourir'
        if (view == fileBrowse) {
            openAudioExplorer();
        }
        // Bouton 'Tester'
        if (view == fileTest) {
            testSample();
        }
        // Bouton 'Par défaut'
        if (view == fileDefault) {
            reInitDialog();
        }
        // Bouton 'Sauvegarder'
        if (view == saveButton) {
            tryToSaveDialog();
        }
    }

    /**
     * Dialog perso d'affichage des informations
     */
    private void infosDialog() {
        // Création du dialog
        CustomDialogInfos infosDialog = new CustomDialogInfos(this, getString(R.string.infos_msg));
        infosDialog.show();
    }

    /**
     * Récupère le nom du fichier à partir de l'URI
     *
     * @param uri Uri
     * @return String
     */
    private String getFileName(Uri uri) {
        String result = null;
        if (Objects.equals(uri.getScheme(), "content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (displayNameIndex >= 0) {
                        result = cursor.getString(displayNameIndex);
                    }
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            if (result != null) {
                int cut = result.lastIndexOf('/');
                if (cut != -1) {
                    result = result.substring(cut + 1);
                }
            }
        }
        // Supprime l'extension
        result = removeExtension(result);
        return result;
    }

    /**
     * Supprime l'extension du nom d'un fichier
     *
     * @param fileName String
     * @return String
     */
    private String removeExtension(String fileName) {
        if (fileName != null && !fileName.isEmpty()) {
            int cutIndex = fileName.lastIndexOf(".");
            if (cutIndex != -1) {
                fileName = fileName.substring(0, cutIndex);
            }
        }
        return fileName;
    }

    /**
     * Ouvre l'exploreur de fichier audio
     */
    private void openAudioExplorer() {
        Intent browseIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        browseIntent.addCategory(Intent.CATEGORY_OPENABLE);
        browseIntent.setType("audio/*");
        audioBrowseLauncher.launch(browseIntent);
    }

    /**
     * Test le sample en récupérant la ressource depuis le stockage externe ou depuis les ressources du projet
     */
    private void testSample() {
        try {
            // Création du MediaPlayer
            mp = musicCtrl.createMediaPlayer(this, currentSample);
            // Lance le MediaPlayer
            mp.start();
            Toast.makeText(this, R.string.play_success, Toast.LENGTH_LONG).show();
        } catch (NullPointerException npe) {
            Toast.makeText(this, R.string.play_fail, Toast.LENGTH_LONG).show();
        } finally {
            // Arrête immédiatement le MediaPlayer
            closeAudio();
        }
    }

    /**
     * Dialog perso de confirmation de la ré-initialisation par défaut
     */
    private void reInitDialog() {
        // Initialisation du listener
        // Ré-initialise le sample par défaut
        CustomDialogConfirmation.myOnClickListener confirmationListener = this::reInit;
        // Création du dialog
        CustomDialogConfirmation confirmationDialog = new CustomDialogConfirmation(this, confirmationListener, getString(R.string.reinit_msg));
        confirmationDialog.show();
    }

    /**
     * Ré-initialise le sample par défaut et met à jour les variables
     */
    private void reInit() {
        try {
            // Ré-initialise le sample par défaut
            musicCtrl.reinitSample(this, sampleId);

            // Récupère le sample par défaut
            currentSample = musicCtrl.getSample(sampleId);

            // Toast de succès
            Toast.makeText(this, R.string.reinit_success, Toast.LENGTH_LONG).show();

            // Mise à jour des champs par défaut
            name.setText(currentSample.name());
            filename.setText(currentSample.filename());

            // Met à jour la variable 'saved'
            saved = true;
        } catch (TechnicalException e) {
            // Toast d'échec de ré-initialisation
            Toast.makeText(this, R.string.reinit_fail, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Dialog perso de confirmation de la sauvegarde
     */
    private void tryToSaveDialog() {
        // Initialisation du listener
        // Essaye de sauvegarder le sample perso
        CustomDialogConfirmation.myOnClickListener confirmationListener = this::tryToSave;
        // Création du dialog
        CustomDialogConfirmation confirmationDialog = new CustomDialogConfirmation(this, confirmationListener, getString(R.string.save_msg));
        confirmationDialog.show();
    }

    /**
     * Essaye d'enregistrer le sample perso
     */
    private void tryToSave() {
        if (name.getText() == null || name.getText().toString().trim().isEmpty()) {
            // Toast si le titre n'est pas renseigné correctement
            Toast.makeText(this, R.string.save_missing_title, Toast.LENGTH_LONG).show();
            name.requestFocus();
        } else if (filename.getText() == null || filename.getText().toString().trim().isEmpty()) {
            // Toast si le fichier n'est pas renseigné correctement
            Toast.makeText(this, R.string.save_missing_filename, Toast.LENGTH_LONG).show();
        } else {
            try {
                // Enregistre le sample
                Sample newSample = new Sample(sampleId - 1, currentSample.name(), currentSample.filename(), currentSample.uri());
                musicCtrl.saveSample(this, sampleId, newSample);
                Toast.makeText(this, R.string.save_success, Toast.LENGTH_LONG).show();
                saved = true;
                changeAnchor.requestFocus();
            } catch (TechnicalException e) {
                Toast.makeText(this, R.string.save_fail, Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Confirmation de fermeture de l'activité si non sauvegardé
     */
    private void confirmExit() {
        // Si sample non sauvegardé
        if (!saved) {
            // Création du dialog de confirmation
            CustomDialogConfirmation.myOnClickListener confirmationListener = this::closeActivity;
            CustomDialogConfirmation confirmationDialog = new CustomDialogConfirmation(this, confirmationListener, getString(R.string.exit_confirm_msg));
            confirmationDialog.show();
        } else {
            // Ferme l'activité
            closeActivity();
        }
    }

    /**
     * Ferme l'activité
     */
    private void closeActivity() {
        finish();
    }

}
