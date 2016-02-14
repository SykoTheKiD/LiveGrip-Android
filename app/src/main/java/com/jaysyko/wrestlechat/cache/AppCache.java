package com.jaysyko.wrestlechat.cache;

import android.util.Log;

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
            List results = cacheQuery.build().find();
            Log.d("CACHE HIT", "HIT");
            return results;
        } catch (ParseException e) {
            Log.d("CACHE MISS", e.getMessage());
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
