package com.example.myapplication.model

import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.POST

data class AddOrderItemRequest(
    val user_id: String,
    val quantity: Int,
    val price: Int,
    val product_id: String
)

// Định nghĩa data class cho response từ API
data class AddOrderItemResponse(
    val success: Boolean,
    val message: String,
    val data: OrderItem? = null
)

data class OrderItem(
    val _id: String,
    val user_id: String,
    val quantity: Int,
    val price: Int,
    val product_Order: Product,
    val product_id: String
)
data class OrderItemResponse(
    val success: Boolean,
    val message: String,
    val data: List<OrderItem>?
)
data class BaseResponse<T>(
    val success: Boolean,
    val message: String?,
    val data: T? = null
)