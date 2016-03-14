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
     * @return List of results
     */
    public static QueryResult queryCache(Query query) {
        return queryDB(query);
    }

    /**
     * Delete a set from the cache
     */
    private static void deleteFromCache() {

    }

    /**
     * Query for results in the database
     * @return List of results
     */
    public static QueryResult queryDB(Query query) {
        return null;
    }

    public static void save() {

    }
}
