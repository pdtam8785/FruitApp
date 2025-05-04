package com.example.fruit.model

import retrofit2.http.Body
import retrofit2.http.POST

data class CreateOrderRequest(
    val app_id: Int,
    val app_user: String,
    val app_time: Long,
    val amount: Long,
    val app_trans_id: String,
    val embed_data: String,
    val item: String,
    val description: String,
    val mac: String,
    val bank_code: String = ""
)

data class CreateOrderResponse(
    val return_code: Int,
    val return_message: String,
    val zp_trans_token: String,
    val order_url: String
)