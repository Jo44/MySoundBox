package fr.mysoundbox.controller;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import fr.mysoundbox.bean.MusicDataFile;
import fr.mysoundbox.bean.Sample;
import fr.mysoundbox.exception.TechnicalException;
import fr.mysoundbox.tools.MusicDataTools;

/**
 * Controller en charge de la gestion des samples
 * <p>
 * Author: Jonathan B.
 * Created: 31/05/2018
 * Last Updated: 31/05/2018
 */
public class MusicController {

    /**
     * Attributs
     */
    private MusicDataFile musicDataFile;
    private MusicDataFile musicDataDefaultFile;

    /**
     * Initialise les 2 MusicDataFiles (par défaut et personnalisé)
     *
     * @param context Context
     */
    public void initMusicDataFiles(Context context) throws TechnicalException {
        // Récupère le MusicDataFile par défaut
        musicDataDefaultFile = MusicDataTools.readDefaultFile(context);
        try {
            // Récupère le MusicDataFile de l'utilisateur si il existe
            musicDataFile = MusicDataTools.readFile(context);
        } catch (TechnicalException tex) {
            // Sinon l'initialise
            musicDataFile = musicDataDefaultFile;
            // Puis l'enregistre
            MusicDataTools.writeFile(context, musicDataFile);
        }
    }

    /**
     * Ré-initialise un nouveau fichier MusicDataFile par défaut et renvoi un boolean pour confirmer succès ou non
     *
     * @param context Context
     * @return boolean
     */
    public boolean resetMusicDataFile(Context context) {
        boolean status;
        // Création du nouveau MusicDataFile par défaut
        musicDataFile = musicDataDefaultFile;

        try {
            // Enregistrement du nouveau fichier MusicDataFile
            MusicDataTools.writeFile(context.getApplicationContext(), musicDataFile);
            status = true;
        } catch (TechnicalException tex) {
            status = false;
        }
        return status;
    }

    /**
     * Initialisation de la liste des 12 samples utilisés
     *
     * @return listSample List<Sample>
     */
    public List<Sample> getSamples() {
        // Initialise la liste des samples
        List<Sample> listSample = new ArrayList<>();
        // Ajoute chaque sample à la liste des samples
        for (int i = 1; i < 13; i++) {
            // Récupère le sample personnalisé si trouvé
            Sample sample = musicDataFile.getSample(i);
            if (sample != null && !sample.name().isEmpty() && !sample.filename().isEmpty() && sample.uri() != null) {
                listSample.add(musicDataFile.getSample(i));
            } else {
                // Sinon récupère le sample par défaut
                listSample.add(musicDataDefaultFile.getSample(i));
            }
        }
        return listSample;
    }

    /**
     * Initialisation du sample utilisé
     *
     * @param sampleId int
     * @return sample Sample
     */
    public Sample getSample(int sampleId) {
        // Récupère le sample personnalisé
        Sample sample = musicDataFile.getSample(sampleId);
        // Vérification du sample personnalisé
        if (sample == null || sample.name().isEmpty() || sample.filename().isEmpty() || sample.uri() == null) {
            // Récupère le sample par défaut
            sample = musicDataDefaultFile.getSample(sampleId);
        }
        return sample;
    }

    /**
     * Ré-initialisation d'un sample par défaut
     *
     * @param context  Context
     * @param sampleId int
     */
    public void reinitSample(Context context, int sampleId) throws TechnicalException {
        // Initialise le sample par défaut
        Sample newSample = musicDataDefaultFile.getSample(sampleId);
        // Remplace l'ancien sample perso par celui-ci
        musicDataFile.setSample(sampleId, newSample);
        // Sauvegarde le fichier
        MusicDataTools.writeFile(context.getApplicationContext(), musicDataFile);
    }

    /**
     * Mise à jour d'un sample
     *
     * @param context   Context
     * @param sampleId  int
     * @param newSample Sample
     */
    public void saveSample(Context context, int sampleId, Sample newSample) throws TechnicalException {
        // Remplace l'ancien sample perso par celui-ci
        musicDataFile.setSample(sampleId, newSample);
        // Sauvegarde le fichier
        MusicDataTools.writeFile(context.getApplicationContext(), musicDataFile);
    }

    /**
     * Créer le MediaPlayer en fonction du sample
     *
     * @param context Context
     * @param sample  Sample
     */
    public MediaPlayer createMediaPlayer(Context context, Sample sample) {
        MediaPlayer mp = null;
        try {
            Uri uri = sample.uri();
            String scheme = uri.getScheme();

            // Création du MediaPlayer
            mp = new MediaPlayer();

            // Traitement selon le schéma URI
            if ("android.resource".equals(scheme)) {
                // Pour les ressources internes
                try (AssetFileDescriptor afd = context.getContentResolver().openAssetFileDescriptor(uri, "r")) {
                    if (afd != null) {
                        mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                    }
                }
            } else if ("file".equals(scheme)) {
                // Pour les URI de type file://
                mp.setDataSource(uri.getPath());
            } else {
                // Pour content:// et autres schémas
                mp.setDataSource(context, uri);
            }

            // Préparation commune
            mp.prepare();
        } catch (Exception e) {
            Log.e("MusicController", "Erreur MediaPlayer: " + e.getMessage());
            if (mp != null) {
                mp.release();
            }
            mp = null;
        }
        return mp;
    }

}
