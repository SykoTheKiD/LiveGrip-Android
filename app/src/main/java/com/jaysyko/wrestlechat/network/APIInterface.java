package com.jaysyko.wrestlechat.network;

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

    // URLS
    String API_VERSION = "v1/";
    String EVENTS = API_VERSION + "events";
    String LOGIN = API_VERSION + "auth/login";
    String REGISTER = API_VERSION + "auth/register";
    String MESSAGES_SAVE = API_VERSION + "messages/save";
    String MESSAGES_EVENT = API_VERSION + "messages&event={eventID}";
    String USER_UPDATE_PROFILE_IMAGE = API_VERSION + "user/update/profile_image";
    String USER_UPDATE_FCM = API_VERSION + "user/update/fcm";
    String MESSAGE_HISTORY = API_VERSION + "message_history&event={eventID}&offset={offset}";

    // URL Params
    String EVENT_ID = "eventID";
    String AUTHORIZATION = "Authorization";
    String OFFSET = "offset";

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

    @GET(MESSAGE_HISTORY)
    Call<MessageGetResponse> getMessageHistory(
            @Header(AUTHORIZATION) String token,
            @Path(EVENT_ID) int id,
            @Path(OFFSET) int offset
    );


    @POST(USER_UPDATE_PROFILE_IMAGE)
    Call<GenericResponse> updateProfileImage(
            @Header(AUTHORIZATION) String token,
            @Body UpdateUserImageData newImage
    );

    @POST(USER_UPDATE_FCM)
    Call<GenericResponse> updateFCMID(
            @Header(AUTHORIZATION) String token,
            @Body UpdateFCMData payload
    );
}
