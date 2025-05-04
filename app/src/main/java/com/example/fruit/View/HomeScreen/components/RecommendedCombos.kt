package com.example.myapplication.View.HomeScreen.components

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import coil.compose.AsyncImage
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.fruit.R

import com.example.myapplication.model.Product
import com.example.myapplication.viewModel.LoginViewModel
import com.example.myapplication.viewModel.ProductViewModel
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@Composable
fun RecommendedCombos(productViewModel: ProductViewModel, navController: NavHostController) {
    val recommendedCombos by productViewModel.recommendedCombos

    if (recommendedCombos.isNotEmpty()) {
        Column {
            Text(
                text = "Recommended Combo",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W500,
                    color = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(recommendedCombos) { product ->
                    ProductItem(product = product, navController = navController)
                }
            }
        }
    }
}
@Composable
fun ProductCategories(productViewModel: ProductViewModel, navController: NavHostController) {
    val categories by productViewModel.categories
    val products by productViewModel.products
    Column {
        categories.forEach { category ->
            val categoryProducts = products.filter { it.category_id == category._id }
            if (categoryProducts.isNotEmpty()) {
                Column {
                    Text(
                        text = category.name,
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        ),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(categoryProducts) { product ->
                            ProductItem(product = product, navController = navController)
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
fun ProductItem(product: Product,navController: NavHostController) {
    Box(modifier = Modifier.width(150.dp)) {
        // Thêm icon ở góc trái phía trên (vd: icon yêu thích)
        IconButton(
            onClick = { /* Xử lý khi bấm vào icon */ },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(4.dp)
                .size(24.dp)
                .zIndex(1f) // Đảm bảo icon hiển thị trên cùng
        ) {
            Icon(
                painter = painterResource(id = R.drawable.vector), // Thay bằng icon của bạn
                contentDescription = "Yêu thích",
                tint = Color(0xFFF08626) // Màu cam
            )
        }

        Card(
            modifier = Modifier.fillMaxWidth()
                .clickable { navController.navigate("product_detail/${product._id}") },
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFF8F8F8)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = product.image_url.firstOrNull()?.let {
                        "http://192.168.0.105:3000/$it"
                    } ?: run {
                        R.drawable.img
                    },
                    contentDescription = product.name ?: "Product image",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = product.name ?: "Unnamed Product",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xff27214d)
                    ),
                    modifier = Modifier.padding(bottom = 4.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val priceFormatted = product.price?.let { price ->
                        val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
                        formatter.format(price)
                    } ?: "N/A"

                    Text(
                        text = priceFormatted,
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xfff08626)
                        )
                    )

                    IconButton(
                        onClick = { /* Xử lý thêm vào giỏ hàng */ },
                        modifier = Modifier.size(30.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.froup10),
                            contentDescription = "Giỏ hàng"
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun SpecialCategoriesTabs(productViewModel: ProductViewModel, navController: NavHostController) {
    val categories by productViewModel.categories
    val products by productViewModel.products
    var selectedTabIndex by remember { mutableStateOf(0) }

    // Lọc danh mục không phải "Combo"
    val specialCategories = categories.filter { !it.name.equals("Combo", ignoreCase = true) }

    if (specialCategories.isNotEmpty()) {
        Column {
            // Tab danh mục
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                itemsIndexed(specialCategories) { index, category ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        modifier = Modifier
                            .background(
                                color = if (selectedTabIndex == index) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else Color.Transparent,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        text = {
                            Text(
                                text = category.name,
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                                    color = if (selectedTabIndex == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                                )
                            )
                        }
                    )
                }
            }

            // Hiển thị sản phẩm của danh mục được chọn
            val selectedCategory = specialCategories.getOrNull(selectedTabIndex)
            if (selectedCategory != null) {
                val categoryProducts = products.filter { it.category_id == selectedCategory._id }

                if (categoryProducts.isNotEmpty()) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(categoryProducts) { product ->
                            ProductItem(product = product, navController = navController)
                        }
                    }
                } else {
                    Text(
                        text = "Không có sản phẩm nào trong danh mục này",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}
//@Composable
//fun ProductList(products: List<Product>) {
//    if (products.isNotEmpty()) {
//        LazyRow(
//            horizontalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//            items(products) { product ->
//                ProductItem(product = product)
//            }
//        }
//    }
//}