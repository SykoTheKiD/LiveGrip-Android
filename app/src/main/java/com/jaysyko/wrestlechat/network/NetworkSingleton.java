package com.jaysyko.wrestlechat.network;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * NetworkSingleton.java
 *
 * Queues up Volley Requests to be sent out
 * @author Jay Syko
 */
public class NetworkSingleton {
    private static NetworkSingleton mInstance = new NetworkSingleton();
    private static Retrofit retrofit;
    private ApiInterface apiService;

    private NetworkSingleton() {
        apiService = getClient().create(ApiInterface.class);
    }

    static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(URLS.getServerURL())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    /**
     * Get an instance of the class
     * @return instance
     */
    public static synchronized NetworkSingleton getInstance() {
        return mInstance;
    }

    public ApiInterface getApiService() {
        return apiService;
    }

    public <T> void request(Call<T> call, final NetworkCallback<T> callback){
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                callback.onFail(t.getMessage());
            }
        });
    }
}