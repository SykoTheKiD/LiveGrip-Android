package com.jaysyko.wrestlechat.db;

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
    public static QueryResult queryCache(Query query, String label) {
        return queryDB(query, label);
    }

    /**
     * Delete a set from the cache
     * @param label String
     */
    private static void deleteFromCache(String label) {

    }

    /**
     * Query for results in the database
     * @param label String
     * @return List of results
     */
    public static QueryResult queryDB(Query query, String label) {
        return null;
    }
}
