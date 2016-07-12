package com.jaysyko.wrestlechat.eventManager;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.jaysyko.wrestlechat.models.Event;
import com.jaysyko.wrestlechat.receivers.EventStartReceiver;
import com.jaysyko.wrestlechat.sharedPreferences.PreferenceProvider;
import com.jaysyko.wrestlechat.sharedPreferences.Preferences;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * NotifyListStore.java
 * @author Jay Syko on 2016-07-11.
 */
public class NotifyListStore implements Serializable, NotifyStore {

    private static final String SAVED_EVENTS = "savedEvents";
    private LinkedList<SavedEvent> events = new LinkedList<>();
    private static NotifyListStore instance = new NotifyListStore();
    private final SavedEvent holder = new SavedEvent(null, null);
    private NotifyListStore() {

    }

    /**
     * Adds an event to the event list store
     * @param event Event
     * @param context Context
     */
    public void add(Event event, Context context){
        event.setNotify(true);
        events.add(new SavedEvent(event, PendingIntent.getBroadcast(
                context,
                event.getEventID(),
                new Intent(context, EventStartReceiver.class), 0)));
    }

    /**
     * Removes an event from the event list store
     * @param event Event
     * @param context Context
     */
    public void remove(Event event, Context context){
        event.setNotify(false);
        holder.event = event;
        holder.pendingIntent = PendingIntent.getBroadcast(context,
                event.getEventID(),
                new Intent(context, EventStartReceiver.class), 0);
        events.remove(holder);
        Alarm.cancelAlarm(holder.pendingIntent, context);
        holder.event = null;
    }

    /**
     * Checks if an event is in the store
     * @param event Event
     * @return boolean
     */
    public boolean contains(Event event){
        holder.event = event;
        boolean ret = events.contains(holder);
        holder.event = null;
        return ret;
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
        String json = gson.toJson(new ArrayList<>(events));
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
            SavedEvent[] eventsArray = gson.fromJson(json, SavedEvent[].class);
            events = new LinkedList<>(Arrays.asList(eventsArray));
        }else{
            events = new LinkedList<>();
        }
    }

    /**
     * @param newEvents List<Event>
     * @param context Context
     * @return List<Event>
     */
    public List<Event> clean(List<Event> newEvents, Context context){
        // TODO: Improve runtime
        for (Event event: newEvents){
            if(contains(event)){
                event.setNotify(true);
            }
        }

        for (SavedEvent savedEvent : events) {
            final Event event = savedEvent.event;
            if(!newEvents.contains(event)){
                remove(event, context);
            }
        }
        return newEvents;
    }

    /**
     * An object with the event and its pending intent for an alarm
     */
    private class SavedEvent {
        Event event;
        PendingIntent pendingIntent;

        SavedEvent(Event event, PendingIntent pendingIntent) {
            this.event = event;
            this.pendingIntent = pendingIntent;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            SavedEvent that = (SavedEvent) o;

            return event.equals(that.event);

        }

        @Override
        public int hashCode() {
            return event.hashCode();
        }
    }
}
