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
            Log.d("CACHE", "HIT");
            return cacheQuery.build().find();
        } catch (ParseException e) {
            Log.d("CACHE", e.getMessage());
        }
        return queryDB(label);
    }

    public void deleteFromCache(List objs) {
        try {
            ParseObject.unpinAll(objs);
        } catch (ParseException ignored) {
        }
    }

    public void deleteFromCache(String label) {
        try {
            ParseObject.unpinAll(label);
        } catch (ParseException e) {
            Log.d("DELETE CACHE LABEL", e.getMessage());
        }
    }

    public List queryDB(String label) {
        try {
            Log.d("DB", "HIT");
            List results = this.query.build().find();
            ParseObject.unpinAll(label);
            ParseObject.pinAll(label, results);
            return results;
        } catch (ParseException e) {
            Log.d("DB", "FAIL");
            return null;
        }
    }
}
