package com.retrytech.medialoot.network;


import com.retrytech.medialoot.model.DMVideo;
import com.retrytech.medialoot.model.VimeoModels;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("complete/search")
    Observable<String> getGoogleSuggetion(@Query("q") String query, @Query("client") String client, @Query("hl") String language);

    @GET("{Id}/config")
    Call<VimeoModels> getmyvimeimodel(@Path("Id") String str);

    @GET("{Id}")
    Call<DMVideo> getmydailymodel(@Path("Id") String str);

}
