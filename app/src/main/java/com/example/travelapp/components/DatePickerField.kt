package com.example.travelapp.components

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import java.util.Calendar

@Composable
fun DatePickerField(label: String, date: String, onDateSelected: (String) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val datePickerDialog = DatePickerDialog(
        context,
        { _, selectedYear, selectedMonth, selectedDay ->
            onDateSelected("$selectedDay/${selectedMonth + 1}/$selectedYear")
        }, year, month, day
    )
    OutlinedTextField(
        value = date,
        onValueChange = {},
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        readOnly = true,
        trailingIcon = {
            Icon(
                Icons.Default.DateRange,
                contentDescription = "Select Date",
                modifier = Modifier.clickable { datePickerDialog.show() }
            )
        }
    )
}