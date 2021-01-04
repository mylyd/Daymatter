package com.mobo.daymatter.network.beans;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface GrayRequest {
    @GET("http://mobotoolpush.moboapps.io/ipo/api/gray/status")
    Call<GrayItem> getSwitchConfig(@QueryMap Map<String, String> params);
}
