package com.example.luggerz.Remote;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public class IGoogleAPI {
    @GET
    public Call<String> getPath(@Url String url) {
        return null;
    }
}
