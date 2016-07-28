package com.jaysyko.wrestlechat.playServices;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.jaysyko.wrestlechat.application.eLog;
import com.jaysyko.wrestlechat.dialogs.Dialog;

/**
 * @author Jay Syko on 2016-07-27.
 */
public class PlayServices {

    private static final String TAG = PlayServices.class.getSimpleName();

    public static boolean isPlayServicesInstalled = false;

    /**
     * Check whether Google Play Services are available.
     *
     * If not, then display dialog allowing user to update Google Play Services
     *
     * @return true if available, or false if not
     */
    public static boolean checkPlayServices(Activity activity) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(activity);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
                alertDialogBuilder.setTitle("Google Play Services not installed");
                alertDialogBuilder.setMessage("Certain features might not be available or might work incorrectly");

                alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        eLog.i(TAG, "Ok to no GPS");
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            } else {
                Log.i(TAG, "This device is not supported.");
                Dialog.makeToast(activity, "Play Services not installed");
            }
            isPlayServicesInstalled = false;
            return false;
        }
        isPlayServicesInstalled = true;
        return true;
    }
}
