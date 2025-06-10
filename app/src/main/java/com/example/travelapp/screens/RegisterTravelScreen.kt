package com.example.travelapp.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterTravelScreen(
    travelId: Int,
    onNavigateBack: () -> Unit
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
    var isLoading by remember { mutableStateOf(false) }
    var showScriptScreen by remember { mutableStateOf(false) }
    var aiSuggestion by remember { mutableStateOf("") }
    var showAdjustPromptDialog by remember { mutableStateOf(false) }
    var adjustPrompt by remember { mutableStateOf("") }

    Scaffold {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
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

            if (travelState.value.script.isNotBlank()) {
                Text(
                    text = "Roteiro:",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 16.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 100.dp, max = 300.dp)
                        .padding(8.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = travelState.value.script,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

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
                },
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()
            ) {
                Text(text = if (travelId == -1) "Adicionar Viagem" else "Salvar Alterações")
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = {
                    isLoading = true
                    coroutineScope.launch {
                        try {
                            val sugestao = gerarRoteiroComGemini(
                                ctx,
                                travelState.value.destination,
                                travelState.value.travelType.toString(),
                                travelState.value.startDate,
                                travelState.value.endDate,
                                travelState.value.budget
                            )
                            aiSuggestion = sugestao
                            showScriptScreen = true
                        } catch (e: Exception) {
                            aiSuggestion = "Erro ao gerar sugestão: ${e.message}"
                            showScriptScreen = true
                        } finally {
                            isLoading = false
                        }
                    }
                },
                enabled = !isLoading,
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(top = 8.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text("Sugestão de Roteiro")
            }

            if (showScriptScreen) {
                AlertDialog(
                    onDismissRequest = { showScriptScreen = false },
                    title = { Text("Roteiro Gerado") },
                    text = {
                        Box(
                            modifier = Modifier
                                .heightIn(max = 300.dp)
                                .verticalScroll(rememberScrollState())
                        ) {
                            Text(aiSuggestion)
                        }
                    },
                    confirmButton = {
                        Row {
                            TextButton(
                                onClick = {
                                    registerTravelViewModel.updateTravelRoteiro(travelId, aiSuggestion)
                                    registerTravelViewModel.loadTravel(travelId)
                                    showScriptScreen = false
                                }
                            ) { Text("Aceitar") }
                            Spacer(modifier = Modifier.width(8.dp))
                            TextButton(
                                onClick = {
                                    adjustPrompt = ""
                                    showScriptScreen = false
                                    showAdjustPromptDialog = true
                                }
                            ) { Text("Ajustar") }
                            Spacer(modifier = Modifier.width(8.dp))
                            TextButton(
                                onClick = {
                                    showScriptScreen = false
                                }
                            ) { Text("Rejeitar") }
                        }
                    }
                )
            }

            if (showAdjustPromptDialog) {
                AlertDialog(
                    onDismissRequest = { showAdjustPromptDialog = false },
                    title = { Text("Ajustar Prompt") },
                    text = {
                        Column {
                            MyTextField(
                                label = "Prompt",
                                value = adjustPrompt,
                                onValueChange = { adjustPrompt = it }
                            )
                        }
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                isLoading = true
                                showAdjustPromptDialog = false
                                coroutineScope.launch {
                                    try {
                                        val sugestao = gerarRoteiroComGemini(
                                            ctx,
                                            adjustPrompt.ifBlank { travelState.value.destination },
                                            travelState.value.travelType.toString(),
                                            travelState.value.startDate,
                                            travelState.value.endDate,
                                            travelState.value.budget
                                        )
                                        aiSuggestion = sugestao
                                        showScriptScreen = true
                                    } catch (e: Exception) {
                                        aiSuggestion = "Erro ao gerar sugestão: ${e.message}"
                                        showScriptScreen = true
                                    } finally {
                                        isLoading = false
                                    }
                                }
                            }
                        ) { Text("Gerar Novo Roteiro") }
                    },
                    dismissButton = {
                        TextButton(onClick = { showAdjustPromptDialog = false }) {
                            Text("Cancelar")
                        }
                    }
                )
            }
        }
    }
}