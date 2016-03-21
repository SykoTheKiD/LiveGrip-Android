package com.jaysyko.wrestlechat.network;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jarushaan on 2016-03-18
 */
public class NetworkRequest {

    private static final String MYSQL_URL = "http://192.168.33.10/";
    private static String TAG = NetworkRequest.class.getSimpleName();
    private NetworkCallback callback;

    public NetworkRequest(NetworkCallback callback) {
        this.callback = callback;
    }

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
                }) {
            @Override
            protected Map<String, String> getParams() {
                return new HashMap<>();
            }
        };
    }

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
                        Log.e(TAG, error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };
    }
}
