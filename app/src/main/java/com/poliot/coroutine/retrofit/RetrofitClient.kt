package com.poliot.coroutine.retrofit

import com.google.gson.GsonBuilder
import com.poliot.coroutine.util.BASE_URL
import com.poliot.coroutine.util.DebugLog
import com.poliot.vest.retrofit.ResponseAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitClient {
    //로그 설정
    private val loggingInterceptor = HttpLoggingInterceptor { message ->
        DebugLog.httpResponse(message)
    }.apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    //Retrofit 객체 초기화
    private val retrofit: Retrofit = Retrofit.Builder()
        .addCallAdapterFactory(ResponseAdapterFactory())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        .build()

    val retrofitService: RetrofitService = retrofit.create(RetrofitService::class.java)
}