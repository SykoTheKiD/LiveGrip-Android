package com.jaysyko.wrestlechat.db;

import org.json.JSONObject;

/**
 * Created by jarushaan on 2016-03-12
 */
public class QueryResult {

    private JSONObject response;

    public QueryResult(JSONObject response) {
        this.response = response;
    }

    public JSONObject getResponse() {
        return this.response;
    }
}
