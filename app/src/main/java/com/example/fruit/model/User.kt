package com.example.myapplication.model

import retrofit2.http.Body
import retrofit2.http.POST


// Data class cho yêu cầu đăng nhập
data class LoginRequest(
    val email: String,
    val password: String,
    val forgotPassword: Boolean = false
)

// Data class cho phản hồi từ API
data class LoginResponse(
    val user: User? = null,
    val token: String? = null,
    val message: String? = null,
    val error: String?
)

// Data class cho thông tin người dùng
data class User(
    val _id: String,
    val first_name: String,
    val email: String,
    val phone_number: String,
    val isVerified: Boolean,
    val role: String
)

// Data class cho phản hồi lỗi từ API
data class ErrorResponse(
    val error: String? = null,
    val message: String? = null,
    val status: Int? = null
)

data class RegisterRequest(
    val first_name: String,
    val email: String,

    val password: String,
    val phone_number: String,
)
data class VerifyResetCodeRequest(
    val email: String,
    val resetCode: String
)

data class ResetCodeResponse(
    val message: String?,
    val error: String?
)

data class ResetPasswordRequest(
    val email: String,
    val resetCode: String,
    val newPassword: String
)

data class ResetPasswordResponse(
    val message: String?,
    val error: String?
)