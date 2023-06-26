package com.poliot.coroutine.retrofit

import com.poliot.coroutine.ProductEntity
import com.poliot.coroutine.data.response.toEntity
import com.poliot.coroutine.util.DebugLog

class RemoteDataSource {

    private val logTag = RemoteDataSource::class.simpleName
    private val retrofitService = RetrofitClient.retrofitService

    suspend fun getAllProducts(): List<ProductEntity>? {
        DebugLog.w(logTag, "getAllProducts-()")
        val response = retrofitService.getProductList()
        return when (response) {
            is Result.Success -> {
                DebugLog.i(logTag, "Result Success!!")
                response.data
                val remoteProductData = response.data
                return remoteProductData.toEntity()
            }
            is Result.ApiError -> {
                DebugLog.i(logTag, "ApiError!!")
                return null
            }
            is Result.NetworkError -> {
                DebugLog.i(logTag, "NetworkError!!")
                DebugLog.d(logTag, "response.throwable => ${response.throwable}")
                return null
            }
            else -> {
                null
            }
        }
    }

    suspend fun getOneProduct(id: String): ProductEntity? {
        DebugLog.w(logTag, "getOneProduct-()")
        val response = retrofitService.getSingleProduct(id)
        return when (response) {
            is Result.Success -> {
                DebugLog.i(logTag, "Result Success!!")
                val remoteProductData = response.data
                return remoteProductData.toEntity()
            }
            is Result.ApiError -> {
                DebugLog.i(logTag, "ApiError!!")
                return null
            }
            is Result.NetworkError -> {
                DebugLog.i(logTag, "NetworkError!!")
                DebugLog.d(logTag, "response.throwable => ${response.throwable}")
                return null
            }
            else -> {
                null
            }
        }
    }
}