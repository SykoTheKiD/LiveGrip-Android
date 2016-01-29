package com.jaysyko.wrestlechat.models;

import com.parse.ParseObject;
import com.parse.ParseQuery;

public class Query<T extends ParseObject> {

    private Class model;
    private ParseQuery<T> query;

    public Query(Class model) {
        this.model = model;
        this.query = ParseQuery.getQuery(this.model);
    }

    public ParseQuery<T> getQuery() {
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

//        ParseQuery<Message> query = ParseQuery.getQuery(Message.class);
//        // Configure limit and sort order
//        query.whereEqualTo(Events.EVENT_ID_KEY, sEventId);
//        query.setLimit(MAX_CHAT_MESSAGES_TO_SHOW);
//        query.orderByAscending(Message.CREATED_AT_KEY);
//        // Execute query to fetch all messages from Parse asynchronously
//        // This is equivalent to a SELECT query with SQL
//        query.findInBackground(new FindCallback<Message>() {
//            public void done(List<Message> messages, ParseException e) {
//                if (e == null) {
//                    MessagingActivity.this.messages.clear();
//                    MessagingActivity.this.messages.addAll(messages);
//                    mAdapter.notifyDataSetChanged(); // update adapter
//                    // Scroll to the bottom of the list on initial load
//                    if (mFirstLoad) {
//                        lvChat.setSelection(mAdapter.getCount() - 1);
//                        mFirstLoad = false;
//                    }
//                } else {
//                    Log.d(LOG_KEY, ERROR_KEY + e.getMessage());
//                }
//            }
//        });