package com.example.myspace.data;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RetrofitService {

    @GET("weather?APPID=6e71959cff1c0c71a6049226d45c69a1&units=metric&q={city}")
    Call<ResponseBody> loadCityWeather(@Path("city") String city);
}
