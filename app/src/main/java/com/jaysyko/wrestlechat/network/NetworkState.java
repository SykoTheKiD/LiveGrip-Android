package com.jaysyko.wrestlechat.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.jaysyko.wrestlechat.application.Initializer;

import java.util.Observable;

/**
 * NetworkState.java
 * Tools to monitor state of the network on the device
 *
 * @author Jay Syko
 */

public class NetworkState extends Observable{
    private static boolean isConnected;
    private static NetworkState instance = new NetworkState();
    private NetworkState(){
        isConnected = isConnected(Initializer.getAppContext());
    }

    /**
     * Returns true or false depending on whether phone is connected to internet or not
     *
     * @param context Context
     * @return boolean
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager cn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf = cn.getActiveNetworkInfo();
        return nf != null && nf.isConnected();
    }

    public static boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
        setChanged();
        notifyObservers();
    }

    public static NetworkState getInstance(){
        return instance;
    }
}
