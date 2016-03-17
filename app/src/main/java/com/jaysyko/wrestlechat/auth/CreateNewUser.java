package com.jaysyko.wrestlechat.auth;

import android.content.Context;

import com.jaysyko.wrestlechat.db.BackEnd;
import com.jaysyko.wrestlechat.db.QueryResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * CreateNewUser.java
 * Creates and stores a new user into the system
 *
 * @author Jay Syko
 */
public class CreateNewUser {
    /**
     * SignUp the user into the app returns true if successful else returns false
     *
     * @param username String
     * @param password String
     * @return boolean
     */
    public static boolean signUpUser(Context context, String username, String password) {
        HashMap<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        QueryResult result = new BackEnd(context).queryDB("/newuser.php", params);
        boolean resultSuccessful = result.isSuccessful();
        if (resultSuccessful) {
            JSONObject payload = result.getPayload();
            try {
                CurrentActiveUser currentUser = CurrentActiveUser.getInstance(username, password);
                currentUser.setProfileImageURL(payload.getString("profile_image"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return resultSuccessful;
    }
}
