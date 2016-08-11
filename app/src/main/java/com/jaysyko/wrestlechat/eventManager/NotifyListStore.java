package com.jaysyko.wrestlechat.eventManager;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jaysyko.wrestlechat.application.eLog;
import com.jaysyko.wrestlechat.models.Event;
import com.jaysyko.wrestlechat.receivers.EventStartReceiver;
import com.jaysyko.wrestlechat.sharedPreferences.PreferenceProvider;
import com.jaysyko.wrestlechat.sharedPreferences.Preferences;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * NotifyListStore.java
 * @author Jay Syko on 2016-07-11.
 */
public class NotifyListStore implements Serializable, NotifyStore {
    private static final String TAG = NotifyListStore.class.getSimpleName();
    private static final String SAVED_EVENTS = "savedEvents";
    private Set<Event> savedEventHashMap = new HashSet<>();
    private static NotifyListStore instance = new NotifyListStore();
    private NotifyListStore() {

    }

    /**
     * Adds an event to the event list store
     * @param event Event
     * @param context Context
     */
    public void add(Event event, Context context){
        event.setNotify(true);
        savedEventHashMap.add(event);
    }

    /**
     * Removes an event from the event list store
     * @param event Event
     * @param context Context
     */
    public void remove(Event event, Context context){
        event.setNotify(false);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                event.getEventID(),
                new Intent(context, EventStartReceiver.class), PendingIntent.FLAG_UPDATE_CURRENT);
        pendingIntent.cancel();
        savedEventHashMap.remove(event);
        Alarm.cancelAlarm(pendingIntent, context);
    }

    /**
     * Checks if an event is in the store
     * @param event Event
     * @return boolean
     */
    public boolean contains(Event event){
        return savedEventHashMap.contains(event);
    }

    /**
     * Returns an instance of the store
     * @return NotifyListStore
     */
    public static NotifyListStore getInstance(){
        return instance;
    }

    /**
     * Store the list store in SharedPreferences
     * @param context Context
     */
    public void storeList(Context context){
        SharedPreferences.Editor editor = PreferenceProvider.getEditor(context, Preferences.NOTIFY_EVENTS);
        Gson gson = new Gson();
        String json = gson.toJson(savedEventHashMap);
        editor.putString(SAVED_EVENTS, json);
        editor.apply();
    }

    /**
     * Restores the list store from SharedPreferences
     * @param context Context
     */
    public void restore(Context context){
        String json = PreferenceProvider.getSharedPreferences(context, Preferences.NOTIFY_EVENTS).getString(SAVED_EVENTS, null);
        Gson gson = new Gson();
        if(json != null){
            Type type = new TypeToken<Set<Event>>() {}.getType();
            savedEventHashMap = gson.fromJson(json, type);
        }else{
            savedEventHashMap = new HashSet<>();
        }
    }

    /**
     * @param newEvents List<Event>
     * @return List<Event>
     */
    public List<Event> clean(List<Event> newEvents){
        // TODO: Improve runtime
        for (Event event: newEvents){
            if(contains(event)){
                event.setNotify(true);
            }
        }
        eLog.i(TAG, "Successfully cleaned events");
        return newEvents;
    }
}
