package com.jaysyko.wrestlechat.db;

import android.content.Context;
import android.util.Log;

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
        Log.e("ME", String.valueOf(context == null));
        requestQueue = Volley.newRequestQueue(context);
    }

    /**
     * Query the app's cache
     *
     * @return List of results
     */
//    public QueryResult queryCache(Query query) {
//        return execute(query);
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
    public synchronized QueryResult execute(StringRequest request) {
        final QueryResult[] queryResponse = {null};
        requestQueue.add(request);
        return queryResponse[0];
    }
}

/**
 * RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
 * StringRequest stringRequest = new StringRequest(Request.Method.POST, DBConstants.MYSQL_URL.concat("/newuser.php"), new Response.Listener<String>() {
 *
 * @Override public void onResponse(String response) {
 * Log.e("K", response);
 * }
 * }, new Response.ErrorListener() {
 * @Override public void onErrorResponse(VolleyError error) {
 * Log.e("E", error.getMessage());
 * }
 * }) {
 * @Override protected Map<String, String> getParams() {
 * HashMap<String, String> params = new HashMap<>();
 * params.put("username", username);
 * params.put("password", password);
 * return params;
 * }
 * };
 * queue.add(stringRequest);
 */
