//package com.example.myapplication.View.HomeScreen.components
//
//import android.content.Context
//import android.util.Log
//import android.widget.Toast
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxHeight
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Email
//import androidx.compose.material.icons.filled.Home
//import androidx.compose.material.icons.filled.Share
//import androidx.compose.material.icons.filled.Star
//import androidx.compose.material3.Divider
//import androidx.compose.material3.DrawerState
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Icon
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.NavigationDrawerItem
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavController
//import androidx.navigation.NavHostController
//import com.example.myapplication.MainActivity
//import com.example.myapplication.viewModel.LoginViewModel
//import kotlinx.coroutines.launch
//
////package com.rentify.user.app.view.auth
//@Composable
//fun DrawerHeader() {
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .background(MaterialTheme.colorScheme.primary)
//            .padding(16.dp),
//        horizontalAlignment = Alignment.Start
//    ) {
//        // Avatar (hình ảnh người dùng)
//        Image(
//            painter = painterResource(id = com.example.myapplication.R.drawable.img), // Thay bằng ảnh của bạn
//            contentDescription = "User Avatar",
//            modifier = Modifier
//                .size(64.dp)
//                .clip(CircleShape)
//                .background(Color.Gray)
//        )
//
//        Spacer(modifier = Modifier.height(12.dp))
//
//        // Tên người dùng
//        Text(
//            text = "Robin Singla",
//            color = Color.White,
//            fontSize = 18.sp,
//            fontWeight = FontWeight.Bold
//        )
//
//        // Email
//        Text(
//            text = "robinsingla@gmail.com",
//            color = Color.White.copy(alpha = 0.7f),
//            fontSize = 14.sp
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//    }
//}
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun DrawerContent(navController: NavHostController, loginViewModel: LoginViewModel, drawerState: DrawerState) {
//    val scope = rememberCoroutineScope()
//    val context = LocalContext.current
//
//    Column(
//        modifier = Modifier
//            .fillMaxHeight()
//            .fillMaxWidth(0.7f)
//            .background(MaterialTheme.colorScheme.surface)
//            .verticalScroll(rememberScrollState())
//    ) {
//        DrawerHeader()
//
//        NavigationDrawerItem(
//            label = { Text("Home") },
//            selected = navController.currentDestination?.route == MainActivity.ROUTER.HOME.name,
//            onClick = {
//                scope.launch {
//                    drawerState.close()
//                    safeNavigate(navController, MainActivity.ROUTER.HOME.name, context)
//                }
//            },
//            icon = { Icon(Icons.Default.Home, "home") },
//            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
//        )
//
//        Divider(modifier = Modifier.padding(vertical = 8.dp))
//
//        NavigationDrawerItem(
//            label = { Text("man2") },
//            selected = navController.currentDestination?.route == MainActivity.ROUTER.man2.name,
//            onClick = {
//                scope.launch {
//                    drawerState.close()
//                    safeNavigate(navController, MainActivity.ROUTER.man2.name, context)
//                }
//            },
//            icon = { Icon(Icons.Default.Star, "man2") },
//            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
//        )
//    }
//}
//fun safeNavigate(navController: NavController, route: String, context: Context) {
//    try {
//        navController.navigate(route) {
//            popUpTo(MainActivity.ROUTER.HOME.name) { inclusive = true }
//            launchSingleTop = true
//        }
//    } catch (e: Exception) {
//        Log.e("NavigationError", "Error navigating to route: $route", e)
//        Toast.makeText(context, "Navigation error: ${e.message}", Toast.LENGTH_SHORT).show()
//    }
//}

//Box(
//modifier = Modifier
//.fillMaxSize()
//.background(Color(0xFFF5F5F5))
//) {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//    ) {
//        // Phần trên (chiếm 50%)
//        Box(modifier = Modifier.weight(1f)) {
//            // Nội dung phần trên (hình ảnh, tên sản phẩm)
//            Column(
//                modifier = Modifier.fillMaxSize(),
//                verticalArrangement = Arrangement.Center
//            ) {
//                AsyncImage(
//                    model = product?.image_url?.firstOrNull()?.let {
//                        "http://192.168.0.105:3000/$it"
//                    } ?: R.drawable.img,
//                    contentDescription = product?.name,
//                    modifier = Modifier
//                        .size(200.dp)
//                        .align(Alignment.CenterHorizontally)
//                )
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                Text(
//                    text = product?.name ?: "",
//                    modifier = Modifier.align(Alignment.CenterHorizontally),
//                    style = TextStyle(
//                        fontSize = 24.sp,
//                        fontWeight = FontWeight.Bold
//                    )
//                )
//            }
//        }
//
//        // Phần dưới (chiếm 50%)
//        Box(
//            modifier = Modifier
//                .weight(1f)
//                .background(
//                    color = Color.White,
//                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
//                )
//                .padding(16.dp)
//        ) {
//            Column {
//                // Bộ đếm số lượng và giá
//                    // Bộ đếm số lượng
//            var quantity by remember { mutableStateOf(1) }
//Text(
//                    text = product?.name ?: "",
//                    modifier = Modifier.align(Alignment.CenterHorizontally),
//                    style = TextStyle(
//                        fontSize = 24.sp,
//                        fontWeight = FontWeight.Bold
//                    )
//                )
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Row(
//                    modifier = Modifier,
//                    horizontalArrangement = Arrangement.Center,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    IconButton(onClick = { if (quantity > 1) quantity-- }) {
//                        Image(
//                            painter = painterResource(id = R.drawable.cong),// Biểu tượng giỏ hàng
//                            contentDescription = "Giỏ hàng",
//
//                            )
//                    }
//
//                    Text(
//                        text = quantity.toString(),
//                        style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Medium),
//                        modifier = Modifier.padding(horizontal = 16.dp)
//                    )
//
//                    IconButton(onClick = { quantity++ }) {
//                        Image(
//                            painter = painterResource(id = R.drawable.tru),// Biểu tượng giỏ hàng
//                            contentDescription = "Giỏ hàng",
//
//                            )
//                    }
//                }
//                Text(
//                    text = product!!.price?.let { price ->
//                        val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
//                        formatter.format(price)
//                    } ?: "N/A",
//                    style = TextStyle(
//                        fontSize = 20.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = Color(0xFFF08626)
//                    )
//                )
//            }
//
//            Spacer(modifier = Modifier.height(56.dp))
//
//                // Mô tả sản phẩm
//                Text(
//                    text = "One Pack Contains:",
//                    style = TextStyle(
//                        fontSize = 16.sp,
//                        fontWeight = FontWeight.Bold
//                    )
//                )
//
//                Text(
//                    text = product?.description ?: "",
//                    style = TextStyle(fontSize = 14.sp)
//                )
//            }
//        }
//    }
//
//    // Nút "Add to basket" cố định ở dưới
//    Button(
//        onClick = { /* ... */ },
//        modifier = Modifier
//            .align(Alignment.BottomCenter)
//            .fillMaxWidth()
//            .padding(16.dp)
//            .height(50.dp),
//        shape = RoundedCornerShape(8.dp),
//        colors = ButtonDefaults.buttonColors(
//            containerColor = Color(0xFFF08626)
//        )
//    ) {
//        Text("Add to basket")
//    }
//}