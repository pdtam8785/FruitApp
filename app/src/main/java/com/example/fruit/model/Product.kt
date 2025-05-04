package com.example.myapplication.model

import retrofit2.http.Body
import retrofit2.http.POST

data class Product(
    val _id:String,
    val name: String,
    val description: String,
    val price: Int,
    val image_url: List<String>,
    val category_id: String,
    val quantity : Int,
)