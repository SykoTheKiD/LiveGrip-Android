package com.jaysyko.wrestlechat.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * NetworkRequest.java
 * Makes requests to the MySQL database
 * @author Jay Syko
 */
public class NetworkRequest {
    private static final String MYSQL_URL = URLS.getServerURL();
    private static String TAG = NetworkRequest.class.getSimpleName();
    private NetworkCallback callback;

    public NetworkRequest(NetworkCallback callback) {
        this.callback = callback;
    }


    public static Retrofit getClient() {
        return new Retrofit.Builder()
                .baseUrl("")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    /**
     * Make a GET request with no parameters
     *
     * @param endpoint REST API Endpoint
     * @return a VolleyRequest to be added to RequestQueue
     */
//    public Request get(RESTEndpoints endpoint) {
//        return new StringRequest(
//                Request.Method.GET,
//                MYSQL_URL.concat(endpoint.getEndpoint()),
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        callback.onSuccess(response);
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e(TAG, error.getMessage());
//                    }
//                });
//    }
//
//    /**
//     * Make a POST request with no parameters
//     * @param endpoint REST API Endpoint
//     * @return a VolleyRequest to be added to RequestQueue
//     */
//    public Request post(RESTEndpoints endpoint) {
//        return new StringRequest(
//                Request.Method.POST,
//                MYSQL_URL.concat(endpoint.getEndpoint()),
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        callback.onSuccess(response);
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e(TAG, "" + error.getMessage());
//                    }
//                });
//    }
//
//    /**
//     * Make a POST request with parameters
//     * @param endpoint REST API Endpoint
//     * @param params POST data
//     * @return a VolleyRequest to be added to RequestQueue
//     */
//    public Request post(RESTEndpoints endpoint, final HashMap<String, String> params) {
//        StringRequest strReq = new StringRequest(
//                Request.Method.POST,
//                MYSQL_URL.concat(endpoint.getEndpoint()),
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        callback.onSuccess(response);
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        callback.onFail(error.toString());
//                        Log.e(TAG, StringResources.NULL_TEXT + error.getMessage());
//                    }
//                }) {
//            @Override
//            protected Map<String, String> getParams() {
//                return params;
//            }
//        };
//        return strReq;
//    }
}
