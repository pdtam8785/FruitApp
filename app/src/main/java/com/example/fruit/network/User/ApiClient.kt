package com.example.myapplication.network.User




import com.example.fruit.network.AuthApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
const val URL = "http://192.168.0.103:3001/"
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

