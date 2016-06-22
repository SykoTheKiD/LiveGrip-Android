package com.jaysyko.wrestlechat.network;

import com.jaysyko.wrestlechat.network.requestData.MessageData;
import com.jaysyko.wrestlechat.network.requestData.UserData;
import com.jaysyko.wrestlechat.network.responses.EventResponse;
import com.jaysyko.wrestlechat.network.responses.MessageGetResponse;
import com.jaysyko.wrestlechat.network.responses.MessageSaveResponse;
import com.jaysyko.wrestlechat.network.responses.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * ApiInterface.java
 * An Interface for the LiveGrip API
 *
 * @author Jay Syko
 */
public interface ApiInterface {

    String EVENTS = "events";
    String LOGIN = "auth/login";
    String REGISTER = "auth/register";
    String MESSAGES_SAVE = "messages/save";
    String MESSAGES_EVENT = "messages&event={eventID}";
    String AUTHORIZATION = "Authorization";
    String EVENT_ID = "eventID";

    @POST(LOGIN)
    Call<UserResponse> getUser(
            @Body UserData data
    );

    @POST(REGISTER)
    Call<UserResponse> createUser(
            @Body UserData data
    );

    @GET(EVENTS)
    Call<EventResponse> getEvents(
            @Header(AUTHORIZATION) String token
    );

    @POST(MESSAGES_SAVE)
    Call<MessageSaveResponse> saveMessage(
            @Header(AUTHORIZATION) String token,
            @Body MessageData data
    );

    @POST(MESSAGES_EVENT)
    Call<MessageGetResponse> getMessages(
            @Header(AUTHORIZATION) String token,
            @Query(EVENT_ID) int id
    );
}
