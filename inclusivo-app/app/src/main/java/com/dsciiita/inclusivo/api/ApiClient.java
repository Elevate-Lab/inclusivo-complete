package com.dsciiita.inclusivo.api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    public static Retrofit getRetrofit(){

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);

        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(loggingInterceptor)
                .connectTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .build();
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://inclusivo-api.el.r.appspot.com/")
                .client(okHttpClient)
                .build();

        return retrofit;
    }

    public static UserService getUserService(){
        return getRetrofit().create(UserService.class);
    }
}
