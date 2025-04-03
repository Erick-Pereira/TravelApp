package com.example.registeruser.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.example.registeruser.components.ErrorDialog
import com.example.registeruser.components.MyTextField
import com.example.registeruser.components.PasswordTextField
import com.example.travelapp.database.AppDatabase
import com.example.travelapp.screens.RegisterUserViewModelFactory

@Composable
fun RegisterUserScreen(
    onNavigateTo: (String) -> Unit
) {
    val ctx = LocalContext.current
    val userDao =     AppDatabase.getDatabase(ctx).userDao()
    val registerUserViewModel: RegisterUserViewModel = viewModel(
        factory = RegisterUserViewModelFactory(userDao)
    )

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RegisterUserFields(registerUserViewModel, onNavigateTo)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterUserFields(
    registerUserViewModel: RegisterUserViewModel,
    onNavigateTo: (String) -> Unit
) {
    val registerUser = registerUserViewModel.uiState.collectAsState()

    MyTextField(
        label = "User",
        value = registerUser.value.user,
        onValueChange = {
            registerUserViewModel.onUserChange(it)
        }
    )
    MyTextField(
        label = "Name",
        value = registerUser.value.name,
        onValueChange = {
            registerUserViewModel.onNameChange(it)
        }
    )
    MyTextField(
        label = "Email",
        value = registerUser.value.email,
        onValueChange = {
            registerUserViewModel.onEmailChange(it)
        }
    )
    PasswordTextField(
        value = registerUser.value.password,
        onValueChange = {
            registerUserViewModel.onPasswordChange(it)
        },
        label = "Password"
    )
    PasswordTextField(
        value = registerUser.value.confirmPassword,
        passwordToCompare = registerUser.value.password,
        onValueChange = {
            registerUserViewModel.onConfirmPasswordChange(it)
        },
        label = "Confirm Password"
    )

    val ctx = LocalContext.current
    Button(
        onClick = {
            registerUserViewModel.register()
        },
        modifier = Modifier.padding(top = 16.dp)
    ) {
        Text(text = "Register User")
    }
    if (registerUser.value.errorMessage.isNotBlank()) {
        ErrorDialog(
            error = registerUser.value.errorMessage,
            onDismissRequest = { registerUserViewModel.cleanDisplayValues() }
        )
    }
    LaunchedEffect(registerUser.value.isSaved){
        if(registerUser.value.isSaved){
            Toast.makeText(ctx,"User registered",Toast.LENGTH_SHORT).show()
            registerUserViewModel.cleanDisplayValues();
            onNavigateTo("LoginUserScreen")
        }
    }
}
