package com.jaysyko.wrestlechat.eventManager;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.jaysyko.wrestlechat.db.BackEnd;
import com.jaysyko.wrestlechat.models.Event;
import com.jaysyko.wrestlechat.utils.DBConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * RetrieveEvents.java
 * Get and hold a list of all the events
 *
 * @author Jay Syko
 */
public class RetrieveEvents {
    private static final Class<Event> EVENT_MODEL = Event.class;
    private static final String EVENTS_MODEL_SIMPLE_NAME = EVENT_MODEL.getSimpleName();
    private static RetrieveEvents retrieveEvents = new RetrieveEvents();
    private List<Event> eventsList = new ArrayList<>();
    private Context context;
    private StringRequest stringRequest = new StringRequest(
            Request.Method.POST,
            DBConstants.MYSQL_URL.concat("events.php"),
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        Log.e("R", response);
                        JSONObject jsonObject = new JSONObject(response);
                        boolean successful = jsonObject.getBoolean("success");
                        if (successful) {
                            JSONObject current;
                            JSONArray events = jsonObject.getJSONArray("payload");
                            for (int i = 0; i < events.length(); i++) {
                                current = (JSONObject) events.get(i);
                                eventsList.add(new Event(
                                        current.getString("id"),
                                        current.getString("name"),
                                        current.getString("info"),
                                        current.getString("match_card"),
                                        current.getString("image"),
                                        current.getString("location"),
                                        current.getString("start_time"),
                                        current.getString("end_time")
                                ));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("ERR", error.getMessage());
        }
    });

    private RetrieveEvents() {
    }

    public static RetrieveEvents getInstance(Context context) {
        retrieveEvents.context = context;
        return retrieveEvents;
    }

    public List<Event> getEventList() {
        return retrieveEvents.eventsList;
    }

    public synchronized void updateEventCards() {
        new BackEnd(context).execute(stringRequest);
    }
}
