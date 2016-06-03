package com.jaysyko.wrestlechat.network;

import com.jaysyko.wrestlechat.network.responses.EventResponse;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by jarushaan on 2016-06-03
 */
public interface APIInterface {

    @GET("events")
    Call<EventResponse> getEvents();
}
