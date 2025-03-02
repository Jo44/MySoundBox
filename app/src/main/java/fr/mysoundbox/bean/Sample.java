package fr.mysoundbox.bean;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Classe modèle d'un sample
 * <p>
 * Author: Jonathan B.
 * Created: 22/05/2018
 * Last Updated: 30/05/2018
 */
public record Sample(int index, String name, String filename, Uri uri) {

    /**
     * Constructeur
     */
    public Sample {
    }

    /**
     * ToString
     */
    @NonNull
    @Override
    public String toString() {
        return "{ Index : " + index + " , Name : " + name + " , Filename : " + filename + " , Uri : '" + uri.toString() + "' }";
    }

    /**
     * ToJSON
     */
    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        try {
            json.put("index", index);
            json.put("name", name);
            json.put("filename", filename);
            json.put("uri", uri);
        } catch (JSONException jsonex) {
            Log.e("ERROR", "Impossible de créer le JSON Sample !");
            json = null;
        }
        return json;
    }

}
