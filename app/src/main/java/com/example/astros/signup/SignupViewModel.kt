package com.example.astros.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.astros.app.data.dto.AuthResponse
import com.example.astros.app.data.dto.User
import com.example.astros.app.service.RetrofitClient
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response

class SignupViewModel : ViewModel() {

    private val _registrationResult = MutableLiveData<Result<AuthResponse>>()
    val registrationResult: LiveData<Result<AuthResponse>> = _registrationResult

    fun registerUser(user: User) {
        viewModelScope.launch {
            try {
                val response: Response<AuthResponse> = RetrofitClient.apiService.registerUser(user)
                if (response.isSuccessful && response.body() != null) {
                    _registrationResult.postValue(Result.success(response.body()!!))
                } else {
                    val errorMessage = extractErrorMessage(response)
                    _registrationResult.postValue(Result.failure(Exception(errorMessage)))
                }
            } catch (e: Exception) {
                Log.e("SignupViewModel", "Error during registration", e)
                _registrationResult.postValue(Result.failure(e))
            }
        }
    }

    private fun extractErrorMessage(response: Response<AuthResponse>): String {
        return try {
            val errorBody = response.errorBody()?.string()
            val jsonObject = JSONObject(errorBody ?: "{}")
            jsonObject.optString("message", "Registration failed")
        } catch (e: Exception) {
            "Registration failed"
        }
    }
}

