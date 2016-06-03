package com.jaysyko.wrestlechat.network;

import android.content.Context;

//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.toolbox.Volley;

/**
 * NetworkSingleton.java
 *
 * Queues up Volley Requests to be sent out
 * @author Jay Syko
 */
public class NetworkSingleton {
    private static NetworkSingleton mInstance;
    private Context mContext;
//    private RequestQueue mRequestQueue;

    private NetworkSingleton(Context context) {
        this.mContext = context;
//        this.mRequestQueue = getRequestQueue();
    }

    /**
     * Get an instance of the class
     *
     * @param context of the calling Class
     * @return instance
     */
    public static synchronized NetworkSingleton getInstance(Context context) {
        if(mInstance == null){
            mInstance = new NetworkSingleton(context);
        }
        return mInstance;
    }

    /**
     * Get the active request queue
     * @return the Volley request queue
     */
//    private RequestQueue getRequestQueue() {
//        if (mRequestQueue == null) {
//            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
//        }
//        return mRequestQueue;
//    }

    /**
     * Add a Volley Request to the request queue
     * @param request Volley Request
     * @param <T> Request Type
     */
//    public <T> void addToRequestQueue(Request<T> request) {
//        getRequestQueue().add(request);
//    }
}