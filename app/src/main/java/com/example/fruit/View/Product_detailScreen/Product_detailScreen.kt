package com.rentify.user.app.view.auth

import android.content.Context
import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
// ... các import material3 khác tương ứng
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fruit.R
import com.example.myapplication.model.Product
import com.example.myapplication.viewModel.OrderItemViewModel
import java.text.NumberFormat
import java.util.Locale
@Composable
fun ProductDetailScreen(navController: NavHostController, productId: String, product: Product) {
    val productViewModel: ProductViewModel = viewModel()
    val product by productViewModel.productDetail
    val isLoading by productViewModel.isLoading
    val error by productViewModel.error
    val context = LocalContext.current
    val orderItemViewModel: OrderItemViewModel = viewModel()
    var quantity by remember { mutableStateOf(1) }

    // Lấy userId từ SharedPreferences
    val sharedPreferences = context.getSharedPreferences("FruitHubPrefs", Context.MODE_PRIVATE)
    val userId = sharedPreferences.getString("user_id", null) ?: "default_user_id"
    // Hiển thị Toast khi có thông báo thành công hoặc lỗi
    // In log để kiểm tra userId
    Log.d("ProductDetailScreen", "Giá trị userId: $userId")
    LaunchedEffect(orderItemViewModel.successMessage, orderItemViewModel.errorMessage) {
        orderItemViewModel.successMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            orderItemViewModel.clearMessages()
        }
        orderItemViewModel.errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            orderItemViewModel.clearMessages()
        }
    }

    // Lấy chi tiết sản phẩm khi màn hình được tạo
    LaunchedEffect(productId) {
        productViewModel.ProductDetail(productId)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier

        ) {

            // Nút Back
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(Alignment.Start)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else if (error != null) {
                Text(
                    text = error ?: "Đã có lỗi xảy ra",
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Button(
                    onClick = { productViewModel.ProductDetail(productId) },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Thử lại")
                }
            } else if (product != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()

                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .background(Color(0xffffa451)),


                        ) {


                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.5f),
                            //     .background(Color(0xffffa451)),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {


                            AsyncImage(
                                model = product?.image_url?.firstOrNull()?.let {
                                    "http://192.168.0.104:3000/$it"
                                } ?: R.drawable.img,
                                contentDescription = product?.name,
                                modifier = Modifier
                                    .size(200.dp)
                                    .clip(CircleShape)

                            )
                        }
                        // Hình ảnh sản phẩm
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .clip(
                                    RoundedCornerShape(
                                        topStart = 16.dp,
                                        topEnd = 16.dp,
                                        bottomStart = 0.dp,
                                        bottomEnd = 0.dp
                                    )
                                )
                                .background(Color(0xffffffff))
                                .verticalScroll(rememberScrollState())
                                .padding(20.dp),
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = product?.name ?: "Unnamed Product",
                                    style = TextStyle(
                                        fontSize = 30.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xff27214d),
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(36.dp))

                            // Bộ đếm số lượng

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    modifier = Modifier,
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    IconButton(onClick = { if (quantity > 1) quantity-- }) {
                                        Image(
                                            painter = painterResource(id = R.drawable.cong),// Biểu tượng giỏ hàng
                                            contentDescription = "Giỏ hàng",

                                            )
                                    }


                                    Text(
                                        text = quantity.toString(),
                                        style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Medium),
                                        modifier = Modifier.padding(horizontal = 16.dp)

                                    )
                                    IconButton(onClick = { quantity++ }) {
                                        Image(
                                            painter = painterResource(id = R.drawable.tru),// Biểu tượng giỏ hàng
                                            contentDescription = "Giỏ hàng",

                                            )
                                    }
                                }
                                Text(
                                    text = (product!!.price * quantity).let { totalPrice ->
                                        val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
                                        formatter.format(totalPrice)
                                    },
                                    style = TextStyle(
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFF08626)
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(56.dp))

                            // Mô tả sản phẩm
                            Text(
                                text = "One Pack Contains:",
                                style = TextStyle(
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xff27214d),
                                )
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = product?.description ?: "No description available",
                                style = TextStyle(
                                    fontSize = 18.sp,
                                    color = Color(0xff27214d),
                                    fontWeight = FontWeight.W400,
                                )
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                        }
                        // Tên và giá
                    }
                }
            }
        }
        // Nút "Add to basket"
        Box(
            modifier = Modifier.fillMaxHeight()
                .padding(16.dp)
        ) {
            Button(
                onClick = {
                    product?.let {
                        orderItemViewModel.addOrderItem(
                            userId = userId,
                            quantity = quantity, // Giả sử số lượng mặc định là 1, bạn có thể thay đổi
                            price = it.price,
                            productId = productId)
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF08626)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Add to basket",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
            }}
    }}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProductDetailScreenPreview() {
    ProductDetailScreen(
        navController = rememberNavController(),
        productId = "sample_id",
        product = Product(
            _id = "sample_id",
            name = "Sample Product",
            description = "This is a sample product description.",
            price = 15000,
            image_url = listOf("sample_image.jpg"),
            category_id = "sample_category",
            quantity = 10
        ))
}