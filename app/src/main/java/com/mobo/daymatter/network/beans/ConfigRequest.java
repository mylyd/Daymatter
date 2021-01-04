package com.mobo.daymatter.network.beans;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface ConfigRequest {
    @GET("/client/v2/resource/config.json?")
    Call<ConfigItem> getConfigRequest(@QueryMap Map<String, String> params);
}
