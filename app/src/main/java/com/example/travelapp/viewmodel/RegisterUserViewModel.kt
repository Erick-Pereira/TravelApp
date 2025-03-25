package com.example.registeruser.screens

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class RegisterUser(
    val user: String = "",
    val name: String = "",
    val email: String = "",
    var password: String = "",
    val confirmPassword: String = "",
    val errorMessage: String = "",
) {
    fun validateAllFields() {
        if(user.isBlank()){
            throw Exception("User is required")
        }
        if(name.isBlank()){
            throw Exception("Name is required")
        }
        if(validateEmail().isNotBlank()){
            throw Exception(validateEmail())
        }
        if(validatePassword().isNotBlank()){
            throw Exception(validatePassword())
        }
        if(validateConfirmPassword().isNotBlank()){
            throw Exception(validateConfirmPassword())
        }
    }

    private fun validateEmail(): String {
        if (email.isBlank()) {
            return "Email is required"
        }

        val emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$".toRegex()
        if (!email.matches(emailRegex)) {
            return "Email in wrong format"
        }

        return ""
    }

    private fun validateConfirmPassword(): String {
    if(password.isBlank()){
        return "Password is required"
    }
        return ""
        }

    private fun validatePassword(): String {
        if(!password.equals(confirmPassword)){
            return "Password is not equal"
        }
        return ""
    }
}

class RegisterUserViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(RegisterUser())
    val uiState: StateFlow<RegisterUser> = _uiState.asStateFlow()

    fun onUserChange(user: String) {
        _uiState.value = _uiState.value.copy(user = user)
    }

    fun onNameChange(name: String){
        _uiState.value = _uiState.value.copy(name = name)
    }

    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(email = email)
    }

    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(password = password)
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        _uiState.value = _uiState.value.copy(confirmPassword = confirmPassword)
    }

    fun register():Boolean{
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