package com.example.openquest;

import retrofit2.converter.gson.GsonConverterFactory;

public class Retrofit {
    retrofit2.Retrofit retrofit = new retrofit2.Retrofit.Builder()
            .baseUrl("https://31a1-31-222-117-187.ngrok-free.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    APIService api = retrofit.create(APIService.class);
}
