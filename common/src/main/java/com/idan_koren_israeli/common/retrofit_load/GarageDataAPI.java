package com.idan_koren_israeli.common.retrofit_load;

import com.idan_koren_israeli.common.GarageData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GarageDataAPI {


    @GET("{garageKey}")
    Call<GarageData> loadGarageData(@Path(value = "garageKey", encoded = true) String _movieKey);


}
