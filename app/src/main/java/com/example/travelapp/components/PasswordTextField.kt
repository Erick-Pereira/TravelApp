package com.example.registeruser.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun PasswordTextField(
    value: String,
    label: String,
    onValueChange: (String) -> Unit,
    passwordToCompare: String? = null,
    isConfirmPassword: Boolean = false
) {
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isTouched by remember { mutableStateOf(false) }

    val isError =
        isTouched && value.isEmpty() || (passwordToCompare != null && value != passwordToCompare)

    OutlinedTextField(
        value = value,
        onValueChange = {
            onValueChange(it)
            if (!isTouched) isTouched = true
        },
        label = { Text(text = label) },
        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        isError = isError,
        trailingIcon = {
            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                Icon(
                    imageVector = if (isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                    contentDescription = "Toggle password visibility"
                )
            }
        },
        modifier = Modifier.fillMaxWidth(),
        supportingText = {
            if (isError) {
                Text(
                    text = if (value.isEmpty()) "Campo obrigatório" else "As senhas não coincidem",
                )
            }
        }
    )


}