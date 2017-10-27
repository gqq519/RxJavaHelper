package com.gqq.rxjavahelper.rxjava1;

import com.gqq.rxjavahelper.rxjava2.GankDateModel;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by gqq on 17/9/20.
 */

public interface GankApi {

    // Retrofit
    @GET("/api/data/Android/10/1")
    Call<GankModel> getGankData();

    // Retrofit 结合 RxJava
    @GET("/api/data/Android/{month}/{day}")
    Observable<GankModel> getGankData1(@Path("month") int month,@Path("day")int day);

    @GET("/api/day/history")
    Observable<GankDateModel> getGankDate();

}
