package com.example.myapplication.viewModel



import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.AddOrderItemRequest

import com.example.myapplication.model.OrderItem
import com.example.myapplication.network.User.ApiClient
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException

class OrderItemViewModel : ViewModel() {
    var isLoading by mutableStateOf(false)
        private set


    var errorMessage by mutableStateOf<String?>(null)
        private set

    var successMessage by mutableStateOf<String?>(null)
        private set

    private val _orderItems = mutableStateOf<List<OrderItem>>(emptyList())
    val orderItems: State<List<OrderItem>> = _orderItems

    fun addOrderItem(userId: String, quantity: Int, price: Int, productId: String) {
        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null
                successMessage = null
                val totalPrice = price * quantity
                val request = AddOrderItemRequest(
                    user_id = userId,
                    quantity = quantity,
                    price = totalPrice,
                    product_id = productId
                )

                val response = ApiClient.authApi.addOrderItem(request)

                if (response.success) {
                    successMessage = response.message
                } else {
                    errorMessage = response.message
                }
            } catch (e: Exception) {
                errorMessage = "Lỗi: ${e.message}"
                Log.d("OrderItemViewModel", "All Order: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    fun getOrderItems(context: Context, onUserNotLoggedIn: () -> Unit) {
        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null
                successMessage = null

                // Lấy userId từ SharedPreferences
                val sharedPreferences = context.getSharedPreferences("FruitHubPrefs", Context.MODE_PRIVATE)
                val userId = sharedPreferences.getString("user_id", null) ?: "default_user_id"

                if (userId == "default_user_id") {
                    onUserNotLoggedIn() // Gọi callback để điều hướng đến màn hình đăng nhập
                    return@launch
                }

                // Gọi API với user_id
                val response = ApiClient.authApi.getOrderItems(userId = userId)

                if (response.success) {
                    _orderItems.value = response.data ?: emptyList()
                } else {
                    errorMessage = response.message
                }
            } catch (e: Exception) {
                errorMessage = "Lỗi: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
    fun deleteOrderItem(orderItemId: String) {
        viewModelScope.launch {
            try {
                Log.d("OrderItemViewModel", "Bắt đầu xóa order item với ID: $orderItemId")
                isLoading = true
                errorMessage = null
                successMessage = null

                // Log trạng thái trước khi gọi API
                Log.d("OrderItemViewModel", "Số lượng order items trước khi xóa: ${_orderItems.value.size}")
                Log.d("OrderItemViewModel", "Order items hiện tại: ${_orderItems.value.map { it._id }}")

                val response = ApiClient.authApi.deleteOrderItem(orderItemId)

                // Log phản hồi từ API
                Log.d("OrderItemViewModel", "Phản hồi từ API: ${response.toString()}")

                if (response.success) {
                    successMessage = response.message
                    Log.d("OrderItemViewModel", "Xóa thành công, message: ${response.message}")

                    // Cập nhật danh sách local
                    val newList = _orderItems.value.filter { it._id != orderItemId }
                    Log.d("OrderItemViewModel", "Số lượng order items sau khi xóa: ${newList.size}")
                    _orderItems.value = newList
                } else {
                    errorMessage = response.message
                    Log.e("OrderItemViewModel", "Lỗi từ server: ${response.message}")
                }
            } catch (e: Exception) {
                errorMessage = "Lỗi khi xóa: ${e.message}"
                Log.e("OrderItemViewModel", "Lỗi trong deleteOrderItem", e)

                // Log chi tiết exception
                when (e) {
                    is SocketTimeoutException -> {
                        Log.e("OrderItemViewModel", "Timeout khi kết nối đến server")
                    }
                    is ConnectException -> {
                        Log.e("OrderItemViewModel", "Không thể kết nối đến server")
                    }
                    is HttpException -> {
                        Log.e("OrderItemViewModel", "Lỗi HTTP: ${e.code()}")
                        Log.e("OrderItemViewModel", "Response body: ${e.response()?.errorBody()?.string()}")
                    }
                    is JsonSyntaxException -> {
                        Log.e("OrderItemViewModel", "Lỗi parse JSON")
                    }
                    else -> {
                        Log.e("OrderItemViewModel", "Lỗi không xác định", e)
                    }
                }
            } finally {
                isLoading = false
                Log.d("OrderItemViewModel", "Kết thúc quá trình xóa")
            }
        }
    }

    fun clearMessages() {
        errorMessage = null
        successMessage = null
    }
}