package com.example.myapplication.viewModel



import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.AddOrderItemRequest
import com.example.myapplication.model.OrderItem
import com.example.myapplication.network.User.ApiClient
import kotlinx.coroutines.launch

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

    fun clearMessages() {
        errorMessage = null
        successMessage = null
    }
}