package com.jaysyko.wrestlechat.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by jarushaan on 2016-03-17
 */
public class NetworkSingleton {
    private static NetworkSingleton mInstance;
    private Context mContext;
    private RequestQueue mRequestQueue;

    private NetworkSingleton(Context context) {
        this.mContext = context;
        this.mRequestQueue = getRequestQueue();
    }

    public static synchronized NetworkSingleton getInstance(Context context) {
        if(mInstance == null){
            mInstance = new NetworkSingleton(context);
        }
        return mInstance;
    }

    private RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request) {
        getRequestQueue().add(request);
    }
}