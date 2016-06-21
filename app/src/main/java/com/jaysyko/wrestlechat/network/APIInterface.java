package com.jaysyko.wrestlechat.network;

import com.jaysyko.wrestlechat.network.requestData.MessageData;
import com.jaysyko.wrestlechat.network.requestData.UserData;
import com.jaysyko.wrestlechat.network.responses.EventResponse;
import com.jaysyko.wrestlechat.network.responses.MessageSaveResponse;
import com.jaysyko.wrestlechat.network.responses.MessageGetResponse;
import com.jaysyko.wrestlechat.network.responses.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by jarushaan on 2016-06-03
 */
public interface ApiInterface {

    @GET("events")
    Call<EventResponse> getEvents();

    @POST("auth/login")
    Call<UserResponse> getUser(
            @Body UserData data
    );

    @POST("auth/register")
    Call<UserResponse> createUser(
            @Body UserData data
    );

    @POST("messages/save")
    Call<MessageSaveResponse> saveMessage(
            @Body MessageData data
    );

    @POST("messages/event")
    Call<MessageGetResponse> createMessage(
            @Body MessageData data
    );
}
