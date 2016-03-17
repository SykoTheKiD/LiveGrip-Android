package com.jaysyko.wrestlechat.db;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jarushaan on 2016-03-12
 */
public class QueryResult {

    private JSONObject response;

    public QueryResult(String response) {
        try {
            this.response = new JSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean isSuccessful() {
        try {
            return (Boolean) this.response.get("status");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public JSONObject getPayload() {
        try {
            return (JSONObject) this.response.get("payload");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        return getPayload().toString();
    }
}
