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
 * Dialog Perso pour confirmation (avec message et listener)
 * <p>
 * Author: Jonathan B.
 * Created: 22/05/2018
 * Last Updated: 22/05/2018
 */
public class CustomDialogConfirmation extends AlertDialog {

    /**
     * Attributs
     */
    private final myOnClickListener myOnClickListener;
    private final String message;

    /**
     * Constructeur
     */
    public CustomDialogConfirmation(Context context, myOnClickListener myOnClickListener, String message) {
        super(context, R.style.CustomAlertDialog);
        this.myOnClickListener = myOnClickListener;
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
        setContentView(R.layout.dialog_confirmation);
        // Récupération des éléments du layout
        TextView messageView = findViewById(R.id.confirm_msg);
        Button btnYes = findViewById(R.id.alertBtnYes);
        Button btnNo = findViewById(R.id.alertBtnNo);
        // Affiche du message personnalisé
        messageView.setText(message);
        // Déclaration du listener des boutons
        View.OnClickListener listener = view -> {
            if (view == btnYes) {
                myOnClickListener.onConfirmationClick();
                dismiss();
            }
            if (view == btnNo) {
                dismiss();
            }
        };
        btnYes.setOnClickListener(listener);
        btnNo.setOnClickListener(listener);
    }

    /**
     * Interface
     */
    public interface myOnClickListener {
        void onConfirmationClick();
    }

}