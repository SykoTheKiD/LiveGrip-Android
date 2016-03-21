package com.jaysyko.wrestlechat.network;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jarushaan on 2016-03-12
 */
public class NetworkResponse {

    public static final String TAG = NetworkResponse.class.getSimpleName();
    private JSONObject response;

    public NetworkResponse(String response) {
        try {
            this.response = new JSONObject(response);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public boolean isSuccessful() {
        try {
            return (Boolean) this.response.get(NetworkResponseKeys.SUCCESS.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public JSONArray getPayload() {
        try {
            return (JSONArray) this.response.get(NetworkResponseKeys.PAYLOAD.toString());
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    @Override
    public String toString() {
        return getPayload().toString();
    }
}
