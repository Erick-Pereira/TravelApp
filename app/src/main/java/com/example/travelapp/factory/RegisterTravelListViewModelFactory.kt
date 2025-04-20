package com.example.travelapp.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.travelapp.dao.TravelDao
import com.example.travelapp.viewmodel.RegisterTravelListViewModel

class RegisterTravelListViewModelFactory(
    private val travelDao: TravelDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RegisterTravelListViewModel(travelDao) as T
    }
}