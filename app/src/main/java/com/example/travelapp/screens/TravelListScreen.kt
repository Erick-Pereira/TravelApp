package com.example.travelapp.screens

import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travelapp.database.AppDatabase
import com.example.travelapp.entity.User
import com.example.travelapp.factory.TravelListViewModelFactory
import com.example.travelapp.viewmodel.TravelListViewModel

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TravelListScreen(onEdit: (Int) -> Unit) {
    val ctx = LocalContext.current;
    val userDao = AppDatabase.getDatabase(ctx).userDao();
    val listViewModel: TravelListViewModel = viewModel(
        factory = TravelListViewModelFactory(userDao)
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
                    ItemUser(user,onEdit = {onEdit(it)})
                }
            }
        }
    }
}

@Composable
fun ItemUser(user: User,onEdit: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier
            .padding(8.dp)
            .pointerInput(Unit) {

                detectTapGestures (
                    onLongPress = {
                        onEdit(user.id)
                    }
                )
            })
        {
            Text(text = user.name)
        }
    }
}
