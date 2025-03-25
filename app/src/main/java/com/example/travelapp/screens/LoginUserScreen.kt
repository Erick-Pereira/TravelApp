package com.example.travelapp.screens

import android.widget.Space
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
import com.example.travelapp.viewmodel.LoginUserViewModel

@Composable
fun LoginUserScreen(
    onNavigateTo : (String) -> Unit
){

    val loginUserViewModel: LoginUserViewModel = viewModel()

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
            LoginUserFields(loginUserViewModel,onNavigateTo)
            Button(onClick = { onNavigateTo("RegisterUserScreen")}) {
                Text(text = "Sign up")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginUserFields(loginUserViewModel: LoginUserViewModel,onNavigateTo: (String) -> Unit) {
    val registerUser = loginUserViewModel.uiState.collectAsState()
    MyTextField(label = "Login", value =
    registerUser.value.login, onValueChange = {
        loginUserViewModel.onLoginChange(it)
    })
    PasswordTextField(value = registerUser.value.password, onValueChange = {
        loginUserViewModel.onPasswordChange(it)
    }, label = "Password")  
    val ctx = LocalContext.current
    Button(
        onClick = {
            if(loginUserViewModel.login()){
                Toast.makeText(ctx, "Usuario logado com sucesso", Toast.LENGTH_SHORT).show()
                onNavigateTo("MenuScreen")
            }
        },
        modifier = Modifier.padding(top = 16.dp)
    ) {
        Text(text = "Log in")
    }
    if(registerUser.value.errorMessage.isNotBlank()){
        ErrorDialog(error = registerUser.value.errorMessage,
            onDismissRequest = {loginUserViewModel.cleanErrorMessage()}
        )
    }
    Spacer(modifier = Modifier.height(16.dp))
}