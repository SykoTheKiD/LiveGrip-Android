package com.jaysyko.wrestlechat.sqlite.DBDAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.jaysyko.wrestlechat.models.User;
import com.jaysyko.wrestlechat.sqlite.SQLiteHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jarushaan on 2016-06-04
 */
public class UserDBDAO {
    private SQLiteHelper sqLiteHelper;
    private SQLiteDatabase database;

    private String[] allColumns =
            {
                    SQLiteHelper.ID_COLUMN,
                    SQLiteHelper.USERNAME_COLUMN,
                    SQLiteHelper.PROFILE_IMAGE
            };

    public UserDBDAO(Context context){
        sqLiteHelper = SQLiteHelper.getHelper(context);
    }

    public void open() throws SQLException {
        database = sqLiteHelper.getWritableDatabase();
    }

    public void close(){
        sqLiteHelper.close();
    }

    public User createUser(User user){
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.USERNAME_COLUMN, user.getUsername());
        values.put(SQLiteHelper.PROFILE_IMAGE, user.getProfileImage());
        Cursor cursor = database.query(SQLiteHelper.USER_TABLE,
                allColumns, SQLiteHelper.ID_COLUMN + " = " + user.getId(), null,
                null, null, null);
        cursor.moveToFirst();
        User newUser = cursorToUser(cursor);
        cursor.close();
        return newUser;
    }

    public void deleteUser(User user){
        database.delete(SQLiteHelper.USER_TABLE, SQLiteHelper.ID_COLUMN + "=" + user.getId(), null);
    }

    public List<User> getAllUser() {
        List<User> users = new ArrayList<>();

        Cursor cursor = database.query(SQLiteHelper.USER_TABLE,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            User user = cursorToUser(cursor);
            users.add(user);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return users;
    }

    public User getUser(int id){
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        Cursor cursor = db.query(SQLiteHelper.USER_TABLE,
                new String[]{SQLiteHelper.ID_COLUMN, SQLiteHelper.USERNAME_COLUMN, SQLiteHelper.PROFILE_IMAGE},
                SQLiteHelper.ID_COLUMN + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if(cursor != null){
            cursor.moveToFirst();
            User user = new User();
            user.setId(cursor.getInt(0));
            user.setUsername(cursor.getString(1));
            user.setProfile_image(cursor.getString(2));
            cursor.close();
            return user;
        }
        return null;
    }


    private User cursorToUser(Cursor cursor){
        User user = new User();
        user.setId(cursor.getInt(0));
        user.setUsername(cursor.getString(1));
        user.setProfile_image(cursor.getString(2));
        return user;
    }

}
