import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.fruit.R
import com.example.myapplication.model.OrderItem
import com.example.myapplication.viewModel.OrderItemViewModel
import kotlinx.coroutines.launch

//
//package com.example.myapplication.viewmodel
//
//import android.app.Activity
//import android.content.Intent
//import android.util.Log
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.myapplication.model.CreateOrderRequest
//import com.example.myapplication.model.CreateOrderResponse
//import com.example.myapplication.network.User.ApiClient
//import com.example.myapplication.network.User.ZaloPayApiClient
//
//
//import com.example.myapplication.utils.ZaloPayUtils
//import com.google.gson.Gson
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.launch
//import retrofit2.HttpException
//import vn.zalopay.sdk.ZaloPaySDK
//import vn.zalopay.sdk.Environment
//import vn.zalopay.sdk.ZaloPayError
//import vn.zalopay.sdk.listeners.PayOrderListener
//
//class ZaloPayViewModel : ViewModel() {
//    private val _paymentState = MutableStateFlow<PaymentState>(PaymentState.Idle)
//    val paymentState: StateFlow<PaymentState> = _paymentState
//
//    fun initZaloPay(appId: Int = 2554, env: Environment = Environment.SANDBOX) {
//        Log.d("ZaloPayViewModel", "initZaloPay called with appId: $appId, env: $env")
//        ZaloPaySDK.init(appId, env)
//    }
//
//    fun createOrderAndPay(
//        activity: Activity,
//        amount: Long,
//        appId: Int = 2554,
//        macKey: String = "sdngKKJmqEMzvh5QQcdD2A9XBSKUNaYn",
//        uriScheme: String,
//        appUser: String = "user_demo"
//    ) {
//        Log.d("ZaloPayViewModel", "createOrderAndPay called with amount: $amount, appUser: $appUser")
//        viewModelScope.launch {
//            Log.d("ZaloPayViewModel", "Coroutine started")
//            _paymentState.value = PaymentState.Loading
//            try {
//                val appTransId = "zp_${System.currentTimeMillis()}"
//                val appTime = System.currentTimeMillis()
//                val data = "$appId|$appTransId|$appUser|$amount|$appTime|{}|[]"
//                Log.d("ZaloPayViewModel", "Data before MAC: $data")
//                val mac = ZaloPayUtils.createMac(data, macKey)
//                Log.d("ZaloPayViewModel", "Updated Data with mac_key: $data|$macKey")
//                Log.d("ZaloPayViewModel", "Updated MAC: $mac")
//
//                val orderResponse = createOrder(appId, appTransId, appUser, amount, mac)
//                Log.d("ZaloPayViewModel", "Order Response: $orderResponse")
//
//                if (orderResponse.return_code == 1) {
//                    Log.d("ZaloPayViewModel", "Order created successfully, zp_trans_token: ${orderResponse.zp_trans_token}")
//                    payWithZaloPaySDK(activity, orderResponse.zp_trans_token, uriScheme)
//                } else {
//                    Log.d("ZaloPayViewModel", "Order creation failed: ${orderResponse.return_message}")
//                    _paymentState.value = PaymentState.Error("Tạo đơn thất bại: ${orderResponse.return_message}")
//                }
//            } catch (e: Exception) {
//                Log.e("ZaloPayViewModel", "Error in createOrderAndPay", e)
//                _paymentState.value = PaymentState.Error("Lỗi tạo đơn: ${e.message}")
//            }
//        }
//    }
//
//    fun payWithZaloPaySDK(
//        activity: Activity,
//        zpTransToken: String,
//        uriScheme: String
//    ) {
//        Log.d("ZaloPayViewModel", "payWithZaloPaySDK called with zpTransToken: $zpTransToken, uriScheme: $uriScheme")
//        viewModelScope.launch {
//            _paymentState.value = PaymentState.Loading
//            ZaloPaySDK.getInstance().payOrder(
//                activity,
//                zpTransToken,
//                uriScheme,
//                object : PayOrderListener {
//                    override fun onPaymentSucceeded(transactionId: String, transToken: String, appTransID: String) {
//                        Log.d("ZaloPayViewModel", "Payment succeeded, transactionId: $transactionId")
//                        _paymentState.value = PaymentState.Success(transactionId)
//                    }
//
//                    override fun onPaymentCanceled(zpTransToken: String, appTransID: String) {
//                        Log.d("ZaloPayViewModel", "Payment canceled")
//                        _paymentState.value = PaymentState.Error("Người dùng hủy thanh toán")
//                    }
//
//                    override fun onPaymentError(zaloPayError: ZaloPayError, zpTransToken: String, appTransID: String) {
//                        Log.d("ZaloPayViewModel", "Payment error: ${zaloPayError.name}")
//                        val errorMsg = when (zaloPayError) {
//                            ZaloPayError.PAYMENT_APP_NOT_FOUND -> "Cần cài đặt ZaloPay"
//                            else -> "Lỗi: ${zaloPayError.name}"
//                        }
//                        _paymentState.value = PaymentState.Error(errorMsg)
//                    }
//                }
//            )
//        }
//    }
//
//    fun handlePaymentResult(intent: Intent?) {
//        Log.d("ZaloPayViewModel", "handlePaymentResult called")
//        ZaloPaySDK.getInstance().onResult(intent)
//    }
//
//    private suspend fun createOrder(
//        appId: Int,
//        appTransId: String,
//        appUser: String,
//        amount: Long,
//        mac: String
//    ): CreateOrderResponse {
//        Log.d("ZaloPayViewModel", "createOrder called with appId: $appId, appTransId: $appTransId")
//        val appTime = System.currentTimeMillis()
//        val request = CreateOrderRequest(
//            app_id = appId,
//            app_user = appUser,
//            app_time = appTime,
//            amount = amount,
//            app_trans_id = appTransId,
//            embed_data = "{}",
//            item = "[]",
//            description = "Thanh toán test ZaloPay",
//            mac = mac,
//            bank_code = ""
//        )
//        Log.d("ZaloPayViewModel", "CreateOrderRequest: $request")
//        try {
//            val response = ZaloPayApiClient.apiService.createOrder(request)
//            Log.d("ZaloPayViewModel", "Raw CreateOrderResponse: ${Gson().toJson(response)}")
//            return response
//        } catch (e: HttpException) {
//            Log.e("ZaloPayViewModel", "HTTP Error: ${e.code()} - ${e.message()}", e)
//            val errorBody = e.response()?.errorBody()?.string()
//            Log.e("ZaloPayViewModel", "Error Body: $errorBody")
//            throw e
//        } catch (e: Exception) {
//            Log.e("ZaloPayViewModel", "Unknown Error in createOrder", e)
//            throw e
//        }
//    }
//}
//
//// Đặt trong file riêng (vd: PaymentState.kt) hoặc cùng file
//sealed class PaymentState {
//    object Idle : PaymentState()
//    object Loading : PaymentState()
//    data class Success(val transactionId: String) : PaymentState()
//    data class Error(val message: String) : PaymentState()
//}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderScreenApp(navController: NavController) {
    val context = LocalContext.current
    val orderItemViewModel: OrderItemViewModel = viewModel()
    val orderItems by orderItemViewModel.orderItems
    val isLoading by remember { mutableStateOf(orderItemViewModel.isLoading) }
    val errorMessage by remember { mutableStateOf(orderItemViewModel.errorMessage) }
    //  val zaloPayViewModel: ZaloPayViewModel = viewModel()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var showPaymentDialog by remember { mutableStateOf(false) }
    // Gọi API để lấy danh sách OrderItem
    LaunchedEffect(Unit) {
        orderItemViewModel.getOrderItems(context) {
            // Điều hướng đến màn hình đăng nhập nếu user chưa đăng nhập
            navController.navigate("login") {
                popUpTo(navController.graph.startDestinationId) {
                    inclusive = true
                }
            }
        }
    }

    val totalPrice = orderItems.sumOf { it.price * it.quantity }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "My Basket",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF08626)
                )
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
                            showPaymentDialog = true // Mở dialog khi nhấp nút
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF08626)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .height(48.dp)
                        .width(150.dp)
                ) {
                    Text(
                        text = "Checkout",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
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
            } else if (errorMessage != null) {
                Text(
                    text = errorMessage ?: "Đã có lỗi xảy ra",
                    color = Color.Red,
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
                    items(orderItems) { orderItem ->
                        OrderItemCard(orderItem = orderItem)
                    }
                }
            }
        }
    }

    if (showPaymentDialog) {
        AlertDialog(
            onDismissRequest = { showPaymentDialog = false },
            title = {
                Text(text = "Chọn phương thức thanh toán")
            },
            text = {
                Column {
                    Text(text = "Tổng cộng: ₦$totalPrice")
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showPaymentDialog = false
                        try {

                        } catch (e: Exception) {
                            Log.e("OrderScreenApp", "Error during ZaloPay checkout", e)
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Lỗi thanh toán ZaloPay: ${e.message}")
                            }
                        }
                    }
                ) {
                    Text("Thanh toán với ZaloPay")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showPaymentDialog = false
                        // Xử lý thanh toán khi nhận hàng (có thể thêm logic tùy chỉnh)
                        Toast.makeText(context, "Thanh toán khi nhận hàng (chưa triển khai)", Toast.LENGTH_SHORT).show()
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Thanh toán khi nhận hàng (chưa triển khai)")
                        }
                    }
                ) {
                    Text("Thanh toán khi nhận hàng")
                }
            }
        )
    }
}

@Composable
fun OrderItemCard(orderItem: OrderItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = orderItem.product_Order.image_url?.firstOrNull()?.let {
                    "http://192.168.0.104:3000/$it" }
                    ?:  R.drawable.img,
                contentDescription = orderItem.product_Order.name,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = orderItem.product_Order.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${orderItem.quantity} packs",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Text(
                text = "₦${orderItem.price * orderItem.quantity}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}