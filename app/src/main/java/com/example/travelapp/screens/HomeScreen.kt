package com.example.travelapp.screens

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun HomeScreen(onNavigateTo: (String) -> Unit,
               onBack: () -> Unit) {

    Button(onClick = { onBack() }) {
        Text(text = "Sign out")
    }
}