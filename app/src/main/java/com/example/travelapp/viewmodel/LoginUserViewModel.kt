package com.example.travelapp.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class LoginUser(
    val login: String = "",
    var password: String = "",   
    val errorMessage: String = "",
) {
    fun validateAllFields() {
        if(validateLogin().isNotBlank()){
            throw Exception(validateLogin())
        }
    }

    private fun validateLogin(): String {
        if(login!=password){
            return "Login ou Senha incorretos"
        }
        return ""
    }
}

class LoginUserViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUser())
    val uiState: StateFlow<LoginUser> = _uiState.asStateFlow()

    fun onLoginChange(login: String) {
        _uiState.value = _uiState.value.copy(login = login)
    }

    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(password = password)
    }

    fun login():Boolean{
        return try{
            _uiState.value.validateAllFields()
            true
        }catch (e: Exception){
            _uiState.value = _uiState.value.copy(errorMessage = e.message?: "Unknown error")
            false
        }
    }

    fun cleanErrorMessage() {
        _uiState.value = _uiState.value.copy(errorMessage = "")
    }
}