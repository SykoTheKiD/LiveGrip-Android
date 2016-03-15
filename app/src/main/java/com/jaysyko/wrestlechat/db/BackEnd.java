package com.jaysyko.wrestlechat.db;

import android.app.Activity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jaysyko.wrestlechat.utils.DBConstants;

import org.json.JSONException;
import org.json.JSONObject;

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

    public BackEnd(Activity activity) {
        requestQueue = Volley.newRequestQueue(activity);
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
    private QueryResult queryDB(String endpoint) {
        final QueryResult[] queryResponse = {null};
        stringRequest = new StringRequest(Request.Method.POST, DBConstants.MYSQL_URL.concat(endpoint), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    queryResponse[0] = new QueryResult(new JSONObject(response));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                requestQueue.add(stringRequest);
                return params;
            }
        };

        return queryResponse[0];
    }

    public synchronized void save() {

    }
}
