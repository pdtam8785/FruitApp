package com.example.myapplication.network.User




import com.example.fruit.network.ZaloPayApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ZaloPayApiClient {
    private const val BASE_URL = "https://sb-openapi.zalopay.vn/"

    val apiService: ZaloPayApiService by lazy {
        val client = OkHttpClient.Builder().build()
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ZaloPayApiService::class.java)
    }
}
