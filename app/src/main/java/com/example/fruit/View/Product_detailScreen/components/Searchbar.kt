//package com.example.myapplication.View.HomeScreen.components
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.text.BasicTextField
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Search
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Icon
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavController
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun SearchBar(navController: NavController) {
//    var searchText by remember { mutableStateOf("") }
//
//    Row(
//        modifier = Modifier
//            .fillMaxWidth(0.8f)
//            .background(
//                color = Color(0xFFF0F4F8), // Màu nền nhạt giống trong hình
//                shape = RoundedCornerShape(12.dp)
//            )
//            .padding(horizontal = 16.dp, vertical = 12.dp),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        // Biểu tượng kính lúp
//        Icon(
//            imageVector = Icons.Default.Search,
//            contentDescription = "Search",
//            tint = Color.Gray,
//            modifier = Modifier.size(24.dp)
//        )
//
//        Spacer(modifier = Modifier.width(8.dp))
//
//        // Trường nhập tìm kiếm
//        BasicTextField(
//            value = searchText,
//            onValueChange = { searchText = it },
//            modifier = Modifier.weight(1f),
//            textStyle = TextStyle(
//                color = Color.Gray,
//                fontSize = 16.sp
//            ),
//            decorationBox = { innerTextField ->
//                if (searchText.isEmpty()) {
//                    Text(
//                        text = "Search for fruit salad combos",
//                        color = Color.Gray,
//                        fontSize = 16.sp
//                    )
//                }
//                innerTextField()
//            }
//        )
//
//
//
//
//    }
//}
