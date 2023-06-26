package com.poliot.coroutine.retrofit

import com.poliot.coroutine.data.response.RemoteProductData
import retrofit2.http.*

interface RetrofitService {

    @GET("products")
    suspend fun getProductList(): Result<List<RemoteProductData>>

    @GET("products/{id}")
    suspend fun getSingleProduct(@Path (value = "id") id: Int): Result<RemoteProductData>
}