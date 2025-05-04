package com.example.fruit

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.fruit.ui.theme.FruitTheme
import com.example.myapplication.model.Product
import com.example.myapplication.viewModel.LoginViewModel
import com.example.myapplication.viewModel.RegisterViewModel
import com.rentify.user.app.view.auth.HomeScreenApp
import com.rentify.user.app.view.auth.LoginScreenApp
import com.rentify.user.app.view.auth.OrderScreenApp
import com.rentify.user.app.view.auth.ProductDetailScreen
import com.rentify.user.app.view.auth.RegisterScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainNavigation()
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    @Preview(showBackground = true)
    @Composable
    fun MainNavigation() {
        val context = LocalContext.current
        val loginViewModel: LoginViewModel = viewModel()
        val startDestination = remember {
            if (loginViewModel.isLoggedIn(context)) "home" else "Register"
        }
        val navController = rememberNavController()
        Scaffold { padding ->
            NavHost(
                navController = navController,
                startDestination = startDestination,
                modifier = Modifier.padding(padding)
            ) {
                composable(ROUTER.LOGIN.name) {
                    LoginScreenApp(viewModel = LoginViewModel(), navController = navController)
                }
                composable(ROUTER.REGISTER.name) {
                    RegisterScreen(viewModel = RegisterViewModel(), navController = navController)
                }
                composable(ROUTER.HOME.name) {
                    HomeScreenApp(navController = navController)
                }
                composable(ROUTER.ORDER.name) {
                    OrderScreenApp(navController = navController)
                }
                composable(
                    route = "product_detail/{productId}",
                    arguments = listOf(navArgument("productId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val productId = backStackEntry.arguments?.getString("productId") ?: ""
                    ProductDetailScreen(navController = navController, productId = productId,product = Product(
                        _id = "sample_id",
                        name = "Sample Product",
                        description = "This is a sample product description.",
                        price = 15000,
                        image_url = listOf("sample_image.jpg"),
                        category_id = "sample_category",
                        quantity = 10)
                    )
                }

            }
        }
    }
    enum class ROUTER {
        LOGIN,
        HOME,
        REGISTER,
        ORDER
    }
}