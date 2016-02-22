package com.jaysyko.wrestlechat.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.jaysyko.wrestlechat.R;

/**
 * Dialog.java
 * Wrapper class to centralize Android dialogs
 *
 * @author Jay Syko
 */
public class Dialog {
    /**
     * Output a toast message
     * @param context Context
     * @param msg String
     */
    public static void makeToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * Display a snackbar message
     * @param view View
     * @param message String
     */
    public static void makeSnackBar(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE);
        snackbar.show();
    }

    /**
     * Display an popup dialog box with the Title of box being title
     * @param context Context
     * @param title String
     * @param bodyContent String
     */
    public static void makeDialog(Context context, String title, String bodyContent) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(bodyContent)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setIcon(R.drawable.ic_face_black_18dp)
                .show();
    }
}