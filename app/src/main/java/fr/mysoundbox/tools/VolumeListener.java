package fr.mysoundbox.tools;

import android.database.ContentObserver;
import android.media.AudioManager;
import android.os.Handler;
import android.widget.SeekBar;

/**
 * Volume Listener - Ecoute le changement de volume via les touches physiques du périphérique et modifie la volumeSeekbar en conséquence
 * <p>
 * Author: Jonathan B.
 * Created: 18/05/2018
 * Last Updated: 20/05/2018
 */
public class VolumeListener extends ContentObserver {

    /**
     * Attributs
     */
    private final AudioManager audioManager;
    private final SeekBar volumeSeekbar;

    /**
     * Constructeur
     *
     * @param handler       Handler
     * @param audioManager  AudioManager
     * @param volumeSeekbar SeekBar
     */
    public VolumeListener(Handler handler, AudioManager audioManager, SeekBar volumeSeekbar) {
        super(handler);
        this.audioManager = audioManager;
        this.volumeSeekbar = volumeSeekbar;
    }

    /**
     * Lors d'un changement du volume via les touches physiques
     *
     * @param selfChange boolean
     */
    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        if (audioManager != null && volumeSeekbar != null) {
            // Récupère le volume
            int volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            // Règle la SeekBar
            volumeSeekbar.setProgress(volume);
        }
    }

}
