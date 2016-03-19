package com.jaysyko.wrestlechat.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by jarushaan on 2016-03-17
 */
public class NetworkSingleton {
    private static NetworkSingleton ourInstance = new NetworkSingleton();
    private Context mContext;
    private RequestQueue mRequestQueue = Volley.newRequestQueue(mContext);

    private NetworkSingleton() {
    }

    public static synchronized NetworkSingleton getInstance(Context context) {
        ourInstance.mContext = context;
        return ourInstance;
    }

    private RequestQueue getRequestQueue() {
        return ourInstance.mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request) {
        getRequestQueue().add(request);
    }
}
