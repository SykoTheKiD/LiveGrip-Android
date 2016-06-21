package com.jaysyko.wrestlechat.network;

import com.jaysyko.wrestlechat.network.responses.AuthErrorResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * NetworkSingleton.java
 * <p/>
 * Queues up Volley Requests to be sent out
 *
 * @author Jay Syko
 */
public class ApiManager {

     static final Retrofit RETROFIT = new Retrofit.Builder()
            .baseUrl(URLS.getServerURL())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private static final ApiInterface API_SERVICE = RETROFIT.create(ApiInterface.class);

    public static ApiInterface getApiService() {
        return API_SERVICE;
    }

    public static <T> void request(Call<T> call, final NetworkCallback<T> callback) {
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if(response.isSuccessful()){
                    callback.onSuccess(response.body());
                }else{
                    AuthErrorResponse authErrorResponse = ApiErrorManager.parseError(response);
                    callback.onFail(authErrorResponse.getMessage());
                }

            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                callback.onFail(t.getMessage());
            }
        });
    }
}