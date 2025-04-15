package com.example.travelapp.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.travelapp.dao.UserDao
import com.example.travelapp.viewmodel.TravelListViewModel

class TravelListViewModelFactory(
    private val userDao: UserDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TravelListViewModel(userDao) as T
    }
}