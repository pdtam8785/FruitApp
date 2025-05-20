package com.example.myapplication.model

import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.POST

data class AddOrderRequest(
    val user_id: String,
    val total_amount: Int,
    val status: Int? = null,
    val delivery_address: String,
    val phone_number: String,
    val payment_method: String
)

data class AddOrderResponse(
    val success: Boolean,
    val message: String,
    val data: Order? = null
)

data class Order(
    val _id: String,
    val user_id: String,
    val total_amount: Int,
    val status: Int,
    val delivery_address: String,
    val created_at: String,
    val payment_method: String,
)