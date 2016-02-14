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

    public List queryCache(String label) {
        Query cacheQuery = this.query;
        cacheQuery.build().fromLocalDatastore();
        try {
            return cacheQuery.build().find();
        } catch (ParseException ignored) {
            return queryDB(label);
        }

    }

    public void deleteFromCache(List objs) {
        try {
            ParseObject.unpinAll(objs);
        } catch (ParseException ignored) {
        }
    }

    private List queryDB(String label) {
        try {
            List results = this.query.build().find();
            ParseObject.pinAll(label, results);
            return results;
        } catch (ParseException e) {
            return null;
        }
    }
}
