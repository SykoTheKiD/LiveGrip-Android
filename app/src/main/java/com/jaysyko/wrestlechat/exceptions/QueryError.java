package com.jaysyko.wrestlechat.exceptions;

public class QueryError extends Exception {
    public QueryError(String friendlyMsg) {
        super(friendlyMsg);
    }
}
