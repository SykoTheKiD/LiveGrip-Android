package com.jaysyko.wrestlechat.auth;

import android.content.Context;
import android.content.SharedPreferences;

import com.jaysyko.wrestlechat.models.User;
import com.jaysyko.wrestlechat.sessionManager.SessionManager;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

/**
 * Created by jarushaan on 2016-04-05
 */
public class SessionManagerTest {

    @Mock
    Context mContext;

    @Mock
    SharedPreferences mSharedPreferences;

    @Mock
    SharedPreferences.Editor mEditor;

    User user;

    @Before
    public void setUp(){
        user = new User();
        user.setId(1);
        user.setAuthToken("test_auth_token");
        user.setUsername("jaysyko");
        user.setProfileImage("profile_image.jpg");
    }

    @Test
    public void userSessionCreationTest(){
        mContext = Mockito.mock(Context.class);
        Assert.assertNotNull(mContext);
        SessionManager.newSession(mContext, user);
        Assert.assertEquals(true, SessionManager.isLoggedIn(mContext));
    }

//    @Mock
//    Context mContext;
//
//    @Mock
//    SharedPreferences mSharedPreferences;
//
//    @Mock
//    SharedPreferences.Editor mEditor;
//
//    JSONObject mPayload;
//    CurrentActiveUser mUser;
//    LocalStorage mLocalStorage;
//
//    @SuppressLint("CommitPrefEdits")
//    @Before
//    public void setup() {
//        initMocks(this);
//        mLocalStorage = new LocalStorage(mContext, StorageFile.AUTH);
//        when(mLocalStorage.getSharedPreferences()).thenReturn(mSharedPreferences);
//        when(mSharedPreferences.edit()).thenReturn(mEditor);
//    }
//
//    @Before
//    public void setUp() throws Exception {
//        Assert.assertNotNull(mContext);
//        try {
//            mPayload = new JSONObject();
//            mPayload.put(UserKeys.ID.toString(), "1");
//            mPayload.put(UserKeys.USERNAME.toString(), "jaysyko");
//            mPayload.put(UserKeys.USER_PROFILE_IMAGE_COLUMN.toString(), "http://cdn.urbanislandz.com/wp-content/uploads/2015/09/The-Weeknd1.jpg");
//            mUser = CurrentActiveUser.newInstance().newUser(mContext, mPayload);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    @Test
//    public void testNewUserGood() {
//        Assert.assertEquals(mUser.getUsername(), "jaysyko");
//        Assert.assertEquals(mUser.getUserID(), "1");
//        Assert.assertEquals(mUser.getProfileImage(), "http://cdn.urbanislandz.com/wp-content/uploads/2015/09/The-Weeknd1.jpg");
//    }
//
//    @After
//    public void cleanUp() {
//        mEditor.clear();
//    }

}
