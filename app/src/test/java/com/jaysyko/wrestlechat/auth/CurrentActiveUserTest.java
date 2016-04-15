package com.jaysyko.wrestlechat.auth;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.jaysyko.wrestlechat.localStorage.LocalStorage;
import com.jaysyko.wrestlechat.localStorage.StorageFile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by jarushaan on 2016-04-05
 */
public class CurrentActiveUserTest {

    @Mock
    Context mContext;

    @Mock
    SharedPreferences mSharedPreferences;

    @Mock
    SharedPreferences.Editor mEditor;

    JSONObject payload;
    JSONArray payloadArray = new JSONArray();
    CurrentActiveUser user;
    LocalStorage localStorage;

    @SuppressLint("CommitPrefEdits")
    @Before
    public void setup() {
        initMocks(this);
        localStorage = new LocalStorage(mContext, StorageFile.AUTH);
        when(localStorage.getSharedPreferences()).thenReturn(mSharedPreferences);
        when(mSharedPreferences.edit()).thenReturn(mEditor);
    }

    @Before
    public void setUp() throws Exception {
        Assert.assertNotNull(mContext);
        try {
            payload = new JSONObject();
            payload.put(UserKeys.ID.toString(), "1");
            payload.put(UserKeys.USERNAME.toString(), "jaysyko");
            payload.put(UserKeys.PROFILE_IMAGE.toString(), "http://cdn.urbanislandz.com/wp-content/uploads/2015/09/The-Weeknd1.jpg");
            payloadArray.put(payload);
            user = CurrentActiveUser.newUser(mContext, payloadArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testNewUserGood() {
        Assert.assertEquals(user.getUsername(), "jaysyko");
        Assert.assertEquals(user.getUserID(), "1");
        Assert.assertEquals(user.getProfileImage(), "http://cdn.urbanislandz.com/wp-content/uploads/2015/09/The-Weeknd1.jpg");
    }

    @After
    public void cleanUp() {
        mEditor.clear();
    }

}
