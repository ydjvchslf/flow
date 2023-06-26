package com.poliot.coroutine

class ProductEntity(
    val id: Int,
    val title: String?,
    val price: Double?,
    val description: String,
    val image: String,
    val rate: Double,
) {
    override fun toString(): String {
        return "id: $id, title: $title, price: $price"
    }
}