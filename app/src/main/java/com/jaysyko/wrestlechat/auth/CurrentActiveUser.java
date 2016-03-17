package com.jaysyko.wrestlechat.auth;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.jaysyko.wrestlechat.db.BackEnd;
import com.jaysyko.wrestlechat.utils.DBConstants;
import com.jaysyko.wrestlechat.utils.ImageTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * CurrentActiveUser.java
 * A single instance for the currently logged in user
 *
 * @author Jay Syko
 */
public class CurrentActiveUser {
    private static CurrentActiveUser activeCurrentActiveUser;
    private String username;
    private String password;
    private String profileImageURL;

    private CurrentActiveUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Returns a new instance of a current logged in user
     *
     * @param username String
     * @param password String
     * @return CurrentActiveUser
     */
    public static CurrentActiveUser getInstance(String username, String password) {
        if (activeCurrentActiveUser == null) {
            activeCurrentActiveUser = new CurrentActiveUser(username, password);
        }
        return activeCurrentActiveUser;
    }

    /**
     * Returns the current instance of the logged in user
     *
     * @return CurrentActiveUser
     */
    public static CurrentActiveUser getInstance() {
//        if (currentUser != null) {
//            activeCurrentActiveUser = new CurrentActiveUser(currentUser.getUsername(), null);
//        }
        return activeCurrentActiveUser;
    }

    public boolean setPassword(String password) {
//        currentUser.setPassword(password);
        return true;
    }

    /**
     * Get the user defined imageUrl else return an generic image
     *
     * @return imageUrl: String
     */
    public String getCustomProfileImageURL() {
//        String userImage = currentUser.getString(User.IMG_ID);
//        if (userImage != null) {
//            activeCurrentActiveUser.profileImageURL = userImage;
//        } else {
//            activeCurrentActiveUser.profileImageURL = ImageTools.defaultProfileImage(activeCurrentActiveUser.getUsername());
//        }
        return activeCurrentActiveUser.profileImageURL;
    }

    /**
     * Return the current user's username
     *
     * @return username: String
     */
    public String getUsername() {
        return username;
    }

    public boolean setUsername(String username) {
        // hit /users/edit
//        currentUser.setUsername(username);
        return true;
    }

    /**
     * Login the user; returns true if successful else returns false
     *
     * @return boolean
     */
    public boolean loginUser(Context context, final String username, final String password) {
        final Boolean[] ret = new Boolean[1];
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                DBConstants.MYSQL_URL.concat("login.php"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            ret[0] = jsonObject.getBoolean("success");
                            Log.e("E", jsonObject.toString());
                        } catch (JSONException e) {
                            ret[0] = false;
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ret[0] = false;
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };

        new BackEnd(context).execute(stringRequest);
        return ret[0];
    }

    /**
     * Logs out the current user
     */
    public void logout() {
        activeCurrentActiveUser = null;
        // delete local session
    }

    /**
     * Set Custom User Image
     *
     * @param url String
     * @return String
     */
    public boolean setProfileImageURL(final String url) {
        Boolean isLinkToImage = ImageTools.isLinkToImage(url);
        if (isLinkToImage) {
            activeCurrentActiveUser.profileImageURL = url;
            return true;
        } else {
            return false;
        }
    }

    public String getUserID() {
        return null;
    }
}
