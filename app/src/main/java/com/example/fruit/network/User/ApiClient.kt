package com.example.myapplication.network.User



import com.example.myapplication.network.AuthApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
const val URL = "http://192.168.0.105:3000/"
object ApiClient {

    private const val BASE_URL = URL// Thay bằng URL API của bạn

    val authApi: AuthApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApi::class.java)
    }
}