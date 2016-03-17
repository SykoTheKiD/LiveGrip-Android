//package com.jaysyko.wrestlechat.db;
//
//import com.parse.ParseQuery;
//
//public final class Query {
//
//    private ParseQuery query;
//
//    public Query(Class model) {
//        this.query = ParseQuery.getQuery(model);
//    }
//
//    /**
//     * Returns an instance of current query
//     *
//     * @return query
//     */
//    public ParseQuery build() {
//        return this.query;
//    }
//
//    /**
//     * Checks if value toCompare is contained in the given column
//     * @param column String
//     * @param toCompare String
//     */
//    final public void whereEqualTo(String column, String toCompare) {
//        this.query.whereEqualTo(column, toCompare);
//    }
//
//    /**
//     * Set maximum number of results to return
//     * @param max Integer
//     */
//
//    final public void setLimit(int max) {
//        this.query.setLimit(max);
//    }
//
//    /**
//     * Order results by given parameter in ascending order
//     * @param orderKey String
//     */
//    final public void orderByASC(String orderKey) {
//        this.query.orderByAscending(orderKey);
//    }
//
//    /**
//     * Order results by given parameter in descending order
//     * @param orderKey String
//     */
//    final public void orderByDESC(String orderKey) {
//        this.query.orderByDescending(orderKey);
//    }
//}