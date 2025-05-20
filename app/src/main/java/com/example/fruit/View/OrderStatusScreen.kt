package com.rentify.user.app.view.auth

import android.content.Context
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.model.Order
import com.example.myapplication.viewModel.OrderViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun OrderStatusTabsScreen(navController: NavController) {
    val orderViewModel: OrderViewModel = viewModel()
    val orders by orderViewModel::orders
    val isLoading by orderViewModel::isLoading
    val errorMessage by orderViewModel::errorMessage
    val successMessage by orderViewModel::successMessage
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Lấy userId từ SharedPreferences
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("FruitHubPrefs", Context.MODE_PRIVATE)
    val userId = sharedPreferences.getString("user_id", null) ?: "default_user_id"

    // Danh sách các tab và trạng thái tương ứng
    val tabs = listOf(
        "Chờ xác nhận" to 0,
        "Chờ lấy hàng" to 1,
        "Chờ giao hàng" to 2,
        "Đã giao hàng" to 3,
        "Trả hàng" to 4,
        "Đã hủy" to 5
    )

    // Trạng thái của Pager
    val pagerState = rememberPagerState(pageCount = { tabs.size })

    // Hiển thị thông báo từ OrderViewModel
    LaunchedEffect(errorMessage, successMessage) {
        errorMessage?.let { error ->
            coroutineScope.launch {
                snackbarHostState.showSnackbar(error)
            }
        }
        successMessage?.let { success ->
            coroutineScope.launch {
                snackbarHostState.showSnackbar(success)
            }
        }
        orderViewModel.clearMessages()
    }

    // Lấy danh sách đơn hàng khi màn hình được tạo
    LaunchedEffect(userId) {
        if (userId != "default_user_id") {
            orderViewModel.fetchOrders(userId)
        } else {
            navController.navigate("login") {
                popUpTo(navController.graph.startDestinationId) {
                    inclusive = true
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Đơn hàng của tôi",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFF08626))
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF5F5F5))
        ) {
            // TabRow để hiển thị các tab
            ScrollableTabRow(
                selectedTabIndex = pagerState.currentPage,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White),
                edgePadding = 0.dp,
                containerColor = Color.White,
                contentColor = Color(0xFFF08626)
            ) {
                tabs.forEachIndexed { index, (title, _) ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = {
                            Text(
                                text = title,
                                fontSize = 14.sp,
                                fontWeight = if (pagerState.currentPage == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            // HorizontalPager để lướt ngang giữa các tab
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                val status = tabs[page].second
                // Sử dụng orders.value và xử lý null
                val filteredOrders = orders.value?.filter { it.status == status } ?: emptyList()

                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .wrapContentWidth(Alignment.CenterHorizontally)
                    )
                } else if (filteredOrders.isEmpty()) {
                    Text(
                        text = "Không có đơn hàng ở trạng thái này",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .wrapContentWidth(Alignment.CenterHorizontally),
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                    ) {
                        items(filteredOrders, key = { order -> order._id }) { order ->
                            OrderCard(order = order)
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OrderCard(order: Order) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Mã đơn hàng: ${order._id}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Tổng tiền: ₦${order.total_amount}",
                fontSize = 14.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Địa chỉ: ${order.delivery_address}",
                fontSize = 14.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Phương thức thanh toán: ${order.payment_method}",
                fontSize = 14.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Ngày tạo: ${order.created_at}",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OrderStatusTabsScreenPreview() {
    val navController = rememberNavController()
    OrderStatusTabsScreen(navController)
}

