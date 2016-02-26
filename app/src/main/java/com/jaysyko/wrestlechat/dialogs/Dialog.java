package com.jaysyko.wrestlechat.dialogs;

import android.content.Context;
import android.widget.Toast;

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
}