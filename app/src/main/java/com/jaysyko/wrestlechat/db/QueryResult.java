package com.jaysyko.wrestlechat.db;

import java.util.List;

/**
 * Created by jarushaan on 2016-03-12
 */
public class QueryResult {

    private List results;

    public QueryResult(List results) {
        this.results = results;
    }

    public List getResults() {
        return this.results;
    }
}
