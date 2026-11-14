package com.example.p2.data.remote;

import com.example.p2.BuildConfig;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class RetrofitClient {
    private static final ApiService API_SERVICE = new Retrofit.Builder()
            // Em celular físico, use localhost com adb reverse e api.baseUrl no local.properties.
            .baseUrl(BuildConfig.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService.class);

    private RetrofitClient() {
    }

    public static ApiService getApiService() {
        return API_SERVICE;
    }
}
