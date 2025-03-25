package com.example.registeruser.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent

@Composable
fun MyTextField(value: String, onValueChange: (String) -> Unit, label: String) {
    var isTouched = remember {
        mutableStateOf(false)
    };
    var focusRequester = remember {
        FocusRequester()
    };
    OutlinedTextField(value = value, onValueChange = {
        onValueChange(it)
        isTouched.value = true
    }, singleLine = true, label = {
        Text(text = label)
    },
        isError = value.isBlank() && isTouched.value,
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .onFocusEvent {
                if (it.hasFocus) {
                    isTouched.value = true;
                }
            },
        supportingText = {
            if (isTouched.value && value.isBlank()) {
                Text(text = "Field ${label} is required")
            }
        }
    )
}