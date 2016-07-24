package com.jaysyko.wrestlechat.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;

import com.google.gson.annotations.SerializedName;
import com.jaysyko.wrestlechat.application.eLog;
import com.jaysyko.wrestlechat.exceptions.ImageURLError;
import com.jaysyko.wrestlechat.network.ApiManager;
import com.jaysyko.wrestlechat.network.NetworkCallback;
import com.jaysyko.wrestlechat.network.requestData.UpdateUserImageData;
import com.jaysyko.wrestlechat.network.responses.FailedRequestResponse;
import com.jaysyko.wrestlechat.network.responses.GenericResponse;
import com.jaysyko.wrestlechat.sharedPreferences.PreferenceKeys;
import com.jaysyko.wrestlechat.sharedPreferences.PreferenceProvider;
import com.jaysyko.wrestlechat.sharedPreferences.Preferences;
import com.jaysyko.wrestlechat.utils.ImageTools;

import retrofit2.Call;

/**
 * @author Jay Syko
 */
public final class User {

    private static final String TAG = User.class.getSimpleName();
    private static final String AUTH_PREFIX = "Token ";
    private static final String BAD_IMAGE_URL_MESSAGE = "Bad Image URL";
    @SerializedName(Utils.ID)
    private int id;
    @SerializedName(Utils.USERNAME)
    private String username;
    @SerializedName(Utils.PROFILE_IMAGE)
    private String profile_image;
    @SerializedName(Utils.TOKEN)
    private Token token;

    public String getAuthToken() {
        return AUTH_PREFIX + token.getToken();
    }

    public void setAuthToken(Token authToken) {
        this.token = authToken;
    }

    public Token getToken(){
        return token;
    }

    public String getProfileImage() {
        eLog.i(TAG, profile_image);
        return profile_image;
    }

    public void setProfileImage(final String profileImage, final Context context) throws ImageURLError {
        if(ImageTools.isLinkToImage(profileImage)){
            setLocalProfileImage(profileImage);
            Call<GenericResponse> call = ApiManager.getApiService().updateProfileImage(getAuthToken(), new UpdateUserImageData(getId(), getProfileImage()));
            ApiManager.request(call, new NetworkCallback<GenericResponse>() {
                @Override
                public void onSuccess(GenericResponse response) {
                    eLog.i(TAG, "Profile Image Updated");
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            updateProfileImage(context, profileImage);
                        }
                    });
                }

                @Override
                public void onFail(FailedRequestResponse error) {
                    error.getCode(context);
                    eLog.e(TAG, error.getMessage());
                }
            });

        }else{
            throw new ImageURLError(BAD_IMAGE_URL_MESSAGE);
        }
    }

    private void updateProfileImage(Context context, String profileImage) {
        SharedPreferences.Editor editor = PreferenceProvider.getEditor(context, Preferences.SESSION);
        editor.putString(PreferenceKeys.NEW_PROFILE_IMAGE, profileImage);
        PreferenceProvider.closeEditor(editor);
    }

    public void setLocalProfileImage(String profileImage){
        this.profile_image = profileImage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return getUsername();
    }
}
