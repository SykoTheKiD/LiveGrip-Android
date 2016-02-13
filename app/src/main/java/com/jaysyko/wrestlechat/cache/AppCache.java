package com.jaysyko.wrestlechat.cache;

import com.jaysyko.wrestlechat.query.Query;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.List;

public class AppCache {

    private Query query;

    public AppCache(Query query) {
        this.query = query;
    }

    public List queryCache() {
        Query cacheQuery = this.query;
        cacheQuery.build().fromLocalDatastore();
        try {
            return cacheQuery.build().find();
        } catch (ParseException e) {
            return queryDB();
        }
    }

    public void deleteFromCache(List objs) {
        try {
            ParseObject.unpinAll(objs);
        } catch (ParseException ignored) {
        }
    }

    private List queryDB() {
        try {
            List results = this.query.build().find();
            ParseObject.pinAll(results);
            return results;
        } catch (ParseException e) {
            return null;
        }
    }
}
