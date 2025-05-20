package com.example.fruit.network

import com.example.fruit.model.CreateOrderRequest
import com.example.fruit.model.CreateOrderResponse
import com.example.myapplication.model.AddOrderItemRequest
import com.example.myapplication.model.AddOrderItemResponse
import com.example.myapplication.model.AddOrderRequest
import com.example.myapplication.model.AddOrderResponse
import com.example.myapplication.model.BaseResponse
import com.example.myapplication.model.Category
import com.example.myapplication.model.GetOrdersResponse
import com.example.myapplication.model.GetUserInfoResponse

import com.example.myapplication.model.LoginRequest
import com.example.myapplication.model.LoginResponse
import com.example.myapplication.model.OrderItemResponse
import com.example.myapplication.model.Product
import com.example.myapplication.model.RegisterRequest
import com.example.myapplication.model.ResetCodeResponse
import com.example.myapplication.model.ResetPasswordRequest
import com.example.myapplication.model.ResetPasswordResponse
import com.example.myapplication.model.UpdateUserInfoRequest
import com.example.myapplication.model.UpdateUserInfoResponse
import com.example.myapplication.model.VerifyResetCodeRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query


interface AuthApi {
    //user
    @POST("user/login") // Đường dẫn endpoint, ví dụ: /login
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("user/reg")
    suspend fun register(@Body request: RegisterRequest): LoginResponse

    @POST("user/verify-reset-code")
    suspend fun verifyResetCode(@Body request: VerifyResetCodeRequest): ResetCodeResponse

    @POST("user/reset-password")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): ResetPasswordResponse

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

    @DELETE("orderItem/deleteOrderItems/{orderitem_id}")
    suspend fun deleteOrderItem(
        @Path("orderitem_id") orderitem_id: String
    ): BaseResponse<Unit>

    // order
    @POST("order/addOrder")
    suspend fun addOrder(
        @Body request: AddOrderRequest
    ): AddOrderResponse
    @PUT("order/updateUserInfo/{user_id}")
    suspend fun updateUserInfo(
        @Path("user_id") userId: String,
        @Body request: UpdateUserInfoRequest
    ): UpdateUserInfoResponse
    @GET("order/users/{user_id}")
    suspend fun getUserInfo(
        @Path("user_id") userId: String
    ): GetUserInfoResponse
    @GET("/order/getOrders/{user_id}")
    suspend fun getOrders(
        @Path("user_id") userId: String // Sử dụng path parameter thay vì query parameter
    ): GetOrdersResponse
}
interface ZaloPayApiService {
    @POST("v2/create")
    suspend fun createOrder(@Body request: CreateOrderRequest): CreateOrderResponse
}