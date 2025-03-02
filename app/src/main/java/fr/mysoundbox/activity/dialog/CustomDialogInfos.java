package fr.mysoundbox.activity.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import fr.mysoundbox.R;

/**
 * Dialog Perso pour affichage des informations (avec message)
 * <p>
 * Author: Jonathan B.
 * Created: 22/05/2018
 * Last Updated: 22/05/2018
 */
public class CustomDialogInfos extends AlertDialog {

    /**
     * Attributs
     */
    private final String message;

    /**
     * Constructeur
     */
    public CustomDialogInfos(Context context, String message) {
        super(context, R.style.CustomAlertDialog);
        this.message = message;
    }

    /**
     * Initialisation
     *
     * @param savedInstanceState Bundle
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Fenêtre Pop-Up
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Applique le layout du dialog
        setContentView(R.layout.dialog_infos);
        // Récupération des éléments du layout
        TextView messageView = findViewById(R.id.infos_msg);
        Button btnOk = findViewById(R.id.alertBtnOk);
        // Affiche du message personnalisé
        messageView.setText(message);
        // Déclaration du listener des boutons
        View.OnClickListener listener = view -> {
            if (view == btnOk) {
                dismiss();
            }
        };
        btnOk.setOnClickListener(listener);
    }

}