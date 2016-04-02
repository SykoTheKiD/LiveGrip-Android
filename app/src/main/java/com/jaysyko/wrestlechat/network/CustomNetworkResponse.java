package com.jaysyko.wrestlechat.network;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * CustomNetworkResponse.java
 *
 * Wrapper class that wraps up the JSON Response in a small API
 * @author Jay Syko
 */
public class CustomNetworkResponse {

    public static final String TAG = CustomNetworkResponse.class.getSimpleName();
    private JSONObject response;

    public CustomNetworkResponse(String response) {
        try {
            this.response = new JSONObject(response);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * Checks if the performed action was successful
     *
     * @return T/F
     */
    public boolean isSuccessful() {
        try {
            return (Boolean) this.response.get(NetworkResponseKeys.SUCCESS.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Gets the results of the performed action
     * @return JSON Array of the details
     */
    public JSONArray getPayload() {
        try {
            return (JSONArray) this.response.get(NetworkResponseKeys.PAYLOAD.toString());
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * Returns the payload as the return for toString()
     * @return response payload
     */
    @Override
    public String toString() {
        return getPayload().toString();
    }
}
