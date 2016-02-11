package com.jaysyko.wrestlechat.dialogs;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

public class Dialog {
    public static void makeToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static void makeSnackBar(View view) {
        Snackbar snackbar = Snackbar.make(view, "Internet Disconnected", Snackbar.LENGTH_INDEFINITE);
        snackbar.show();
    }
}
