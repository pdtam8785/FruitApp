package com.rentify.user.app.view.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.fruit.R

import com.example.myapplication.View.HomeScreen.components.DrawerContent
import com.example.myapplication.View.HomeScreen.components.DrawerHeader
import com.example.myapplication.View.HomeScreen.components.ProductCategories
import com.example.myapplication.View.HomeScreen.components.RecommendedCombos
import com.example.myapplication.View.HomeScreen.components.SpecialCategoriesTabs
import com.example.myapplication.viewModel.LoginViewModel
import com.example.myapplication.viewModel.ProductViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenApp(navController: NavHostController, productViewModel: ProductViewModel = viewModel(), loginViewModel: LoginViewModel = viewModel()) {
    // Trạng thái của Drawer
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // ModalNavigationDrawer để tạo menu trượt
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(navController, loginViewModel = loginViewModel, drawerState = drawerState)
        },
        content = {
            Scaffold(
                modifier = Modifier.padding(horizontal = 20.dp),
                topBar = {
                    TopAppBar(
                        modifier = Modifier.background(color = Color(0xfffff666)),
                        title = { Text("") },
                        navigationIcon = {
                            IconButton(onClick = {
                                scope.launch { drawerState.open() }
                            }) {
                                Image(
                                    painter = painterResource(id = R.drawable.group),// Biểu tượng giỏ hàng
                                    contentDescription = "Giỏ hàng",
                                    modifier = Modifier.size(30.dp),
                                )
                            }
                        },

                        actions = { // Thêm actions để đặt biểu tượng giỏ hàng
                            IconButton(onClick = {
                                // Xử lý khi nhấn vào giỏ hàng (ví dụ: điều hướng đến màn hình giỏ hàng)
                                navController.navigate("order") // Thay "cart_screen" bằng route của bạn
                            }) {
                                Image(
                                    painter = painterResource(id = R.drawable.shoppingcart),// Biểu tượng giỏ hàng
                                    contentDescription = "Giỏ hàng",
                                    modifier = Modifier.size(40.dp),
                                )
                            }
                        }
                    )

                },
                content = { padding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)

                            // Cho phép cuộn nếu nội dung dài

                    ) {
                        Spacer(modifier = Modifier.height(16.dp))

                        // Tiêu đề "Hello Tony, What fruit salad combo do you want today?"
                        Text(
                            text = "Hello Tony,",
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.W400,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        )
                        Text(
                            text = "What fruit salad combo do you want today?",
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.W500,
                                color = MaterialTheme.colorScheme.onBackground
                            ),
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Row(    modifier = Modifier.padding(10.dp),  verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center) {
                            com.example.myapplication.View.HomeScreen.components.SearchBar( navController)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.group6),// Biểu tượng giỏ hàng
                                    contentDescription = "Giỏ hàng",
                                    modifier = Modifier.size(30.dp),
                                )
                            }
                        }

                        Column(
                            modifier = Modifier
                                .verticalScroll(rememberScrollState()) // Cho phép cuộn nếu nội dung dài

                        ) {
                            RecommendedCombos(productViewModel, navController)

                            Spacer(modifier = Modifier.height(24.dp))

                            SpecialCategoriesTabs(productViewModel, navController)

                            Spacer(modifier = Modifier.height(24.dp))
                        }
                        //  ProductCategories(productViewModel)
                    }
                }
            )
        }
    )
}



@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()
    HomeScreenApp(navController)
}



/////
