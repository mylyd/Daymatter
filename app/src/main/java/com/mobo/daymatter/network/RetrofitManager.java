package com.mobo.daymatter.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 1、使用方法RetrofitManager.INSTANCE.getRequest(？？)
 * 2、若更换baseUrl，则需要在请求的时候加上 @Headers({"url_type:weather"})， 并且在拦截器中增加适配：urls，types
 */
public enum RetrofitManager {
    INSTANCE;

    private static Retrofit sRetrofit;
    private static final String BASE_URL = "http://api.mobo.ai/";

    RetrofitManager() {
        init();
    }

    private void init() {
        // 初始化okhttp
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();

        // 初始化Retrofit
        sRetrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

    public <T> T getRequest(Class<? extends T> cls) {
        return sRetrofit.create(cls);
    }
}
