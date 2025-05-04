package com.example.myapplication.viewModel



import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.myapplication.model.Category
import com.example.myapplication.model.ErrorResponse
import com.example.myapplication.model.LoginRequest
import com.example.myapplication.model.Product
import com.example.myapplication.network.User.ApiClient
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException
class ProductViewModel : ViewModel() {
    private val _categories = mutableStateOf<List<Category>>(emptyList())
    val categories: State<List<Category>> = _categories

    private val _products = mutableStateOf<List<Product>>(emptyList())
    val products: State<List<Product>> = _products

    private val _recommendedCombos = mutableStateOf<List<Product>>(emptyList())
    val recommendedCombos: State<List<Product>> = _recommendedCombos
    private val _productDetail = mutableStateOf<Product?>(null)
    val productDetail: State<Product?> = _productDetail
    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val authApi = ApiClient.authApi
    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error
    init {
        viewModelScope.launch {
            try {
                // Lấy danh sách danh mục
                val categoriesList = authApi.getCategories()
                _categories.value = categoriesList
                Log.d("ProductViewModel", "Categories: $categoriesList")

                // Tìm danh mục "Combo"
                val comboCategory = _categories.value.find { it.name.equals("Combo", ignoreCase = true) }
                Log.d("ProductViewModel", "Combo Category: $comboCategory")

                // Lấy tất cả sản phẩm trước
                val allProducts = authApi.getProducts()
                _products.value = allProducts
                Log.d("ProductViewModel", "All Products: $allProducts")

                // Lọc sản phẩm thuộc danh mục "Combo"
                if (comboCategory != null) {
                    _recommendedCombos.value = authApi.getProducts(comboCategory._id)
                    Log.d("ProductViewModel", "Recommended Combos: ${_recommendedCombos.value}")
                } else {
                    Log.d("ProductViewModel", "Combo Category not found")
                }
            } catch (e: Exception) {
                // Xử lý lỗi
            }
        }
    }
    fun ProductDetail(productId: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                val product = authApi.getProductDetail(productId)
                _productDetail.value = product
            } catch (e: Exception) {
                _error.value = "Lỗi khi tải chi tiết sản phẩm: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}