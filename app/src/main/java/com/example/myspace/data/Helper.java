package com.example.myspace.data;

import com.example.myspace.data.service.RetrofitService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Helper {

    public static DateTimeFormatter getDateTimeFormatter() {
        return DateTimeFormatter.ofPattern("yyyy-LL-dd HH:mm:ss");
    }

    public static DateTimeFormatter getDateFormatter() {
        return DateTimeFormatter.ofPattern("yyyy-LL-dd");
    }

    public static String getStringValue(int value) {
        if(value <10) return "0" + value;
        return String.valueOf(value);
    }

    public static String getStringDateTime(LocalDateTime localDateTime) {
        return getDateTimeFormatter().format(localDateTime);
    }

    public static RetrofitService getRetrofitApiWithUrl(String url) {

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(RetrofitService.class);
    }
}
