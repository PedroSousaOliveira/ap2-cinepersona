package com.example.p2.data.remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class RetrofitClient {
    // Em celular físico, troque pelo IP local do computador.
    private static final String BASE_URL = "http://10.0.2.2:8000/";
    private static final ApiService API_SERVICE = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService.class);

    private RetrofitClient() {
    }

    public static ApiService getApiService() {
        return API_SERVICE;
    }
}
