package com.jaysyko.wrestlechat.auth;

import android.content.Context;
import android.content.SharedPreferences;

import com.jaysyko.wrestlechat.models.User;
import com.jaysyko.wrestlechat.network.APIInterface;
import com.jaysyko.wrestlechat.network.ApiManager;
import com.jaysyko.wrestlechat.network.NetworkCallback;
import com.jaysyko.wrestlechat.network.requestData.AuthData;
import com.jaysyko.wrestlechat.network.responses.UserResponse;
import com.jaysyko.wrestlechat.sessionManager.SessionManager;
import com.jaysyko.wrestlechat.sharedPreferences.PreferenceProvider;
import com.jaysyko.wrestlechat.sharedPreferences.Preferences;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import retrofit2.Call;


/**
 * @author Jay Syko
 */
public class SessionManagerTest {

    @Mock Context mContext;
    @Mock PreferenceProvider preferenceProvider;
    @Mock SharedPreferences mSharedPreferences;
    @Mock SharedPreferences.Editor mEditor;
    final User user = new User();

    @Before
    public void setUp(){
        // Create a dummy user object
        user.setId(1);

        // Mock Android instance classes
        mContext = Mockito.mock(Context.class);
        mSharedPreferences = Mockito.mock(SharedPreferences.class);
        mEditor = Mockito.mock(SharedPreferences.Editor.class);
    }

    @Test
    public void userSessionCreationTest(){
        MockitoAnnotations.initMocks(this);
        Assert.assertNotNull(mContext);
        Mockito.when(PreferenceProvider.getSharedPreferences(mContext, Preferences.SESSION)).thenReturn(mSharedPreferences);
        Mockito.when(PreferenceProvider.getEditor(mContext, Preferences.SESSION)).thenReturn(mEditor);
        SessionManager.newSession(mContext, user);
        Assert.assertEquals(1, SessionManager.getCurrentUser().getId());
    }

    @Test
    public void userLoginNetworkTest(){
        APIInterface apiManager = ApiManager.getApiService();
        Call<UserResponse> call = apiManager.getUser(new AuthData("ronaldo", "pass"));
        ApiManager.request(call, new NetworkCallback<UserResponse>() {
            @Override
            public void onSuccess(UserResponse response) {
                Assert.assertEquals("success", response.getStatus());
            }

            @Override
            public void onFail(String error) {
                Assert.fail(error);
            }
        });
    }

    @Test
    public void destroyUserSession(){
        Mockito.when(PreferenceProvider.getSharedPreferences(mContext, Preferences.SESSION)).thenReturn(mSharedPreferences);
        Mockito.when(PreferenceProvider.getEditor(mContext, Preferences.SESSION)).thenReturn(mEditor);
        SessionManager.destroySession(mContext);
        Assert.assertEquals(null, SessionManager.getCurrentUser());

    }

    @After
    public void cleanUp(){
        mEditor.clear();
    }

}
