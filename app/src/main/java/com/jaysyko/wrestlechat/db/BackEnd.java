package com.jaysyko.wrestlechat.db;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jaysyko.wrestlechat.utils.DBConstants;

import java.util.HashMap;
import java.util.Map;

/**
 * BackEnd.java
 * Contains the code for the backend of the app
 *
 * @author Jay Syko
 */
public class BackEnd {
    private RequestQueue requestQueue;
    private StringRequest stringRequest;

    public BackEnd(Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }

    /**
     * Query the app's cache
     *
     * @return List of results
     */
//    public QueryResult queryCache(Query query) {
//        return queryDB(query);
//    }

    /**
     * Delete a set from the cache
     */
    private void deleteFromCache() {

    }

    /**
     * Query for results in the database
     *
     * @return List of results
     */
    public synchronized QueryResult queryDB(String endpoint, final HashMap<String, String> params) {
        final QueryResult[] queryResponse = {null};
        stringRequest = new StringRequest(
                Request.Method.POST,
                DBConstants.MYSQL_URL.concat(endpoint),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("RE", response);
                        queryResponse[0] = new QueryResult(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("RE", error.getMessage());
                queryResponse[0] = new QueryResult(null);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };
        requestQueue.add(stringRequest);
        return queryResponse[0];
    }
}
