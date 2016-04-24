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
    private static final String SUCCESS = "success";
    private static final String FAIL = "fail";
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
            String status = (String) this.response.get(NetworkResponseKeys.STATUS.toString());
            switch (status) {
                case SUCCESS:
                    return true;
                case FAIL:
                    return false;
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return false;
    }

    /**
     * Gets the results of the performed action
     * @return JSON Array of the details
     */
    public JSONArray getPayload() {
        try {
            return (JSONArray) this.response.get(NetworkResponseKeys.DATA.toString());
        } catch (JSONException | ClassCastException e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    public String getMessage() {
        try {
            return (String) this.response.get(NetworkResponseKeys.MESSAGE.toString());
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
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
