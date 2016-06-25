package com.jaysyko.wrestlechat.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jarushaan on 2016-06-04
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    public static final String ID_COLUMN = "id";
    public static final String EVENTS_TABLE = "events";
    public static final String EVENT_ID_COLUMN = "event_id";
    public static final String EVENT_NAME_COLUMN = "name";
    public static final String EVENT_LOCATION_COLUMN = "location";
    public static final String EVENT_START_TIME_COLUMN = "start_time";
    public static final String EVENT_END_TIME_COLUMN = "end_time";
    public static final String EVENT_INFO_COLUMN = "event_info";
    public static final String EVENT_MATCH_CARD_COLUMN = "match_card";
    public static final String EVENT_IMAGE_COLUMN = "event_image";
    public static final String CREATE_EVENTS_TABLE = "CREATE TABLE "
            + EVENTS_TABLE
            + "("
            + ID_COLUMN + " INTEGER PRIMARY KEY,  "
            + EVENT_ID_COLUMN + " INTEGER, "
            + EVENT_NAME_COLUMN + " TEXT, "
            + EVENT_LOCATION_COLUMN + " TEXT, "
            + EVENT_START_TIME_COLUMN + " TIMESTAMP, "
            + EVENT_END_TIME_COLUMN + " TIMESTAMP, "
            + EVENT_INFO_COLUMN + " TEXT, "
            + EVENT_MATCH_CARD_COLUMN + " TEXT, "
            + EVENT_IMAGE_COLUMN + " TEXT"
            +")";
    private static final String DATABASE_NAME = "livegrip";
    private static final int DATABASE_VERSION = 1;
    private static SQLiteHelper instance;

    private SQLiteHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized SQLiteHelper getHelper(Context context){
        if(instance == null){
            instance = new SQLiteHelper(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_EVENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + EVENTS_TABLE);
        onCreate(db);
    }
}
