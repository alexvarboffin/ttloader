package com.retrytech.medialoot.network;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.retrytech.medialoot.model.VimeoModels;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import retrofit2.converter.gson.GsonConverterFactory;

public class CallVimeoApi {
    private static CallVimeoApi callVimeoApi;
    String Base_URL = "https://player.vimeo.com/video/";
    Gson gson;
    Retrofit retrofit;
    ApiService webservices;

    private CallVimeoApi() {
        initretrofit();
    }

    public static CallVimeoApi getInstance() {
        if (callVimeoApi == null) {
            callVimeoApi = new CallVimeoApi();
        }
        return callVimeoApi;
    }

    public void initretrofit() {
        this.gson = new Gson();
        this.retrofit = new Builder().baseUrl(this.Base_URL).addConverterFactory(GsonConverterFactory.create(this.gson)).build();
        this.webservices = (ApiService) this.retrofit.create(ApiService.class);
    }

    public void getVimeoVideoResponce(final Context context2, String str, final ResponseStatus responseChecker) {
        this.webservices.getmyvimeimodel(str).enqueue(new Callback<VimeoModels>() {
            public void onResponse(Call<VimeoModels> call, Response<VimeoModels> response) {
                if (response.isSuccessful()) {
                    responseChecker.onSuccess(response.body());
                    return;
                }
                int code = response.code();
                if (code == 400) {
                    Toast.makeText(context2, "page_end", Toast.LENGTH_SHORT).show();
                } else if (code == 504) {
                    Toast.makeText(context2, "Server_network_Problem", Toast.LENGTH_SHORT).show();
                }
            }

            public void onFailure(Call<VimeoModels> call, Throwable th) {
                isKnownException(th);
                Log.e("VimieoError", "onFailure: "+th.getMessage());
            }
        });
    }
    public static boolean isKnownException(Throwable th) {
        return (th instanceof ConnectException) || (th instanceof UnknownHostException) || (th instanceof SocketTimeoutException) || (th instanceof IOException);
    }
}
