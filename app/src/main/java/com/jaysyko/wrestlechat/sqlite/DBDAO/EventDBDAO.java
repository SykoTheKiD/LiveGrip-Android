package com.jaysyko.wrestlechat.sqlite.DBDAO;

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
public class EventDBDAO {

    private SQLiteHelper sqLiteHelper;
    private SQLiteDatabase database;

    private String[] allColumns =
            {
                    SQLiteHelper.ID_COLUMN,
                    SQLiteHelper.EVENT_NAME_COLUMN,
                    SQLiteHelper.EVENT_LOCATION_COLUMN,
                    SQLiteHelper.EVENT_IMAGE_COLUMN,
                    SQLiteHelper.EVENT_INFO_COLUMN,
                    SQLiteHelper.EVENT_MATCH_CARD_COLUMN,
                    SQLiteHelper.EVENT_START_TIME_COLUMN,
                    SQLiteHelper.EVENT_END_TIME_COLUMN,
            };

    public EventDBDAO(Context context){
        sqLiteHelper = SQLiteHelper.getHelper(context);
    }

    public void open() throws SQLException {
        database = sqLiteHelper.getWritableDatabase();
    }

    public void close(){
        sqLiteHelper.close();
    }

    public Event createUser(Event event){
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.EVENT_NAME_COLUMN, event.getEventName());
        values.put(SQLiteHelper.EVENT_LOCATION_COLUMN, event.getEventLocation());
        values.put(SQLiteHelper.EVENT_IMAGE_COLUMN, event.getEventImage());
        values.put(SQLiteHelper.EVENT_INFO_COLUMN, event.getEventInfo());
        values.put(SQLiteHelper.EVENT_MATCH_CARD_COLUMN, event.getMatchCard());
        values.put(SQLiteHelper.EVENT_START_TIME_COLUMN, event.getEventStartTime());
        values.put(SQLiteHelper.EVENT_END_TIME_COLUMN, event.getEventEndTime());
        Cursor cursor = database.query(SQLiteHelper.EVENTS_TABLE,
                allColumns, SQLiteHelper.ID_COLUMN + " = " + event.getEventID(), null,
                null, null, null);
        cursor.moveToFirst();
        Event newEvent = cursorToEvent(cursor);
        cursor.close();
        return newEvent;
    }

    public void deleteUser(Event event){
        database.delete(SQLiteHelper.EVENTS_TABLE, SQLiteHelper.ID_COLUMN + "=" + event.getEventID(), null);
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


    private Event cursorToEvent(Cursor cursor){

        return new Event(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6),
                cursor.getString(7));
    }
}
