package com.jaysyko.wrestlechat.network;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.jaysyko.wrestlechat.utils.StringResources;

import java.util.HashMap;
import java.util.Map;

/**
 * NetworkRequest.java
 * Makes requests to the MySQL database
 * @author Jay Syko
 */
public class NetworkRequest {
    private static final String MYSQL_URL = "http://192.168.33.10/v1/";
    private static String TAG = NetworkRequest.class.getSimpleName();
    private NetworkCallback callback;

    public NetworkRequest(NetworkCallback callback) {
        this.callback = callback;
    }

    /**
     * Make a GET request with no parameters
     *
     * @param endpoint REST API Endpoint
     * @return a VolleyRequest to be added to RequestQueue
     */
    public Request get(RESTEndpoints endpoint) {
        return new StringRequest(
                Request.Method.POST,
                MYSQL_URL.concat(endpoint.getEndpoint()),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.getMessage());
                    }
                });
    }

    /**
     * Make a POST request with no parameters
     * @param endpoint REST API Endpoint
     * @return a VolleyRequest to be added to RequestQueue
     */
    public Request post(RESTEndpoints endpoint) {
        return new StringRequest(
                Request.Method.POST,
                MYSQL_URL.concat(endpoint.getEndpoint()),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.getMessage());
                    }
                });
    }

    /**
     * Make a POST request with parameters
     * @param endpoint REST API Endpoint
     * @param params POST data
     * @return a VolleyRequest to be added to RequestQueue
     */
    public Request post(RESTEndpoints endpoint, final HashMap<String, String> params) {
        return new StringRequest(
                Request.Method.POST,
                MYSQL_URL.concat(endpoint.getEndpoint()),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, StringResources.NULL_TEXT + error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };
    }
}
