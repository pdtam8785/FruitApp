package com.example.myapplication.viewModel



import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.myapplication.model.ErrorResponse
import com.example.myapplication.model.LoginRequest
import com.example.myapplication.model.ResetPasswordRequest
import com.example.myapplication.model.VerifyResetCodeRequest
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
    val role = mutableStateOf<String?>(null) // Thêm trạng thái cho role

    val forgotPassword = mutableStateOf(false)
    val resetCodeSent = mutableStateOf(false)
    val resetCode = mutableStateOf("")
    val newPassword = mutableStateOf("") // Thêm trạng thái cho mật khẩu mới
    val resetCodeVerified = mutableStateOf(false) // Trạng thái xác minh mã
    private fun saveToken(context: Context, token: String, userId: String, role: String) {
        val sharedPreferences = context.getSharedPreferences("FruitHubPrefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("auth_token", token)
            putString("user_id", userId)
            putString("user_role", role) // Lưu role vào SharedPreferences
            if (rememberMe.value) {
                putBoolean("remember_me", true)
            }
            apply()
        }
        Log.d("LoginViewModel", "Token saved successfully: $token, Role: $role")
    }

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
                    loginError.value = response.error ?: "Yêu cầu đặt lại mật khẩu thất bại"
                }
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                loginError.value =   when (e.code()) {
                    404 -> errorResponse.error ?: "Email không tồn tại"
                    403 -> errorResponse.error ?: "Tài khoản của bạn đã bị cấm"
                    500 -> errorResponse.error ?: "Lỗi server nội bộ"
                    else -> "Yêu cầu thất bại: ${e.message()}"
                }
            } catch (e: Exception) {
                loginError.value = "Yêu cầu thất bại: ${e.message}"
            }
        }
    }
    fun verifyResetCode() {
        viewModelScope.launch {
            try {
                val response = ApiClient.authApi.verifyResetCode(
                    VerifyResetCodeRequest(email.value, resetCode.value)
                )
                if (response.message == "Mã xác nhận hợp lệ") {
                    resetCodeVerified.value = true
                    loginError.value = null
                    Log.d("LoginViewModel", "Mã xác nhận hợp lệ")
                } else {
                    loginError.value = response.error ?: "Xác minh mã thất bại"
                }
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                loginError.value = when (e.code()) {
                    400 -> errorResponse.error ?: "Mã xác nhận không hợp lệ hoặc đã hết hạn"
                    500 -> errorResponse.error ?: "Lỗi server nội bộ"
                    else -> "Xác minh mã thất bại: ${e.message()}"
                }
            } catch (e: Exception) {
                loginError.value = "Xác minh mã thất bại: ${e.message}"
            }
        }
    }

    fun resetPassword(navController: NavController) {
        viewModelScope.launch {
            try {
                val response = ApiClient.authApi.resetPassword(
                    ResetPasswordRequest(email.value, resetCode.value, newPassword.value)
                )
                if (response.message == "Đổi mật khẩu thành công") {
                    loginError.value = null
                    resetForgotPasswordState()
                    Log.d("LoginViewModel", "Đổi mật khẩu thành công")
                    navController.navigate("login") {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                } else {
                    loginError.value = response.error ?: "Đổi mật khẩu thất bại"
                }
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                loginError.value = when (e.code()) {
                    400 -> errorResponse.error ?: "Mã xác nhận không hợp lệ hoặc đã hết hạn"
                    500 -> errorResponse.error ?: "Lỗi server nội bộ"
                    else -> "Đổi mật khẩu thất bại: ${e.message()}"
                }
            } catch (e: Exception) {
                loginError.value = "Đổi mật khẩu thất bại: ${e.message}"
            }
        }
    }
    fun doLogin(navigator: NavController, context: Context) {
        viewModelScope.launch {
            try {
                // Hiển thị Toast thay vì Log
                Toast.makeText(context, "Đang đăng nhập...", Toast.LENGTH_SHORT).show()

                val response = ApiClient.authApi.login(
                    LoginRequest(email.value, password.value, forgotPassword.value)
                )

                if (response.token != null && response.user != null) {
                    if (response.user.role == "baned") {
                        Toast.makeText(context, "Tài khoản của bạn đã bị cấm", Toast.LENGTH_LONG).show()
                        return@launch
                    }

                    saveToken(context, response.token, response.user._id, response.user.role)
                    role.value = response.user.role
                    Toast.makeText(context, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show()

                    // Điều hướng
                    val destination = if (response.user.role == "admin") "admin_dashboard" else "home"
                    navigator.navigate(destination) {
                        popUpTo(navigator.graph.startDestinationId) { inclusive = true }
                    }
                } else {
                    Toast.makeText(context, "Đăng nhập thất bại: Thiếu token hoặc thông tin người dùng", Toast.LENGTH_LONG).show()
                }
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                val errorMessage = when (e.code()) {
                    401 -> errorResponse.error ?: "Email hoặc mật khẩu không đúng"
                    403 -> errorResponse.error ?: "Tài khoản bị cấm/chưa xác nhận"
                    400 -> errorResponse.error ?: "Dữ liệu không hợp lệ"
                    else -> "Lỗi hệ thống: ${e.message()}"
                }
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Toast.makeText(context, "Lỗi: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun logout(context: Context, navigator: NavController) {
        val sharedPreferences = context.getSharedPreferences("FruitHubPrefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            remove("auth_token")
            remove("user_id")
            remove("user_role")
            remove("remember_me")
            apply()
        }
        Log.d("LoginViewModel", "Logged out successfully")
        role.value = null // Xóa role khi đăng xuất
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
        role.value = null
    }
}