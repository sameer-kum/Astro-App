package com.example.astros.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.astros.app.data.dto.User
import com.example.astros.app.data.dto.UserProfile
import com.example.astros.app.service.RetrofitClient
import kotlinx.coroutines.launch

class DashboardViewModel : ViewModel() {

    private val _userProfile = MutableLiveData<User?>()
    val userProfile: LiveData<User?> = _userProfile

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun fetchUserProfile(token: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getUserProfile("Bearer $token")
                if (response.isSuccessful) {
                    _userProfile.postValue(response.body())
                } else {
                    _error.postValue("Failed to fetch user profile")
                }
            } catch (e: Exception) {
                _error.postValue("Error: ${e.message}")
            }
        }
    }
}
