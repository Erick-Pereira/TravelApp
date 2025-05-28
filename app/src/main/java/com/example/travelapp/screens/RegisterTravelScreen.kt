package com.example.travelapp.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.registeruser.components.MyTextField
import com.example.travelapp.components.DatePickerField
import com.example.travelapp.components.EnumDropdown
import com.example.travelapp.database.AppDatabase
import com.example.travelapp.enums.EnumTravelType
import com.example.travelapp.factory.RegisterTravelListViewModelFactory
import com.example.travelapp.viewmodel.RegisterTravelListViewModel
import com.example.travelapp.ai.gerarRoteiroComGemini
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterTravelScreen(
    travelId: Int,
    onNavigateBack: () -> Unit,
    onNavigateToAdjustPrompt: (String, Int) -> Unit
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

    val coroutineScope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }
    var roteiro by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

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
            DatePickerField(
                label = "Data de Início",
                date = travelState.value.startDate?.let { dateFormat.format(it) } ?: "",
                onDateSelected = { newDate ->
                    registerTravelViewModel.onStartDateChange(newDate)
                }
            )
            DatePickerField(
                label = "Data de Término",
                date = travelState.value.endDate?.let { dateFormat.format(it) } ?: "",
                onDateSelected = { newDate ->
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
            Button(
                onClick = {
                    isLoading = true
                    coroutineScope.launch {
                        // Chamada real da API Gemini
                        roteiro = gerarRoteiroComGemini(
                            ctx,
                            destino = travelState.value.destination,
                            tipo = travelState.value.travelType.toString(),
                            dataInicio = travelState.value.startDate,
                            dataFim = travelState.value.endDate,
                            orcamento = travelState.value.budget
                        )
                        isLoading = false
                        showDialog = true
                    }
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Gerar Roteiro com IA")
            }

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Roteiro Gerado") },
                    text = { Text(roteiro) },
                    confirmButton = {
                        TextButton(onClick = {
                            // Salve o roteiro ou associe ao objeto viagem
                            showDialog = false
                        }) { Text("Aceitar") }
                    },
                    dismissButton = {
                        Row {
                            TextButton(onClick = { showDialog = false }) { Text("Rejeitar") }
                            TextButton(onClick = {
                                showDialog = false
                                onNavigateToAdjustPrompt(roteiro, travelId)
                            }) { Text("Ajustar") }
                        }
                    }
                )
            }

            if (isLoading) {
                // Exiba um indicador de carregamento se desejar
                Text("Gerando roteiro...")
            }
        }
    }
}