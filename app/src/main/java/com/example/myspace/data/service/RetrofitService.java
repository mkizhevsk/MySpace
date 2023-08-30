package com.example.myspace.data.service;

import com.example.myspace.data.weather.Weather;

import java.time.LocalDateTime;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitService {

    @GET("/data/2.5/weather")
    Call<ResponseBody> loadCityWeather(
            @Query("APPID") String appId,
            @Query("units") String units,
            @Query("q") String city
    );

    @GET("/data/2.5/weather")
    Call<Weather> loadPojoCityWeather(
            @Query("APPID") String appId,
            @Query("units") String units,
            @Query("q") String city
    );

    @GET("/addCard")
    Call<ResponseBody> saveCard(
            @Query("internalCode") String internalCode,
            @Query("editDateTime") LocalDateTime editDateTime,
            @Query("front") String front,
            @Query("back") String back,
            @Query("example") String example,
            @Query("status") int status
    );
}
