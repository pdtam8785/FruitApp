package com.rentify.user.app.view.auth

import OrderItemCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

import androidx.compose.ui.platform.LocalContext


import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

import com.example.myapplication.viewModel.LoginViewModel
import com.example.myapplication.viewModel.OrderItemViewModel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.viewModel.ProductViewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll

import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search

import androidx.compose.material3.AlertDialog


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
// ... các import material3 khác tương ứng
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fruit.R
import com.example.myapplication.model.OrderItem
import com.example.myapplication.model.Product
//import com.example.myapplication.viewmodel.PaymentState


//import com.example.myapplication.viewmodel.ZaloPayViewModel
import kotlinx.coroutines.launch
import androidx.compose.material.DismissValue
import androidx.compose.material.DismissDirection
import androidx.compose.material.rememberDismissState
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.text.input.KeyboardType
import com.example.myapplication.model.UserInfo
import com.example.myapplication.network.User.ApiClient
import com.example.myapplication.viewModel.OrderViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun OrderScreenApp(navController: NavController) {
    val context = LocalContext.current
    val orderViewModel: OrderViewModel = viewModel()
    val orderItemViewModel: OrderItemViewModel = viewModel()
    val orderItems by orderItemViewModel.orderItems
    val isLoading by orderItemViewModel::isLoading
    val errorMessage by orderItemViewModel::errorMessage
    val successMessage by orderItemViewModel::successMessage

    val orderSuccessMessage by orderViewModel::successMessage
    val orderErrorMessage by orderViewModel::errorMessage
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var showPaymentDialog by remember { mutableStateOf(false) }

    // State cho địa chỉ và số điện thoại
    var deliveryAddress by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }

    // Lấy userId từ SharedPreferences
    val sharedPreferences = context.getSharedPreferences("FruitHubPrefs", Context.MODE_PRIVATE)
    val userId = sharedPreferences.getString("user_id", null) ?: "default_user_id"

    // Lấy thông tin từ userModel khi khởi động
    LaunchedEffect(userId) {
        if (userId != "default_user_id") {
            val userResponse = fetchUserInfo(userId)
            deliveryAddress = userResponse?.delivery_address ?: ""
            phoneNumber = userResponse?.phone_number ?: ""
        }
    }

    // Gọi API để lấy danh sách OrderItem
    LaunchedEffect(Unit) {
        orderItemViewModel.getOrderItems(context) {
            navController.navigate("login") {
                popUpTo(navController.graph.startDestinationId) {
                    inclusive = true
                }
            }
        }
    }

    // Hiển thị thông báo từ OrderItemViewModel
    LaunchedEffect(errorMessage, successMessage) {
        errorMessage?.let { error ->
            Log.d("OrderScreenApp", "Showing Snackbar for error: $error")
            coroutineScope.launch {
                snackbarHostState.showSnackbar(error)
            }
        }
        successMessage?.let { success ->
            Log.d("OrderScreenApp", "Showing Snackbar for success: $success")
            coroutineScope.launch {
                snackbarHostState.showSnackbar(success)
            }
        }
        orderItemViewModel.clearMessages()
    }

    // Hiển thị thông báo từ OrderViewModel
    LaunchedEffect(orderErrorMessage, orderSuccessMessage) {
        orderErrorMessage?.let { error ->
            Log.d("OrderScreenApp", "Showing Snackbar for order error: $error")
            coroutineScope.launch {
                snackbarHostState.showSnackbar(error)
            }
        }
        orderSuccessMessage?.let { success ->
            Log.d("OrderScreenApp", "Showing Snackbar for order success: $success")
            coroutineScope.launch {
                snackbarHostState.showSnackbar(success)
                navController.navigate("orderSuccess") {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = false
                    }
                }
            }
        }
        orderViewModel.clearMessages()
    }

    val totalPrice = orderItems.sumOf { it.price * it.quantity }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Basket", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFF08626))
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total: ₦${totalPrice}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Button(
                    onClick = {
                        if (orderItems.isEmpty()) {
                            Log.d("OrderScreenApp", "Cart is empty")
                            Toast.makeText(context, "Giỏ hàng trống, không thể thanh toán", Toast.LENGTH_SHORT).show()
                        } else {
                            Log.d("OrderScreenApp", "Checkout clicked, totalPrice: $totalPrice, orderItems: ${orderItems.size}")
                            showPaymentDialog = true // Chỉ mở dialog, không gọi addOrder ngay
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF08626)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(48.dp).width(150.dp)
                ) {
                    Text("Checkout", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF5F5F5))
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )
            } else if (orderItems.isEmpty()) {
                Text(
                    text = "Giỏ hàng trống",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    items(orderItems, key = { it._id }) { orderItem ->
                        val dismissState = rememberDismissState(
                            confirmStateChange = {
                                if (it == DismissValue.DismissedToStart) {
                                    coroutineScope.launch {
                                        try {
                                            orderItemViewModel.deleteOrderItem(orderItem._id)
                                            snackbarHostState.showSnackbar("Đã xóa sản phẩm")
                                        } catch (e: Exception) {
                                            snackbarHostState.showSnackbar("Lỗi khi xóa: ${e.message}")
                                        }
                                    }
                                }
                                true
                            }
                        )
                        SwipeToDismiss(
                            state = dismissState,
                            directions = setOf(DismissDirection.EndToStart),
                            background = {
                                val direction = dismissState.dismissDirection
                                if (direction == DismissDirection.EndToStart) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(Color.Red)
                                            .padding(horizontal = 20.dp)
                                            .width(LocalConfiguration.current.screenWidthDp.dp * 0.2f),
                                        contentAlignment = Alignment.CenterEnd
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete",
                                            tint = Color.White,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }
                            },
                            dismissContent = { OrderItemCard(orderItem) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        )
                    }
                }
            }
        }
    }

    // Dialog thanh toán với trường nhập địa chỉ và số điện thoại
    if (showPaymentDialog) {
        AlertDialog(
            onDismissRequest = { showPaymentDialog = false },
            title = { Text("Thanh toán đơn hàng") },
            text = {
                Column {
                    Text(text = "Tổng cộng: ₦$totalPrice")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = deliveryAddress,
                        onValueChange = { deliveryAddress = it },
                        label = { Text("Địa chỉ giao hàng") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = deliveryAddress.isBlank()
                    )
                    if (deliveryAddress.isBlank()) {
                        Text(
                            text = "Vui lòng nhập địa chỉ giao hàng",
                            color = Color.Red, // Thay thế MaterialTheme.colorScheme.error
                            style = TextStyle(fontSize = 12.sp), // Thay thế MaterialTheme.typography.bodySmall
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it },
                        label = { Text("Số điện thoại") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        isError = phoneNumber.isBlank()
                    )
                    if (phoneNumber.isBlank()) {
                        Text(
                            text = "Vui lòng nhập số điện thoại",
                            color = Color.Red, // Thay thế MaterialTheme.colorScheme.error
                            style = TextStyle(fontSize = 12.sp), // Thay thế MaterialTheme.typography.bodySmall
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (deliveryAddress.isBlank() || phoneNumber.isBlank()) {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Vui lòng nhập đầy đủ địa chỉ và số điện thoại")
                            }
                        } else {
                            orderViewModel.updateUserInfo(
                                userId = userId,
                                deliveryAddress = deliveryAddress,
                                phoneNumber = phoneNumber,
                                onSuccess = {
                                    showPaymentDialog = false // Đóng dialog
                                    orderViewModel.addOrder(
                                        context = context,
                                        totalAmount = totalPrice,
                                        paymentMethod = "ZaloPay", // Đặt trực tiếp tại đây
                                        status = 0,
                                        onUserNotLoggedIn = {
                                            navController.navigate("login") {
                                                popUpTo(navController.graph.startDestinationId) {
                                                    inclusive = true
                                                }
                                            }
                                        },
                                        onMissingUserInfo = { showPaymentDialog = true }
                                    )
                                },
                                onError = { errorMsg ->
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar(errorMsg)
                                    }
                                }
                            )
                        }
                    }
                ) {
                    Text("Thanh toán với ZaloPay")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        if (deliveryAddress.isBlank() || phoneNumber.isBlank()) {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Vui lòng nhập đầy đủ địa chỉ và số điện thoại")
                            }
                        } else {
                            orderViewModel.updateUserInfo(
                                userId = userId,
                                deliveryAddress = deliveryAddress,
                                phoneNumber = phoneNumber,
                                onSuccess = {
                                    showPaymentDialog = false // Đóng dialog
                                    orderViewModel.addOrder(
                                        context = context,
                                        totalAmount = totalPrice,
                                        paymentMethod = "Pay on Delivery", // Đặt trực tiếp tại đây
                                        status = 0,
                                        onUserNotLoggedIn = {
                                            navController.navigate("login") {
                                                popUpTo(navController.graph.startDestinationId) {
                                                    inclusive = true
                                                }
                                            }
                                        },
                                        onMissingUserInfo = { showPaymentDialog = true }
                                    )
                                },
                                onError = { errorMsg ->
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar(errorMsg)
                                    }
                                }
                            )
                        }
                    }
                ) {
                    Text("Thanh toán khi nhận hàng")
                }
            }
        )
    }
}
@Preview(showBackground = true)
@Composable
fun OrderScreenPreview() {
    val navController = rememberNavController()
    OrderScreenApp(navController)
}
suspend fun fetchUserInfo(userId: String): UserInfo? {
    return try {
        val response = ApiClient.authApi.getUserInfo(userId)
        if (response.success) response.data else null
    } catch (e: Exception) {
        Log.e("OrderScreenApp", "Error fetching user info: ${e.message}")
        null
    }
}
