package com.example.travelapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelapp.dao.UserDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LoginUser(
    val login: String = "",
    var password: String = "",
    val errorMessage: String = "",
    val isLoggedIn: Boolean = false
) {
    fun validateAllFields() {
        if (login.isBlank()) {
            throw Exception("Login é obrigatório")
        }
        if (password.isBlank()) {
            throw Exception("Senha é obrigatória")
        }
    }
}

class LoginUserViewModel(private val userDao: UserDao) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUser())
    val uiState: StateFlow<LoginUser> = _uiState.asStateFlow()

    fun onLoginChange(login: String) {
        _uiState.value = _uiState.value.copy(login = login)
    }

    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(password = password)
    }

    fun login() {
        viewModelScope.launch {
            try {
                _uiState.value.validateAllFields()
                val user = userDao.findByUserAndPassword(_uiState.value.login, _uiState.value.password)
                if (user == null) {
                    throw Exception("Usuário ou senha incorretos")
                }
                _uiState.value = _uiState.value.copy(isLoggedIn = true, errorMessage = "")
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoggedIn = false, errorMessage = e.message ?: "Erro desconhecido")
            }
        }
    }

    fun cleanErrorMessage() {
        _uiState.value = _uiState.value.copy(errorMessage = "")
    }
}