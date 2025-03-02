package fr.mysoundbox.activity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import fr.mysoundbox.R;
import fr.mysoundbox.bean.Sample;
import fr.mysoundbox.controller.MusicController;
import fr.mysoundbox.exception.TechnicalException;
import fr.mysoundbox.tools.VolumeListener;

/**
 * Activité en charge de la lecture des différents samples
 * <p>
 * Author: Jonathan B.
 * Created: 18/05/2018
 * Last Updated: 31/05/2018
 */
public class MainActivity extends AppCompatActivity implements OnClickListener, CompoundButton.OnCheckedChangeListener, MediaPlayer.OnCompletionListener, OnSeekBarChangeListener {

    /**
     * Attributs
     */
    // Attributs de classe
    private MusicController musicCtrl;
    private List<Sample> listSample;
    private MediaPlayer mp;
    private AudioManager audioManager = null;
    private VolumeListener volumeListener = null;

    // Attributs d'IHM
    private Button manageButton = null;
    private List<ToggleButton> listToggleButton = null;
    private ToggleButton samplePlay1 = null;
    private ToggleButton samplePlay2 = null;
    private ToggleButton samplePlay3 = null;
    private ToggleButton samplePlay4 = null;
    private ToggleButton samplePlay5 = null;
    private ToggleButton samplePlay6 = null;
    private ToggleButton samplePlay7 = null;
    private ToggleButton samplePlay8 = null;
    private ToggleButton samplePlay9 = null;
    private ToggleButton samplePlay10 = null;
    private ToggleButton samplePlay11 = null;
    private ToggleButton samplePlay12 = null;
    private SeekBar volumeSeekbar = null;
    private ImageView volumeDown = null;
    private ImageView volumeUp = null;

    /**
     * Création de l'activité
     *
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String INFO = "[INFO]";
        Log.e(INFO, "=> MainActivity");
        // Initialisation du controller
        musicCtrl = new MusicController();
        // Initialisation du layout
        initLayout();
        // Initialise la liste des toggle buttons
        initListToggleButtons();
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
        // Initialise les toggle buttons sur OFF
        initToggleButtons();
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
        setContentView(R.layout.activity_main);
        // Change la couleur de la barre de navigation
        initNavigationBarColor();
        // Récupère les éléments du layout
        manageButton = findViewById(R.id.manageButton);
        samplePlay1 = findViewById(R.id.samplePlay1);
        samplePlay2 = findViewById(R.id.samplePlay2);
        samplePlay3 = findViewById(R.id.samplePlay3);
        samplePlay4 = findViewById(R.id.samplePlay4);
        samplePlay5 = findViewById(R.id.samplePlay5);
        samplePlay6 = findViewById(R.id.samplePlay6);
        samplePlay7 = findViewById(R.id.samplePlay7);
        samplePlay8 = findViewById(R.id.samplePlay8);
        samplePlay9 = findViewById(R.id.samplePlay9);
        samplePlay10 = findViewById(R.id.samplePlay10);
        samplePlay11 = findViewById(R.id.samplePlay11);
        samplePlay12 = findViewById(R.id.samplePlay12);
        volumeSeekbar = findViewById(R.id.volumeSeekbar);
        volumeDown = findViewById(R.id.volumeDown);
        volumeUp = findViewById(R.id.volumeUp);
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
    private void initListToggleButtons() {
        listToggleButton = new ArrayList<>();
        listToggleButton.add(samplePlay1);
        listToggleButton.add(samplePlay2);
        listToggleButton.add(samplePlay3);
        listToggleButton.add(samplePlay4);
        listToggleButton.add(samplePlay5);
        listToggleButton.add(samplePlay6);
        listToggleButton.add(samplePlay7);
        listToggleButton.add(samplePlay8);
        listToggleButton.add(samplePlay9);
        listToggleButton.add(samplePlay10);
        listToggleButton.add(samplePlay11);
        listToggleButton.add(samplePlay12);
    }

    /**
     * Initialise les listeners
     */
    private void initListeners() {
        // Bouton 'Manage'
        manageButton.setOnClickListener(this);
        // Toggle Buttons
        for (ToggleButton toggleButton : listToggleButton) {
            if (toggleButton != null) {
                toggleButton.setOnCheckedChangeListener(this);
            }
        }
        // Barre de volume
        volumeSeekbar.setOnSeekBarChangeListener(this);
        volumeDown.setOnClickListener(this);
        volumeUp.setOnClickListener(this);
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
            listToggleButton.get(i).setText(sample.name());
            listToggleButton.get(i).setTextOn(sample.name());
            listToggleButton.get(i).setTextOff(sample.name());
            i++;
        }
    }

    /**
     * Initialise l'Audio Manager, la SeekBar et le listener de changement de volume via touches physiques
     */
    private void initAudio() {
        try {
            // Définis le volume 'Media' du périphérique par défaut (au lieu de la sonnerie)
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            // Initialise l'Audio Manager
            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            if (audioManager == null) {
                throw new TechnicalException("Impossible de récupérer l'Audio Manager !");
            }
            // Récupère le volume 'Media' du périphérique et le volume max
            int volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            // Initialise la barre de réglage du volume
            volumeSeekbar.setProgress(volume);
            volumeSeekbar.setMax(maxVolume);
            // Initialise le listener de changement de volume via touches physiques
            volumeListener = new VolumeListener(new Handler(Looper.getMainLooper()), audioManager, volumeSeekbar);
            // Enregistre le listener
            getApplicationContext().getContentResolver().registerContentObserver(android.provider.Settings.System.CONTENT_URI, true, volumeListener);
        } catch (Exception ex) {
            // Si erreur, ferme l'activité
            Log.e("ERROR", "Impossible d'initialiser l'audio !");
            // Ferme l'activité
            closeActivity();
        }
    }

    /**
     * Ferme le MediaPlayer si besoin puis supprime le listener de changement de volume via touches physiques
     */
    private void closeAudio() {
        // Désactive le son
        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;
        }
        // Supprime l'enregistrement du listener de changement du volume via touches physiques
        getApplicationContext().getContentResolver().unregisterContentObserver(volumeListener);
    }

    /**
     * Initialise les toogle buttons sur OFF lors du focus sur l'activité
     */
    private void initToggleButtons() {
        for (ToggleButton button : listToggleButton) {
            if (button != null && button.isChecked()) {
                button.setChecked(false);
                // Réinitialise la couleur de la police du bouton
                button.setTextColor(getResources().getColor(R.color.colorAccent, getTheme()));
            }
        }
    }

    /**
     * Lors du clic sur un bouton/image, traitement en fonction du bouton/image
     *
     * @param view View
     */
    @Override
    public void onClick(View view) {
        // Ouvre l'activité 'Manage'
        if (view == manageButton) {
            Intent manageIntent = new Intent(MainActivity.this, ManageActivity.class);
            startActivity(manageIntent);
        }
        // Boutons Volume +
        if (view == volumeUp) {
            changeVolumeUp(true);
        }
        // Boutons Volume -
        if (view == volumeDown) {
            changeVolumeUp(false);
        }
    }

    /**
     * Lors du clic sur un toggle button, traitement en fonction du toggle button
     *
     * @param buttonView CompoundButton
     * @param isChecked  boolean
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView != null) {
            // Si un toggle button est activé
            if (isChecked) {
                // Change la couleur de la police du bouton
                buttonView.setTextColor(getResources().getColor(R.color.green, getTheme()));
                // Désactive les autres toggle buttons si besoin
                turnOffOthers(buttonView.getId());
                // Lance le son en fonction du toggle button
                if (buttonView == samplePlay1) {
                    turnOnOne(0);
                }
                if (buttonView == samplePlay2) {
                    turnOnOne(1);
                }
                if (buttonView == samplePlay3) {
                    turnOnOne(2);
                }
                if (buttonView == samplePlay4) {
                    turnOnOne(3);
                }
                if (buttonView == samplePlay5) {
                    turnOnOne(4);
                }
                if (buttonView == samplePlay6) {
                    turnOnOne(5);
                }
                if (buttonView == samplePlay7) {
                    turnOnOne(6);
                }
                if (buttonView == samplePlay8) {
                    turnOnOne(7);
                }
                if (buttonView == samplePlay9) {
                    turnOnOne(8);
                }
                if (buttonView == samplePlay10) {
                    turnOnOne(9);
                }
                if (buttonView == samplePlay11) {
                    turnOnOne(10);
                }
                if (buttonView == samplePlay12) {
                    turnOnOne(11);
                }
            } else {
                // Si un bouton est désactivé
                // Remet la couleur de la police du bouton par défaut
                buttonView.setTextColor(getResources().getColor(R.color.colorAccent, getTheme()));
                // Désactive le son
                if (mp != null) {
                    mp.stop();
                    mp.release();
                    mp = null;
                }
            }
        }
    }

    /**
     * Désactive tous les autres toggle buttons hormis celui cliqué
     *
     * @param selectedToggleButtonId int
     */
    private void turnOffOthers(int selectedToggleButtonId) {
        // Parmis tous les toggle buttons
        for (ToggleButton toggleButton : listToggleButton) {
            // Si un toggle button est sélectionné et différent de celui cliqué
            if (toggleButton != null && toggleButton.getId() != selectedToggleButtonId && toggleButton.isChecked()) {
                // Désactive le toggle button
                toggleButton.setChecked(false);
            }
        }
    }

    /**
     * Active la lecture d'un sample
     *
     * @param selectedSample int
     */
    private void turnOnOne(int selectedSample) {
        // Récupère le sample à jouer
        Sample sample = listSample.get(selectedSample);

        // Initialise le Media Player en fonction du sample sélectionné
        mp = musicCtrl.createMediaPlayer(this, sample);

        // Si Media Player correctement initialisé, lance la lecture
        if (mp != null) {
            // Initialise le listener de fin de lecture du Media Player
            mp.setOnCompletionListener(this);
            // Puis démarre la lecture
            mp.start();
        } else {
            // Affiche un message d'erreur
            Toast.makeText(this, R.string.play_fail, Toast.LENGTH_LONG).show();

            // Désactive tous les Toggle Buttons
            initToggleButtons();
        }
    }

    /**
     * Change le volume en fonction du bouton Volume + ou -
     *
     * @param up boolean
     */
    private void changeVolumeUp(boolean up) {
        // Récupère le précédent volume 'Media' du périphérique
        int volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int volumeMax = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        // Détermine le nouveau volume en fonction du bouton
        if (up) {
            // Si volume + demandé
            if (volume >= volumeMax) {
                volume = volumeMax;
            } else {
                volume++;
            }
        } else {
            // Si volume - demandé
            if (volume <= 0) {
                volume = 0;
            } else {
                volume--;
            }
        }
        // Met à jour le volume de l'Audio Manager et de la volumeSeekBar
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_PLAY_SOUND);
        volumeSeekbar.setProgress(volume);
    }

    /**
     * Lors de la fin de lecture du Media Player
     *
     * @param currentMp MediaPlayer
     */
    @Override
    public void onCompletion(MediaPlayer currentMp) {
        // Désactive tous les toogle buttons encore activés
        initToggleButtons();
    }

    /**
     * Lors du changement du volume via la SeekBar
     *
     * @param seekBar  SeekBar
     * @param progress int
     * @param fromUser boolean
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        // Met à jour l'Audio Manager en fonction de la progression de la SeekBar
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
        Log.e("INFO", "Volume : " + progress);
    }

    /**
     * Inutilisé
     */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    /**
     * Ferme l'activité
     */
    private void closeActivity() {
        finish();
    }

}
