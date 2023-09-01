package com.example.myspace.data;

import android.util.Log;

import com.example.myspace.data.dto.CardDto;
import com.example.myspace.data.entity.Card;
import com.example.myspace.data.service.RetrofitService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TinyFitnessProvider {

    private static TinyFitnessProvider ourInstance = new TinyFitnessProvider();
    public static TinyFitnessProvider getInstance() {
        return ourInstance;
    }

    private final String TINY_FITNESS_URL = "https://tiny-fitness.ru/api/";

    final String TAG = "myLogs";

    public void saveCard(CardDto cardDto) {
        Log.d(TAG, "saveCard " + cardDto);

        RetrofitService api = Helper.getRetrofitApiWithUrl(TINY_FITNESS_URL);

        /*api.saveCard(card.getInternalCode(), card.getEditDateTime(), card.getFront(), card.getBack(), card.getExample(), card.getStatus())
                .enqueue(new Callback<ResponseBody>() {

                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Log.d(TAG, "onResponse");
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.d(TAG, "onFailure");
                    }
                });*/
        Call<CardDto> call = api.saveCardPost(cardDto);

        call.enqueue(new Callback<CardDto>() {
            @Override
            public void onResponse(Call<CardDto> call, Response<CardDto> response) {
                Log.d(TAG, "onResponse");
            }

            @Override
            public void onFailure(Call<CardDto> call, Throwable t) {
                Log.d(TAG, "onFailure");
            }
        });
    }
}
