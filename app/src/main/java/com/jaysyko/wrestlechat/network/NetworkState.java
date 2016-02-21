package com.jaysyko.wrestlechat.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * NetworkState.java
 * Tools to monitor state of the network on the device
 *
 * @author Jay Syko
 */

public class NetworkState {

    /**
     * Returns true or false depending on whether phone is connected to internet or not
     * @param context Context
     * @return boolean
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager cn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf = cn.getActiveNetworkInfo();
        return nf != null && nf.isConnected();
    }
}
