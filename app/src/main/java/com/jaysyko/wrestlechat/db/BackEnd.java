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

    /**
     * Query the app's cache
     * @param label String
     * @return List of results
     */
    public static List queryCache(Query query, String label) {
        query.build().fromLocalDatastore();
        try {
            return query.build().find();
        } catch (ParseException e) {
            Log.d("CACHE", e.getMessage());
        }
        return queryDB(query, label);
    }

    /**
     * Delete a set from the cache
     * @param label String
     */
    private static void deleteFromCache(String label) {
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
    public static List queryDB(Query query, String label) {
        try {
            List results = query.build().find();
            deleteFromCache(label);
            ParseObject.pinAll(label, results);
            return results;
        } catch (ParseException e) {
            Log.d("DB", "FAIL");
            return null;
        }
    }
}
