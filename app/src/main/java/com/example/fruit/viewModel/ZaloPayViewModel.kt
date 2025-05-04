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