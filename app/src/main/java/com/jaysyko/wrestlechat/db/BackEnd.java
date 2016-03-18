package com.jaysyko.wrestlechat.db;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * BackEnd.java
 * Contains the code for the backend of the app
 *
 * @author Jay Syko
 */
public class BackEnd {
    private RequestQueue requestQueue;

    public BackEnd(Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }


    /**
     * Query for results in the database
     *
     * @return List of results
     */
    public synchronized QueryResult execute(StringRequest request) {
        final QueryResult[] queryResponse = {null};
        requestQueue.add(request);
        return queryResponse[0];
    }
}

