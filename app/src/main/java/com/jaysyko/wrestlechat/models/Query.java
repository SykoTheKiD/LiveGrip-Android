package com.jaysyko.wrestlechat.models;

import com.parse.ParseObject;
import com.parse.ParseQuery;

public class Query<T extends ParseObject> {

    private ParseQuery query;

    public Query(Class model) {
        this.query = ParseQuery.getQuery(model);
    }

    public ParseQuery getQuery() {
        return query;
    }

    final public Query whereEqualTo(String column, String toCompare) {
        this.query.whereEqualTo(column, toCompare);
        return this;
    }

    final public Query setLimit(int max) {
        this.query.setLimit(max);
        return this;
    }

    final public Query orderByASC(String orderKey) {
        this.query.orderByAscending(orderKey);
        return this;
    }

    final public Query orderByDESC(String orderKey) {
        this.query.orderByDescending(orderKey);
        return this;
    }
}