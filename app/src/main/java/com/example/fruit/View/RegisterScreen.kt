package com.rentify.user.app.view.auth

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fruit.R

import com.example.myapplication.viewModel.LoginViewModel
import com.example.myapplication.viewModel.RegisterViewModel

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavHostController, viewModel: RegisterViewModel) {
    // Khởi tạo SharedPreferences trong ViewModel
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.initSharedPreferences(context)
    }

    var firstName by remember { viewModel.firstName }
    var email by remember { viewModel.email }
    var password by remember { viewModel.password }
    var phoneNumber by remember { viewModel.phoneNumber }
    val registerError by remember { viewModel.registerError }
    val registerSuccess by remember { viewModel.registerSuccess }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(
                        RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = 0.dp,
                            bottomEnd = 0.dp
                        )
                    )
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = 0.dp,
                            bottomEnd = 0.dp
                        ),
                        clip = true
                    )
                    .background(Color(0xffffa451))
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.anhtraicay),
                    contentDescription = "Giỏ hàng",
                    modifier = Modifier.size(250.dp),
                )
                Image(
                    painter = painterResource(id = R.drawable.bong),
                    contentDescription = "Giỏ hàng",
                    modifier = Modifier.height(50.dp).width(300.dp),
                )
                Column(
                    modifier = Modifier
                        .clip(
                            RoundedCornerShape(
                                topStart = 26.dp,
                                topEnd = 26.dp,
                                bottomStart = 26.dp,
                                bottomEnd = 26.dp
                            )
                        )
                        .shadow(
                            elevation = 10.dp,
                            shape = RoundedCornerShape(
                                topStart = 26.dp,
                                topEnd = 26.dp,
                                bottomStart = 26.dp,
                                bottomEnd = 26.dp
                            ),
                            clip = true
                        )
                        .background(Color(0xFFE0E0E0))
                        .padding(20.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(20.dp))
                    // Trường nhập First Name
                    OutlinedTextField(
                        value = firstName,
                        onValueChange = { firstName = it },
                        label = { Text("Họ và tên") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color(0xfff08626),
                            focusedLabelColor = Color(0xfff08626),
                            cursorColor = Color(0xfff08626),
                            unfocusedBorderColor = Color.Gray,
                            unfocusedLabelColor = Color.Gray
                        )
                    )
                    // Trường nhập Email
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color(0xfff08626),
                            focusedLabelColor = Color(0xfff08626),
                            cursorColor = Color(0xfff08626),
                            unfocusedBorderColor = Color.Gray,
                            unfocusedLabelColor = Color.Gray
                        )
                    )
                    // Trường nhập Password
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Mật khẩu") },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color(0xfff08626),
                            focusedLabelColor = Color(0xfff08626),
                            cursorColor = Color(0xfff08626),
                            unfocusedBorderColor = Color.Gray,
                            unfocusedLabelColor = Color.Gray
                        )
                    )
                    // Trường nhập Phone Number
                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it },
                        label = { Text("Số điện thoại") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color(0xfff08626),
                            focusedLabelColor = Color(0xfff08626),
                            cursorColor = Color(0xfff08626),
                            unfocusedBorderColor = Color.Gray,
                            unfocusedLabelColor = Color.Gray
                        )
                    )

                    // Hiển thị lỗi nếu có
                    registerError?.let {
                        Text(
                            text = it,
                            color = Color.Red,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    // Hiển thị thông báo thành công nếu có
                    registerSuccess?.let {
                        Text(
                            text = it,
                            color = Color.Green,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }

                // Nút Đăng ký
                Button(
                    onClick = { viewModel.doRegister(navController) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Text("Đăng ký")
                }

                // Nút quay lại màn hình đăng nhập
                Text(
                    text = "Đã có tài khoản? Đăng nhập",
                    color = Color.Blue,
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .clickable {
                            navController.navigate("login")
                        }
                )
            }
        }
    )
}
@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    val navController = rememberNavController()
    RegisterScreen(viewModel = RegisterViewModel(), navController = navController)
}