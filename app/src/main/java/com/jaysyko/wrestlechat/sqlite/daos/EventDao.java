package com.jaysyko.wrestlechat.sqlite.daos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.jaysyko.wrestlechat.models.Event;
import com.jaysyko.wrestlechat.sqlite.SQLiteHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jarushaan on 2016-06-05
 */
public class EventDao {

    private SQLiteHelper sqLiteHelper;
    private SQLiteDatabase database;

    private String[] allColumns =
            {
                    SQLiteHelper.ID_COLUMN,
                    SQLiteHelper.EVENT_ID_COLUMN,
                    SQLiteHelper.EVENT_NAME_COLUMN,
                    SQLiteHelper.EVENT_LOCATION_COLUMN,
                    SQLiteHelper.EVENT_IMAGE_COLUMN,
                    SQLiteHelper.EVENT_INFO_COLUMN,
                    SQLiteHelper.EVENT_MATCH_CARD_COLUMN,
                    SQLiteHelper.EVENT_START_TIME_COLUMN,
                    SQLiteHelper.EVENT_END_TIME_COLUMN,
            };

    public EventDao(Context context){
        sqLiteHelper = SQLiteHelper.getHelper(context);
    }

    public void open() throws SQLException {
        database = sqLiteHelper.getWritableDatabase();
    }

    public void close(){
        sqLiteHelper.close();
    }

    public void createEvent(Event event){
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.EVENT_ID_COLUMN, event.getEventID());
        values.put(SQLiteHelper.EVENT_NAME_COLUMN, event.getEventName());
        values.put(SQLiteHelper.EVENT_LOCATION_COLUMN, event.getEventLocation());
        values.put(SQLiteHelper.EVENT_IMAGE_COLUMN, event.getEventImage());
        values.put(SQLiteHelper.EVENT_INFO_COLUMN, event.getEventInfo());
        values.put(SQLiteHelper.EVENT_MATCH_CARD_COLUMN, event.getMatchCard());
        values.put(SQLiteHelper.EVENT_START_TIME_COLUMN, event.getEventStartTime());
        values.put(SQLiteHelper.EVENT_END_TIME_COLUMN, event.getEventEndTime());
        database.insert(SQLiteHelper.EVENTS_TABLE, null, values);
    }

    public void deleteEvent(Event event){
        database.delete(SQLiteHelper.EVENTS_TABLE, SQLiteHelper.ID_COLUMN + SQLOperators.EQUALS + event.getEventID(), null);
    }

    public List<Event> getAllEvents() {
        List<Event> events = new ArrayList<>();

        Cursor cursor = database.query(SQLiteHelper.EVENTS_TABLE,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Event event = cursorToEvent(cursor);
            events.add(event);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return events;
    }

    public void addAll(List<Event> events){
        for(Event event: events){
            createEvent(event);
        }
    }

    private Event cursorToEvent(Cursor cursor){
        return new Event(
                cursor.getInt(cursor.getColumnIndex(SQLiteHelper.EVENT_ID_COLUMN)),
                cursor.getString(cursor.getColumnIndex(SQLiteHelper.EVENT_NAME_COLUMN)),
                cursor.getString(cursor.getColumnIndex(SQLiteHelper.EVENT_INFO_COLUMN)),
                cursor.getString(cursor.getColumnIndex(SQLiteHelper.EVENT_IMAGE_COLUMN)),
                cursor.getString(cursor.getColumnIndex(SQLiteHelper.EVENT_LOCATION_COLUMN)),
                cursor.getString(cursor.getColumnIndex(SQLiteHelper.EVENT_MATCH_CARD_COLUMN)),
                cursor.getString(cursor.getColumnIndex(SQLiteHelper.EVENT_START_TIME_COLUMN)),
                cursor.getString(cursor.getColumnIndex(SQLiteHelper.EVENT_END_TIME_COLUMN))
        );
    }
}
