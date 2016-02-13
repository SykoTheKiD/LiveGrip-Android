package com.jaysyko.wrestlechat.query;

import com.jaysyko.wrestlechat.cache.AppCache;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public final class Query<T extends ParseObject> {

    private ParseQuery query;

    public Query(Class model) {
        this.query = ParseQuery.getQuery(model);
    }

    public ParseQuery build() {
        return this.query;
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

    final public List execute() {
        return new AppCache(this).queryCache();
    }
}