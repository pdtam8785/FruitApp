package com.example.myapplication.network

import com.example.myapplication.model.AddOrderItemRequest
import com.example.myapplication.model.AddOrderItemResponse
import com.example.myapplication.model.Category
import com.example.myapplication.model.LoginRequest
import com.example.myapplication.model.LoginResponse
import com.example.myapplication.model.OrderItemResponse
import com.example.myapplication.model.Product
import com.example.myapplication.model.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


interface AuthApi {
//user
    @POST("user/login") // Đường dẫn endpoint, ví dụ: /login
    suspend fun login(@Body request: LoginRequest): LoginResponse
    @POST("user/reg")
    suspend fun register(@Body request: RegisterRequest): LoginResponse
// catogory
    @GET("category/getCategory")
    suspend fun getCategories(): List<Category>
// product
    @GET("product/getProduct")
    suspend fun getProducts(@Query("category_id") categoryId: String? = null): List<Product>
    @GET("product/getProductDetail/{id}")
    suspend fun getProductDetail(@Path("id") productId: String): Product
// order_item
    @POST("orderItem/addOrderItem")
    suspend fun addOrderItem(@Body request: AddOrderItemRequest): AddOrderItemResponse
    @GET("orderItem/getOrderItems/{user_id}")
    suspend fun getOrderItems(
        @Path("user_id") userId: String
    ): OrderItemResponse
}