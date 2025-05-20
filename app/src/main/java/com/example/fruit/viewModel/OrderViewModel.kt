package com.example.myapplication.viewModel


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.AddOrderRequest
import com.example.myapplication.model.Order
import com.example.myapplication.network.User.ApiClient

import kotlinx.coroutines.launch


import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.myapplication.model.UpdateUserInfoRequest
import com.example.myapplication.model.UserInfo


class OrderViewModel : ViewModel() {
    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var successMessage by mutableStateOf<String?>(null)
        private set

    private val _orders = mutableStateOf<List<Order>>(emptyList())
    val orders: State<List<Order>> = _orders
    private val _userInfo = mutableStateOf<UserInfo?>(null)
    val userInfo: State<UserInfo?> = _userInfo
    fun fetchOrders(userId: String) {
        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null
                successMessage = null

                val response = ApiClient.authApi.getOrders(userId)
                if (response.success) {
                    _orders.value = response.data ?: emptyList()
                    Log.d("OrderViewModel", "User Info: ${_userInfo.value}")
                } else {
                    errorMessage = response.message
                }
            } catch (e: Exception) {
                errorMessage = "Lỗi khi lấy danh sách đơn hàng: ${e.message}"
                Log.e("OrderViewModel", "Fetch Orders: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }
    fun addOrder(
        context: Context,
        totalAmount: Int,
        paymentMethod: String,
        status: Int? = null,
        onUserNotLoggedIn: () -> Unit,
        onMissingUserInfo: () -> Unit // Callback khi thiếu thông tin
    ) {
        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null
                successMessage = null

                // Lấy userId từ SharedPreferences
                val sharedPreferences =
                    context.getSharedPreferences("FruitHubPrefs", Context.MODE_PRIVATE)
                val userId = sharedPreferences.getString("user_id", null) ?: "default_user_id"

                if (userId == "default_user_id") {
                    onUserNotLoggedIn()
                    return@launch
                }

                // Lấy thông tin người dùng
                val userInfo = fetchUserInfo(userId)
                if (userInfo == null || userInfo.delivery_address.isNullOrBlank() || userInfo.phone_number.isNullOrBlank()) {
                    onMissingUserInfo() // Gọi callback để yêu cầu nhập thông tin
                    return@launch
                }

                // Tạo request
                val request = AddOrderRequest(
                    user_id = userId,
                    total_amount = totalAmount,
                    status = status,
                    delivery_address = userInfo.delivery_address,
                    phone_number = userInfo.phone_number,
                    payment_method = paymentMethod
                )

                // Gọi API
                val response = ApiClient.authApi.addOrder(request)

                if (response.success) {
                    successMessage = response.message
                    response.data?.let { newOrder ->
                        _orders.value = _orders.value + newOrder
                    }
                } else {
                    errorMessage = response.message
                }
            } catch (e: Exception) {
                errorMessage = "Lỗi khi tạo Order: ${e.message}"
                Log.d("OrderViewModel", "Add Order: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    private suspend fun fetchUserInfo(userId: String): UserInfo? {
        return try {
            isLoading = true
            errorMessage = null
            successMessage = null

            val response = ApiClient.authApi.getUserInfo(userId)
            if (response.success) {
                _userInfo.value = response.data
                response.data
            } else {
                errorMessage = response.message
                null
            }
        } catch (e: Exception) {
            errorMessage = "Lỗi khi lấy thông tin người dùng: ${e.message}"
            Log.e("UserViewModel", "Fetch User Info: ${e.message}")
            null
        } finally {
            isLoading = false
        }
    }


    fun updateUserInfo(
        userId: String,
        deliveryAddress: String,
        phoneNumber: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null
                successMessage = null

                if (deliveryAddress.isBlank() || phoneNumber.isBlank()) {
                    onError("Vui lòng nhập đầy đủ địa chỉ và số điện thoại")
                    return@launch
                }

                val request = UpdateUserInfoRequest(
                    delivery_address = deliveryAddress,
                    phone_number = phoneNumber
                )

                val response = ApiClient.authApi.updateUserInfo(userId, request)
                if (response.success) {
                    successMessage = "Cập nhật thông tin người dùng thành công"
                    _userInfo.value =
                        UserInfo(delivery_address = deliveryAddress, phone_number = phoneNumber)
                    onSuccess()
                } else {
                    errorMessage = response.message ?: "Cập nhật thất bại"
                    onError(errorMessage ?: "Cập nhật thất bại")
                }
            } catch (e: Exception) {
                errorMessage = "Lỗi khi cập nhật thông tin người dùng: ${e.message}"
                Log.e("UserViewModel", "Update User Info: ${e.message}")
                onError(errorMessage ?: "Lỗi không xác định")
            } finally {
                isLoading = false
            }
        }
    }

    fun clearMessages() {
        errorMessage = null
        successMessage = null
    }

}
//    private suspend fun fetchUserInfo(userId: String): UserInfo? {
//        return try {
//            val response = ApiClient.authApi.getUserInfo(userId)
//            if (response.success) response.data else null
//        } catch (e: Exception) {
//            Log.e("OrderViewModel", "Error fetching user info: ${e.message}")
//            null
//        }
//    }
