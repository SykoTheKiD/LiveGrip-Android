package com.jaysyko.wrestlechat.db;

import android.util.Log;

import com.jaysyko.wrestlechat.query.Query;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.List;

/**
 * BackEnd.java
 * Contains the code for the backend of the app
 *
 * @author Jay Syko
 */
public class BackEnd {

    private Query query;

    public BackEnd(Query query) {
        this.query = query;
    }

    /**
     * Query the app's cache
     * @param label String
     * @return List of results
     */
    public List queryCache(String label) {
        Query cacheQuery = this.query;
        cacheQuery.build().fromLocalDatastore();
        try {
            Log.d("CACHE", "HIT");
            return cacheQuery.build().find();
        } catch (ParseException e) {
            Log.d("CACHE", e.getMessage());
        }
        return queryDB(label);
    }

    /**
     * Delete a set from the cache
     * @param label String
     */
    private void deleteFromCache(String label) {
        try {
            ParseObject.unpinAll(label);
        } catch (ParseException e) {
            Log.d("DELETE CACHE LABEL", e.getMessage());
        }
    }

    /**
     * Query for results in the database
     * @param label String
     * @return List of results
     */
    public List queryDB(String label) {
        try {
            Log.d("DB", "HIT");
            List results = this.query.build().find();
            deleteFromCache(label);
            ParseObject.pinAll(label, results);
            return results;
        } catch (ParseException e) {
            Log.d("DB", "FAIL");
            return null;
        }
    }
}
