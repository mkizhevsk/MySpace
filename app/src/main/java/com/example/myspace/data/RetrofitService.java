package com.example.myspace.data;

import com.example.myspace.data.weather.Weather;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitService {

    @GET("/data/2.5/weather")
    Call<ResponseBody> loadCityWeather(@Query("APPID") String appId, @Query("units") String units, @Query("q") String city);

    @GET("/data/2.5/weather")
    Call<Weather> loadPojoCityWeather(@Query("APPID") String appId, @Query("units") String units, @Query("q") String city);
}
