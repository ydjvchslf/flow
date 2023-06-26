package com.poliot.coroutine.data.response

import com.google.gson.annotations.SerializedName
import com.poliot.coroutine.ProductEntity

data class RemoteProductData(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("price") val price: Double,
    @SerializedName("description") val description: String,
    @SerializedName("image") val image: String,
    @SerializedName("rating") val rating: Rate
)

data class Rate(
    val rate: Double
)

fun RemoteProductData.toEntity() = ProductEntity (
    id = id,
    title = title,
    price = price,
    description = description,
    image = image,
    rate = rating.rate
)

fun List<RemoteProductData>.toEntity() = map {
    it.toEntity()
}