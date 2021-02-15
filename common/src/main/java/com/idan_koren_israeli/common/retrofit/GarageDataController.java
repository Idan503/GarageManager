package com.idan_koren_israeli.common.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.idan_koren_israeli.common.GarageData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GarageDataController implements Callback<GarageData> {

    private static final String BASE_URL = "https://pastebin.com/raw/";

    private OnGarageDataLoadedListener responseCallback;

    public void fetchGarageData(String garageKey, OnGarageDataLoadedListener onLoaded) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        GarageDataAPI garageAPI = retrofit.create(GarageDataAPI.class);

        responseCallback = onLoaded;

        Call<GarageData> call = garageAPI.loadGarageData(garageKey);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<GarageData> call, Response<GarageData> response) {
        if (response.isSuccessful()) {
            GarageData garageData = response.body();
            if(responseCallback != null)
                responseCallback.onGarageDataLoaded(garageData);

        } else {
            System.out.println(response.errorBody());
        }
    }

    @Override
    public void onFailure(Call<GarageData> call, Throwable t) {
        t.printStackTrace();
    }
}