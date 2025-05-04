package com.example.myapplication.viewModel



import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.myapplication.model.ErrorResponse
import com.example.myapplication.model.LoginRequest
import com.example.myapplication.network.User.ApiClient
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException
//
//class LoginViewModel : ViewModel() {
//    // Trạng thái cho email, password, checkbox "Remember Me" và lỗi
//    val email = mutableStateOf("")
//    val password = mutableStateOf("")
//    val rememberMe = mutableStateOf(false)
//    val loginError = mutableStateOf<String?>(null)
//
//    private lateinit var sharedPreferences: SharedPreferences
//
//    // Khởi tạo SharedPreferences
//    fun initSharedPreferences(context: Context) {
//        sharedPreferences = context.getSharedPreferences("FruitHubPrefs", Context.MODE_PRIVATE)
//    }
//
//    // Hàm lưu token vào SharedPreferences
//    private fun saveToken(token: String) {
//        with(sharedPreferences.edit()) {
//            putString("auth_token", token)
//            if (rememberMe.value) {
//                putBoolean("remember_me", true)
//            }
//            apply()
//        }
//    }
//
//    // Hàm xử lý đăng nhập
//    fun doLogin(navigator: NavController) {
//        viewModelScope.launch {
//            try {
//                val response = ApiClient.authApi.login(
//                    LoginRequest(email.value, password.value)
//                )
//                // Đăng nhập thành công
//                if (response.token != null) {
//                    saveToken(response.token)
//                    loginError.value = null
//                    Log.d("LoginViewModel", "Initializing ViewModel")
////                    navigator.navigate("home") {
////                        popUpTo(navigator.graph.startDestinationId) {
////                            inclusive = true
////                        }
////                    }
//                } else {
//                    loginError.value = "Login failed: No token received"
//                }
//            } catch (e: HttpException) {
//                // Xử lý lỗi từ API
//                val errorBody = e.response()?.errorBody()?.string()
//                val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
//                loginError.value = when (e.code()) {
//                    401 -> errorResponse.error ?: "Sai thông tin đăng nhập"
//                    400 -> errorResponse.messenger ?: "Lỗi server nội bộ"
//                    else -> "Login failed: ${e.message()}"
//                }
//            } catch (e: Exception) {
//                loginError.value = "Login failed: ${e.message}"
//            }
//        }
//    }
//}
class LoginViewModel : ViewModel() {
    val email = mutableStateOf("")
    val password = mutableStateOf("")
    val rememberMe = mutableStateOf(false)
    val loginError = mutableStateOf<String?>(null)

    val forgotPassword = mutableStateOf(false)
    val resetCodeSent = mutableStateOf(false)
    val resetCode = mutableStateOf("")

    private fun saveToken(context: Context, token: String) {
        val sharedPreferences = context.getSharedPreferences("FruitHubPrefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("auth_token", token)
            if (rememberMe.value) {
                putBoolean("remember_me", true)
            }
            apply()
        }
        Log.d("LoginViewModel", "Token saved successfully: $token")
    }

    // Hàm kiểm tra xem người dùng đã đăng nhập hay chưa
    fun isLoggedIn(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences("FruitHubPrefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("auth_token", null)
        val rememberMe = sharedPreferences.getBoolean("remember_me", false)
        return token != null && rememberMe
    }

    fun requestPasswordReset() {
        viewModelScope.launch {
            try {
                val response = ApiClient.authApi.login(
                    LoginRequest(email.value, "", true)
                )
                if (response.message == "Mã xác nhận đã được gửi đến email của bạn") {
                    resetCodeSent.value = true
                    loginError.value = null
                    Log.d("LoginViewModel", "Mã xác nhận đã được gửi")
                } else {
                    loginError.value = "Yêu cầu đặt lại mật khẩu thất bại"
                }
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                loginError.value = when (e.code()) {
                    404 -> errorResponse.error ?: "Email không tồn tại"
                    500 -> errorResponse.error ?: "Lỗi server nội bộ"
                    else -> "Yêu cầu thất bại: ${e.message()}"
                }
            } catch (e: Exception) {
                loginError.value = "Yêu cầu thất bại: ${e.message}"
            }
        }
    }

    fun doLogin(navigator: NavController, context: Context) {
        viewModelScope.launch {
            try {
                Log.d("LoginViewModel", "Attempting login with email: ${email.value}")
                val response = ApiClient.authApi.login(
                    LoginRequest(email.value, password.value, forgotPassword.value)
                )
                Log.d("LoginViewModel", "API Response: $response")

                if (response.token != null) {
                    val sharedPreferences = context.getSharedPreferences("FruitHubPrefs", Context.MODE_PRIVATE)
                    val userId = response.user?._id
                    sharedPreferences.edit().putString("user_id", userId).apply()
                    saveToken(context, response.token)

                    Log.d("LoginViewModel", "Token saved successfully, navigating to home")
                    loginError.value = null
                    navigator.navigate("home") {
                        popUpTo(navigator.graph.startDestinationId) {
                            inclusive = true
                        }
                    }
                } else {
                    loginError.value = "Đăng nhập thất bại: Không nhận được token"
                    Log.e("LoginViewModel", "No token in response")
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Login failed", e)
                loginError.value = "Đăng nhập thất bại: ${e.message}"
            }
        }
    }
    fun logout(context: Context, navigator: NavController) {
        val sharedPreferences = context.getSharedPreferences("FruitHubPrefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            remove("auth_token")
            remove("remember_me")
            apply()
        }
        Log.d("LoginViewModel", "Logged out successfully")
        navigator.navigate("login") {
            popUpTo(navigator.graph.startDestinationId) {
                inclusive = true
            }
        }
    }
    fun resetForgotPasswordState() {
        forgotPassword.value = false
        resetCodeSent.value = false
        resetCode.value = ""
        loginError.value = null
    }

    fun resetLoginState() {
        email.value = ""
        password.value = ""
        rememberMe.value = false
        loginError.value = null
        forgotPassword.value = false
        resetCodeSent.value = false
        resetCode.value = ""
    }
}