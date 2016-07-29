package com.jaysyko.wrestlechat.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.jaysyko.wrestlechat.network.NetworkState;

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isConnected = NetworkState.isConnected(context);
        NetworkState.getInstance().setConnected(isConnected);
    }
}
