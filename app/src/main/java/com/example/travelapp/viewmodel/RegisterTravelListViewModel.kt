package com.example.travelapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelapp.dao.TravelDao
import com.example.travelapp.entity.Travel
import com.example.travelapp.enums.EnumTravelType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class RegisterTravel(
    val id: Int? = null,
    val destination: String = "",
    val travelType: EnumTravelType = EnumTravelType.LAZER,
    val startDate: Date = Date(),
    val endDate: Date = Date(),
    val budget: Double = 0.0,
    val script: String = "",
    val errorMessage: String = "",
    val isSaved: Boolean = false,
) {
    fun validateAllFields() {
        if (destination.isBlank()) {
            throw Exception("Destination is required")
        }
        if (budget <= 0) {
            throw Exception("Budget must be greater than zero")
        }
        if (startDate.after(endDate)) {
            throw Exception("Start date must be before end date")
        }
    }

    fun toTravel(): Travel {
        return Travel(
            id = id ?: 0,
            destination = destination,
            travelType = travelType,
            startDate = startDate,
            endDate = endDate ?: startDate,
            budget = budget,
            script = script
        )
    }
}

class RegisterTravelListViewModel(private val travelDao: TravelDao) : ViewModel() {
    private val _uiState = MutableStateFlow(RegisterTravel())
    val uiState: StateFlow<RegisterTravel> = _uiState.asStateFlow()

    val travels: Flow<List<Travel>> = travelDao.findAll()

    fun onDestinationChange(destination: String) {
        _uiState.value = _uiState.value.copy(destination = destination)
    }

    fun onTravelTypeChange(travelType: EnumTravelType) {
        _uiState.value = _uiState.value.copy(travelType = travelType)
    }

    fun onBudgetChange(budget: Double) {
        _uiState.value = _uiState.value.copy(budget = budget)
    }

    fun onStartDateChange(startDate: String) {
        val parsedDate = parseDate(startDate)
        if (parsedDate != null) {
            _uiState.value = _uiState.value.copy(startDate = parsedDate)
        } else {
            // Handle invalid date input - maybe show an error message
            _uiState.value = _uiState.value.copy(errorMessage = "Invalid start date format")
        }
    }

    fun onEndDateChange(endDate: String) {
        val parsedDate = parseDate(endDate)
        if (parsedDate != null) {
            _uiState.value = _uiState.value.copy(endDate = parsedDate)
        } else {
            // Handle invalid date input - maybe show an error message
            _uiState.value = _uiState.value.copy(errorMessage = "Invalid end date format")
        }
    }

    private fun parseDate(dateString: String): Date? {
        return try {
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(dateString)
        } catch (e: ParseException) {
            null
        }
    }

    fun saveTravel(onSaved: (Int) -> Unit = {}) {
        viewModelScope.launch {
            val currentTravel = uiState.value
            val travel = currentTravel.toTravel()
            val id = if (currentTravel.id != null && currentTravel.id != -1) {
                travelDao.update(travel)
                currentTravel.id
            } else {
                travelDao.insert(travel).toInt()
            }
            _uiState.value = _uiState.value.copy(isSaved = true, id = id)
            onSaved(id)
        }
    }

    fun loadTravel(travelId: Int) {
        viewModelScope.launch {
            val travel = travelDao.findById(travelId)
            if (travel != null) {
                _uiState.value = RegisterTravel(
                    id = travel.id,
                    destination = travel.destination,
                    travelType = travel.travelType,
                    startDate = travel.startDate,
                    endDate = travel.endDate,
                    budget = travel.budget,
                    script = travel.script
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Travel not found"
                )
            }
        }
    }

    fun deleteTravel(travel: Travel) {
        viewModelScope.launch {
            travelDao.delete(travel)
        }
    }

    fun updateTravelRoteiro(travelId: Int, roteiro: String) {
        viewModelScope.launch {
            val travel = travelDao.findById(travelId)
            if (travel != null) {
                val updatedTravel = travel.copy(script = roteiro)
                travelDao.update(updatedTravel)
                // Atualiza o estado se for a viagem atualmente exibida
                if (uiState.value.id == travelId) {
                    _uiState.value = _uiState.value.copy(script = roteiro)
                }
            }
        }
    }
}