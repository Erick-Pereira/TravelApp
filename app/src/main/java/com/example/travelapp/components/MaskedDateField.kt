package com.example.travelapp.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MaskedDateField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            // Permitir apenas números e barras
            val filteredValue = newValue.filter { it.isDigit() || it == '/' }
            onValueChange(filteredValue)
        },
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth()
    )
}