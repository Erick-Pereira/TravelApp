package com.example.travelapp.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.registeruser.components.ErrorDialog
import com.example.registeruser.components.MyTextField
import com.example.registeruser.components.PasswordTextField
import com.example.travelapp.R
import com.example.travelapp.database.AppDatabase
import com.example.travelapp.factory.LoginUserViewModelFactory
import com.example.travelapp.viewmodel.LoginUserViewModel

@Composable
fun LoginUserScreen(
    onNavigateTo: (String) -> Unit
) {
    val ctx = LocalContext.current
    val userDao = AppDatabase.getDatabase(ctx).userDao()
    val loginUserViewModel: LoginUserViewModel = viewModel(
        factory = LoginUserViewModelFactory(userDao)
    )

    Scaffold {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logotipo),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(150.dp)
                    .padding(bottom = 16.dp),
                contentScale = ContentScale.Fit
            )
            LoginUserFields(loginUserViewModel, onNavigateTo)
            Button(onClick = { onNavigateTo("RegisterUserScreen") }) {
                Text(text = "Sign up")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginUserFields(
    loginUserViewModel: LoginUserViewModel,
    onNavigateTo: (String) -> Unit
) {
    val loginUser = loginUserViewModel.uiState.collectAsState()
    val ctx = LocalContext.current

    MyTextField(
        label = "Login",
        value = loginUser.value.login,
        onValueChange = {
            loginUserViewModel.onLoginChange(it)
        }
    )
    PasswordTextField(
        value = loginUser.value.password,
        onValueChange = {
            loginUserViewModel.onPasswordChange(it)
        },
        label = "Password"
    )
    Button(
        onClick = {
            loginUserViewModel.login()
        },
        modifier = Modifier.padding(top = 16.dp)
    ) {
        Text(text = "Log in")
    }
    if (loginUser.value.errorMessage.isNotBlank()) {
        ErrorDialog(
            error = loginUser.value.errorMessage,
            onDismissRequest = { loginUserViewModel.cleanErrorMessage() }
        )
    }
    Spacer(modifier = Modifier.height(16.dp))

    // LaunchedEffect to handle navigation on successful login
    LaunchedEffect(loginUser.value.isLoggedIn) {
        if (loginUser.value.isLoggedIn) {
            Toast.makeText(ctx, "Usuário logado com sucesso", Toast.LENGTH_SHORT).show()
            onNavigateTo("LoggedScreen")
        }
    }
}