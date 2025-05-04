//package com.rentify.user.app.view.auth
//
//import android.os.Bundle
//import android.widget.Toast
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.focusable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.navigationBarsPadding
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.statusBarsPadding
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonColors
//import androidx.compose.material3.Checkbox
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextField
//import androidx.compose.material3.TextFieldDefaults
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.draw.shadow
//import androidx.compose.ui.focus.onFocusChanged
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.text.input.PasswordVisualTransformation
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.lifecycle.ViewModelProvider
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavController
//import androidx.navigation.NavHostController
//import androidx.navigation.Navigator
//import androidx.navigation.compose.rememberNavController
//import com.example.myapplication.R
//
//import com.example.myapplication.viewModel.LoginViewModel
//
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun LoginScreenApp(navController: NavHostController, viewModel: LoginViewModel) {
//    // Khởi tạo SharedPreferences trong ViewModel
//    val context = LocalContext.current
////    LaunchedEffect(Unit) {
////        viewModel.initSharedPreferences(context)
////
////    }
//
//    val email = viewModel.email.value
//    val password = viewModel.password.value
//    var rememberMe by remember { viewModel.rememberMe }
//    val loginError by remember { viewModel.loginError }
//    val forgotPassword by remember { viewModel.forgotPassword }
//    val resetCodeSent by remember { viewModel.resetCodeSent }
//    val resetCode by remember { viewModel.resetCode }
//    val isEmailValid = email.isNotBlank()
//    val isPasswordValid = password.isNotBlank()
//    val isFormValid = isEmailValid && isPasswordValid
//    Scaffold(
//        modifier = Modifier.fillMaxSize(),
//        content = { padding ->
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//
//                    .clip(
//                        RoundedCornerShape(
//                            topStart = 16.dp,
//                            topEnd = 16.dp,
//                            bottomStart = 0.dp,
//                            bottomEnd = 0.dp
//                        )
//                    )
//                    .shadow(
//                        elevation = 8.dp,
//                        shape = RoundedCornerShape(
//                            topStart = 16.dp,
//                            topEnd = 16.dp,
//                            bottomStart = 0.dp,
//                            bottomEnd = 0.dp
//                        ),
//                        clip = true)
//
//                    .background(Color(0xffffa451))
//                    .padding(horizontal = 16.dp),
//                verticalArrangement = Arrangement.Center,
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Image(
//                    painter = painterResource(id = R.drawable.anhtraicay),// Biểu tượng giỏ hàng
//                    contentDescription = "Giỏ hàng",
//                    modifier = Modifier.size(250.dp),
//                )
//                Image(
//                    painter = painterResource(id = R.drawable.bong),// Biểu tượng giỏ hàng
//                    contentDescription = "Giỏ hàng",
//                    modifier = Modifier.height(50.dp).width(300.dp),
//                )
//                Column(
//                    modifier = Modifier
//                        .clip(
//                            RoundedCornerShape(
//                                topStart = 26.dp,
//                                topEnd = 26.dp,
//                                bottomStart = 26.dp,
//                                bottomEnd = 26.dp
//                            )
//                        )
//                        .shadow(
//                            elevation = 10.dp,
//                            shape = RoundedCornerShape(
//                                topStart = 26.dp,
//                                topEnd = 26.dp,
//                                bottomStart = 26.dp,
//                                bottomEnd = 26.dp
//                            ),
//                            clip = true)
//                        .background(Color(0xFFE0E0E0))
//                        .padding(20.dp),
//                    verticalArrangement = Arrangement.Center,
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ){
//                    Spacer( modifier = Modifier.height(20.dp))
//                    // Trường nhập Email
//                    OutlinedTextField(
//                        value = email,
//                        onValueChange = {  viewModel.email.value  = it },
//                        label = { Text("Email") },
//                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(bottom = 16.dp),
//                        colors = TextFieldDefaults.outlinedTextFieldColors(
//                            focusedBorderColor = Color(0xfff08626),
//                            focusedLabelColor = Color(0xfff08626),
//                            cursorColor = Color(0xfff08626),
//                            unfocusedBorderColor = Color.Gray,
//                            unfocusedLabelColor = Color.Gray
//                        )
//                    )
//                    // Trường nhập Password
//                    OutlinedTextField(
//                        value = password,
//                        onValueChange = { viewModel.password.value = it },
//                        label = { Text("Password") },
//                        visualTransformation = PasswordVisualTransformation(),
//                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(bottom = 16.dp),
//                        colors = TextFieldDefaults.outlinedTextFieldColors(
//                            focusedBorderColor = Color(0xfff08626), // Màu viền khi focus
//                            focusedLabelColor = Color(0xfff08626),  // Màu nhãn khi focus
//                            cursorColor = Color(0xfff08626),        // Màu con trỏ khi focus
//                            unfocusedBorderColor = Color.Gray,      // Màu viền khi không focus (giữ nguyên màu xám mặc định)
//                            unfocusedLabelColor = Color.Gray        // Màu nhãn khi không focus (giữ nguyên màu xám mặc định)
//                        )
//                    )
//
//                    // Checkbox "Remember Me"
//                    // Checkbox "Remember Me"
//                    if (!forgotPassword) {
//                        Row(
//                            modifier = Modifier.fillMaxWidth(),
//                            verticalAlignment = Alignment.CenterVertically
//                        ) {
//                            Checkbox(
//                                checked = rememberMe,
//                                onCheckedChange = { rememberMe = it }
//                            )
//                            Text("Ghi nhớ tôi")
//                        }
//                    }
//
//                    // Hiển thị lỗi nếu có
//                    loginError?.let {
//                        Text(
//                            text = it,
//                      //      color = MaterialTheme.colors.error,
//                            modifier = Modifier.padding(vertical = 8.dp)
//                        )
//                    }
//                    if (resetCodeSent) {
//                        Text(
//                            text = "Mã xác nhận đã được gửi đến email của bạn",
//                   //         color = MaterialTheme.colors.primary,
//                            modifier = Modifier.padding(vertical = 8.dp)
//                        )
//                    }
//
//                    // Nút "Quên mật khẩu?" hoặc "Hủy"
//                    Text(
//                        text = if (forgotPassword) "Hủy" else "Quên mật khẩu?",
//                 //       color = MaterialTheme.colors.primary,
//                        modifier = Modifier
//                            .padding(vertical = 8.dp)
//                            .clickable {
//                                if (forgotPassword) {
//                                    viewModel.resetForgotPasswordState()
//                                } else {
//                                    viewModel.forgotPassword.value = true
//                                    viewModel.requestPasswordReset()
//                                }
//                            }
//                    )
//                }
//                // Tiêu đề
//
//
//                // Nút Login
//                if (!forgotPassword) {
//                    Button(
//                        onClick = { viewModel.doLogin(navController, context) },
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//                        Text("Đăng nhập")
//                    }
//                }
//            }
//        }
//    )
//}
//@Preview(showBackground = true)
//@Composable
//fun LoginScreenPreview() {
//    val navController = rememberNavController()
//    LoginScreenApp(viewModel = LoginViewModel(), navController = navController)
//}