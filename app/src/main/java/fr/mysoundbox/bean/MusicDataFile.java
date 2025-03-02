package fr.mysoundbox.bean;

import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Classe modèle d'un fichier permettant de sauvegarder 12 samples
 * <p>
 * Author: Jonathan B.
 * Created: 20/05/2018
 * Last Updated: 30/05/2018
 */
public class MusicDataFile {

    /**
     * Attributs
     */
    private final List<Sample> listSample;

    /**
     * Constructeur
     *
     * @param listSample List<Sample>
     */
    public MusicDataFile(List<Sample> listSample) {
        this.listSample = listSample;
    }

    /**
     * ToString
     */
    @NonNull
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MusicDataFile : [ ");
        if (listSample == null || listSample.size() != 12) {
            sb.append("null");
        } else {
            for (Sample sample : listSample) {
                sb.append(sample.toString());
            }
        }
        sb.append(" ]");
        return sb.toString();
    }

    /**
     * ToJSON
     */
    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        // Vérifie que l'objet comporte bien une liste de 12 samples
        try {
            if (listSample != null && listSample.size() == 12) {
                JSONArray listJSONSample = new JSONArray();
                // Ajoute chaque sample à la JSONArray
                for (Sample sample : listSample) {
                    listJSONSample.put(sample.toJSONObject());
                }
                // Ajoute la JSONArray au JSONObject
                json.put("samples", listJSONSample);
            } else {
                throw new JSONException("L'objet MusicDataFile ne comporte pas 12 samples !");
            }
        } catch (JSONException jsonex) {
            Log.e("ERROR", "Impossible de créer le JSON MusicDataFile !");
            json = null;
        }
        return json;
    }

    /**
     * Getter / Setter
     */
    public Sample getSample(int sampleId) {
        return listSample.get(sampleId - 1);
    }

    public void setSample(int sampleId, Sample sample) {
        this.listSample.set(sampleId - 1, sample);
    }

}
