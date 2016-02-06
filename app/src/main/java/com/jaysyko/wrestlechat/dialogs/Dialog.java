package com.jaysyko.wrestlechat.dialogs;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by jarushaan on 2016-02-05.
 */
public class Dialog {
    public static void makeToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static void makeDialog() {

    }
}
