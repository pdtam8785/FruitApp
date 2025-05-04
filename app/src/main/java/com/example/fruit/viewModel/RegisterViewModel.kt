package com.example.myapplication.viewModel

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myapplication.model.ErrorResponse
import com.example.myapplication.model.RegisterRequest
import com.example.myapplication.network.User.ApiClient
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RegisterViewModel : ViewModel() {
    val firstName = mutableStateOf("")
    val email = mutableStateOf("")
    val password = mutableStateOf("")
    val phoneNumber = mutableStateOf("")
    val registerError = mutableStateOf<String?>(null)
    val registerSuccess = mutableStateOf<String?>(null)
    val role = mutableStateOf<String?>(null) // Thêm trạng thái cho role

    private lateinit var sharedPreferences: SharedPreferences

    fun initSharedPreferences(context: Context) {
        sharedPreferences = context.getSharedPreferences("FruitHubPrefs", Context.MODE_PRIVATE)
        Log.d("RegisterViewModel", "SharedPreferences initialized")
    }

    private fun saveToken(token: String, userId: String, role: String) {
        with(sharedPreferences.edit()) {
            putString("auth_token", token)
            putString("user_id", userId)
            putString("user_role", role) // Lưu role
            apply()
        }
        Log.d("RegisterViewModel", "Token saved: $token, Role: $role")
    }

    fun doRegister(navigator: NavController) {
        viewModelScope.launch {
            try {
                Log.d(
                    "RegisterViewModel", "Register Request Data: " +
                            "firstName=${firstName.value}, " +
                            "email=${email.value}, " +
                            "password=${password.value}, " +
                            "phoneNumber=${phoneNumber.value}"
                )
                val response = ApiClient.authApi.register(
                    RegisterRequest(
                        firstName.value,
                        email.value,
                        password.value,
                        phoneNumber.value
                    )
                )
                Log.d("RegisterViewModel", "Register Response: $response")

                if (response.token != null && response.user != null) {
                    saveToken(response.token, response.user._id, response.user.role)
                    role.value = response.user.role // Cập nhật role trong ViewModel
                    registerError.value = null
                    registerSuccess.value = response.message
                    resetState()
                    Log.d("RegisterViewModel", "Đăng ký thành công, token: ${response.token}, role: ${response.user.role}")
                    navigator.navigate("login") {
                        popUpTo(navigator.graph.startDestinationId) {
                            inclusive = true
                        }
                    }
                } else {
                    registerError.value = "Đăng ký thất bại: Không nhận được token hoặc thông tin người dùng"
                    Log.d("RegisterViewModel", "Token hoặc user không hợp lệ: ${response.token}")
                }
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.d("RegisterViewModel", "Error Body: $errorBody")
                val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                registerError.value = when (e.code()) {
                    400 -> errorResponse.error ?: "Dữ liệu không hợp lệ hoặc email đã tồn tại"
                    403 -> errorResponse.error ?: "Không được phép gán vai trò này"
                    500 -> errorResponse.error ?: "Lỗi gửi email xác nhận"
                    else -> "Đăng ký thất bại: ${e.message()}"
                }
            } catch (e: Exception) {
                registerError.value = "Đăng ký thất bại: ${e.message}"
                Log.d("RegisterViewModel", "Exception: ${e.message}")
            }
        }
    }

    private fun resetState() {
        email.value = ""
        password.value = ""
        firstName.value = ""
        phoneNumber.value = ""
        registerError.value = null
        role.value = null
    }
}
