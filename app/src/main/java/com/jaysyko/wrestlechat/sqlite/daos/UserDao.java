package com.jaysyko.wrestlechat.sqlite.daos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.jaysyko.wrestlechat.models.User;
import com.jaysyko.wrestlechat.sqlite.SQLiteHelper;

/**
 * Created by jarushaan on 2016-06-04
 */
public class UserDao {
    private SQLiteHelper sqLiteHelper;
    private SQLiteDatabase database;

    private String[] allColumns =
            {
                    SQLiteHelper.ID_COLUMN,
                    SQLiteHelper.USER_ID_COLUMN,
                    SQLiteHelper.USER_USERNAME_COLUMN,
                    SQLiteHelper.USER_PROFILE_IMAGE_COLUMN
            };

    public UserDao(Context context){
        sqLiteHelper = SQLiteHelper.getHelper(context);
    }

    public void open() throws SQLException {
        database = sqLiteHelper.getWritableDatabase();
    }

    public void close(){
        sqLiteHelper.close();
    }

    public void createUser(User user){
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.USER_ID_COLUMN, user.getId());
        values.put(SQLiteHelper.USER_USERNAME_COLUMN, user.getUsername());
        values.put(SQLiteHelper.USER_PROFILE_IMAGE_COLUMN, user.getProfileImage());
        values.put(SQLiteHelper.USER_AUTH_TOKEN_COLUMN, user.getAuthToken());
        database.insert(SQLiteHelper.USER_TABLE, null, values);
    }

    public void deleteUser(User user){
        database.delete(SQLiteHelper.USER_TABLE, SQLiteHelper.USER_ID_COLUMN + Operators.EQUALS + user.getId(), null);
    }

    public User getUser(int id){
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        Cursor cursor = db.query(SQLiteHelper.USER_TABLE,
                allColumns,
                SQLiteHelper.USER_ID_COLUMN + Operators.EQUALS_IF,
                new String[]{String.valueOf(id)},
                null, null, null, null);
        if(cursor != null && cursor.moveToFirst()){
            return cursorToUser(cursor);
        }
        return null;
    }

    private User cursorToUser(Cursor cursor){
        User user = new User();
        user.setId(cursor.getInt(cursor.getColumnIndex(SQLiteHelper.USER_ID_COLUMN)));
        user.setUsername(cursor.getString(cursor.getColumnIndex(SQLiteHelper.USER_USERNAME_COLUMN)));
        user.setProfile_image(cursor.getString(cursor.getColumnIndex(SQLiteHelper.USER_PROFILE_IMAGE_COLUMN)));
        user.setAuthToken(cursor.getString(cursor.getColumnIndex(SQLiteHelper.USER_AUTH_TOKEN_COLUMN)));
        return user;
    }

}
