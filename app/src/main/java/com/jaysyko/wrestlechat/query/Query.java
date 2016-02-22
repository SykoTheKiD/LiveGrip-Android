package com.jaysyko.wrestlechat.query;

import com.jaysyko.wrestlechat.db.BackEnd;
import com.parse.ParseQuery;

import java.util.List;

public final class Query {

    private ParseQuery query;
    private String label;

    public Query(Class model) {
        this.query = ParseQuery.getQuery(model);
        this.label = model.getSimpleName();
    }

    /**
     * Returns an instance of current query
     *
     * @return query
     */
    public ParseQuery build() {
        return this.query;
    }

    /**
     * Checks if value toCompare is contained in the given coloumn
     * @param column String
     * @param toCompare String
     * @return query
     */
    final public Query whereEqualTo(String column, String toCompare) {
        this.query.whereEqualTo(column, toCompare);
        return this;
    }

    /**
     * Set maximum number of results to return
     * @param max Integer
     * @return query
     */

    final public Query setLimit(int max) {
        this.query.setLimit(max);
        return this;
    }

    /**
     * Order results by given parameter in ascending order
     * @param orderKey String
     * @return query
     */
    final public Query orderByASC(String orderKey) {
        this.query.orderByAscending(orderKey);
        return this;
    }

    /**
     * Order results by given parameter in descending order
     * @param orderKey String
     * @return query
     */
    final public Query orderByDESC(String orderKey) {
        this.query.orderByDescending(orderKey);
        return this;
    }

    /**
     * Run query against app cache
     * @return List
     */
    final public List execute() {
        return new BackEnd(this).queryCache(this.label);
    }

    /**
     * Run query against database
     * @return List
     */
    final public List executeHard() {
        return new BackEnd(this).queryDB(this.label);
    }
}