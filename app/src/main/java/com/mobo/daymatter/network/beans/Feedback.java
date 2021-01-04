package com.mobo.daymatter.network.beans;

import com.google.gson.JsonElement;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Feedback {
    @POST("client/v3/user/feedback.json")
    Call<JsonElement> postFeedBack(@Body Map<String, String> params);
}
