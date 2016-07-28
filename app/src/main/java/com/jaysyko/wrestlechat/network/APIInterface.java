package com.jaysyko.wrestlechat.network;

import com.jaysyko.wrestlechat.application.App;
import com.jaysyko.wrestlechat.network.requestData.AuthData;
import com.jaysyko.wrestlechat.network.requestData.MessageData;
import com.jaysyko.wrestlechat.network.requestData.UpdateFCMData;
import com.jaysyko.wrestlechat.network.requestData.UpdateUserImageData;
import com.jaysyko.wrestlechat.network.responses.EventResponse;
import com.jaysyko.wrestlechat.network.responses.GenericResponse;
import com.jaysyko.wrestlechat.network.responses.MessageGetResponse;
import com.jaysyko.wrestlechat.network.responses.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * ApiInterface.java
 * An Interface for the LiveGrip API
 *
 * @author Jay Syko
 */
public interface APIInterface {

    String EVENTS = "events&app_version=" + App.APP_VERSION;
    String LOGIN = "auth/login";
    String REGISTER = "auth/register";
    String MESSAGES_SAVE = "messages/save";
    String MESSAGES_EVENT = "messages&event={eventID}";
    String AUTHORIZATION = "Authorization";
    String EVENT_ID = "eventID";
    String USER_UPDATE_PROFILE_IMAGE = "user/update/profile_image";

    @POST(LOGIN)
    Call<UserResponse> getUser(
            @Body AuthData data
    );

    @POST(REGISTER)
    Call<UserResponse> createUser(
            @Body AuthData data
    );

    @GET(EVENTS)
    Call<EventResponse> getEvents(
            @Header(AUTHORIZATION) String token
    );

    @POST(MESSAGES_SAVE)
    Call<GenericResponse> saveMessage(
            @Header(AUTHORIZATION) String token,
            @Body MessageData data
    );

    @GET(MESSAGES_EVENT)
    Call<MessageGetResponse> getMessages(
            @Header(AUTHORIZATION) String token,
            @Path(EVENT_ID) int id
    );

    @POST(USER_UPDATE_PROFILE_IMAGE)
    Call<GenericResponse> updateProfileImage(
            @Header(AUTHORIZATION) String token,
            @Body UpdateUserImageData newImage
    );

    @POST("user/update/fcm")
    Call<GenericResponse> updateFCMID(
            @Header(AUTHORIZATION) String token,
            @Body UpdateFCMData payload
    );
}
