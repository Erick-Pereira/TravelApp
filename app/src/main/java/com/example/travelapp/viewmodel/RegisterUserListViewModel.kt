package com.example.travelapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.travelapp.dao.UserDao
import kotlinx.coroutines.flow.Flow
import com.example.travelapp.entity.User

class RegisterUserListViewModel(private val userDao: UserDao):ViewModel() {
    val users: Flow<List<User>> = userDao.findAll();
}