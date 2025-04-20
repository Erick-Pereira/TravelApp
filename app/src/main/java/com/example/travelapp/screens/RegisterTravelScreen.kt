package com.example.travelapp.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.registeruser.components.MyTextField
import com.example.travelapp.components.EnumDropdown
import com.example.travelapp.components.MaskedDateField
import com.example.travelapp.database.AppDatabase
import com.example.travelapp.enums.EnumTravelType
import com.example.travelapp.factory.RegisterTravelListViewModelFactory
import com.example.travelapp.viewmodel.RegisterTravelListViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterTravelScreen(
    travelId: Int, onNavigateBack: () -> Unit
) {
    val ctx = LocalContext.current
    val travelDao = AppDatabase.getDatabase(ctx).travelDao()
    val registerTravelViewModel: RegisterTravelListViewModel = viewModel(
        factory = RegisterTravelListViewModelFactory(travelDao)
    )
    val travelState = registerTravelViewModel.uiState.collectAsState()

    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    LaunchedEffect(travelId) {
        if (travelId != -1) {
            registerTravelViewModel.loadTravel(travelId)
        }
    }

    Scaffold {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MyTextField(label = "Destino",
                value = travelState.value.destination,
                onValueChange = { registerTravelViewModel.onDestinationChange(it) })
            EnumDropdown(label = "Tipo de Viagem",
                options = listOf("Lazer", "Negócios"),
                selectedOption = when (travelState.value.travelType) {
                    EnumTravelType.LAZER -> "Lazer"
                    EnumTravelType.NEGOCIOS -> "Negócios"
                },
                onOptionSelected = {
                    val enumValue =
                        if (it == "Lazer") EnumTravelType.LAZER else EnumTravelType.NEGOCIOS
                    registerTravelViewModel.onTravelTypeChange(enumValue)
                })
            MaskedDateField(
                label = "Data de Início",
                value = travelState.value.startDate?.let { dateFormat.format(it) } ?: "",
                onValueChange = { newDate ->
                    registerTravelViewModel.onStartDateChange(newDate)
                }
            )
            MaskedDateField(
                label = "Data de Término",
                value = travelState.value.endDate?.let { dateFormat.format(it) } ?: "",
                onValueChange = { newDate ->
                    registerTravelViewModel.onEndDateChange(newDate)
                }
            )
            MyTextField(label = "Orçamento",
                value = travelState.value.budget.toString(),
                onValueChange = { registerTravelViewModel.onBudgetChange(it.toDouble()) })
            if (travelState.value.errorMessage.isNotBlank()) {
                Text(
                    text = travelState.value.errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(8.dp)
                )
            }
            Button(
                onClick = {
                    registerTravelViewModel.saveTravel()
                }, modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(text = if (travelId == -1) "Adicionar Viagem" else "Salvar Alterações")
            }
            LaunchedEffect(travelState.value.isSaved) {
                if (travelState.value.isSaved) {
                    Toast.makeText(ctx, "Viagem salva com sucesso!", Toast.LENGTH_SHORT).show()
                    onNavigateBack()
                }
            }
        }
    }
}