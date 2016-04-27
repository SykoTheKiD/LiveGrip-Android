package com.jaysyko.wrestlechat.auth;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.jaysyko.wrestlechat.localStorage.LocalStorage;
import com.jaysyko.wrestlechat.localStorage.StorageFile;

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

    JSONObject mPayload;
    CurrentActiveUser mUser;
    LocalStorage mLocalStorage;

    @SuppressLint("CommitPrefEdits")
    @Before
    public void setup() {
        initMocks(this);
        mLocalStorage = new LocalStorage(mContext, StorageFile.AUTH);
        when(mLocalStorage.getSharedPreferences()).thenReturn(mSharedPreferences);
        when(mSharedPreferences.edit()).thenReturn(mEditor);
    }

    @Before
    public void setUp() throws Exception {
        Assert.assertNotNull(mContext);
        try {
            mPayload = new JSONObject();
            mPayload.put(UserKeys.ID.toString(), "1");
            mPayload.put(UserKeys.USERNAME.toString(), "jaysyko");
            mPayload.put(UserKeys.PROFILE_IMAGE.toString(), "http://cdn.urbanislandz.com/wp-content/uploads/2015/09/The-Weeknd1.jpg");
            mUser = CurrentActiveUser.newUser(mContext, mPayload);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testNewUserGood() {
        Assert.assertEquals(mUser.getUsername(), "jaysyko");
        Assert.assertEquals(mUser.getUserID(), "1");
        Assert.assertEquals(mUser.getProfileImage(), "http://cdn.urbanislandz.com/wp-content/uploads/2015/09/The-Weeknd1.jpg");
    }

    @After
    public void cleanUp() {
        mEditor.clear();
    }

}
