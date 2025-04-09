package com.example.travelapp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travelapp.database.AppDatabase
import com.example.travelapp.entity.User
import com.example.travelapp.factory.RegisterUserListViewModelFactory
import com.example.travelapp.viewmodel.RegisterUserListViewModel

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun RegisterUserListScreen() {
    val ctx = LocalContext.current;
    val userDao = AppDatabase.getDatabase(ctx).userDao();
    val listViewModel : RegisterUserListViewModel = viewModel(
        factory = RegisterUserListViewModelFactory(userDao)
    )
    val userState = listViewModel.users.collectAsState(initial = emptyList())
    Scaffold(
        topBar =
        {
            TopAppBar(title = { Text(text = "Titulo") })
        }) {
        Column(modifier = Modifier.padding(it)) {
            LazyColumn {
                items(items = userState.value) { user ->
                    ItemUser(user)
                }
            }
        }
    }
}

@Composable
fun ItemUser(user: User) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = user.name)
        }
    }
}