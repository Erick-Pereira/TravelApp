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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travelapp.database.AppDatabase
import com.example.travelapp.entity.Travel
import com.example.travelapp.factory.RegisterTravelListViewModelFactory
import com.example.travelapp.viewmodel.RegisterTravelListViewModel
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TravelListScreen(onEdit: (Int) -> Unit, onAddTravel: () -> Unit) {
    val ctx = LocalContext.current
    val travelDao = AppDatabase.getDatabase(ctx).travelDao()
    val listViewModel: RegisterTravelListViewModel = viewModel(
        factory = RegisterTravelListViewModelFactory(travelDao)
    )
    val travelState = listViewModel.travels.collectAsState(initial = emptyList())
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Lista de Viagens") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddTravel) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Adicionar Viagem")
            }
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            LazyColumn {
                items(items = travelState.value, key = { travel -> travel.id }) { travel ->
                    val dismissState = rememberDismissState(
                        confirmValueChange = { dismissValue ->
                            if (dismissValue == DismissValue.DismissedToEnd || dismissValue == DismissValue.DismissedToStart) {
                                coroutineScope.launch {
                                    listViewModel.deleteTravel(travel)
                                }
                                true
                            } else {
                                false
                            }
                        }
                    )
                    SwipeToDismiss(
                        state = dismissState,
                        directions = setOf(DismissDirection.EndToStart, DismissDirection.StartToEnd),
                        background = {},
                        dismissContent = {
                            ItemTravel(travel, onEdit = { travelId ->
                                onEdit(travelId)
                            })
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ItemTravel(travel: Travel, onEdit: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        onEdit(travel.id)
                    }
                )
            },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = travel.destination)
            Text(text = "Tipo: ${travel.travelType}")
            Text(text = "Orçamento: R$ ${travel.budget}")
        }
    }
}
