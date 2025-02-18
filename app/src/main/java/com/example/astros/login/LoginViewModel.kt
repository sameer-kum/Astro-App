package com.example.astros.login

import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.astros.app.data.dto.AuthResponse
import com.example.astros.app.data.dto.LoginCredentials
import com.example.astros.app.service.RetrofitClient
import com.example.astros.service.SharedPreference
import kotlinx.coroutines.*
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException

class LoginViewModel(private val sharedPreference: SharedPreference) : ViewModel() {

    private val _loginResponse = MutableLiveData<AuthResponse?>()
    val loginResponse: LiveData<AuthResponse?> get() = _loginResponse

    private val _loginError = MutableLiveData<String>()
    val loginError: LiveData<String> get() = _loginError

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun loginUser(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _loginError.value = "Please enter both email and password"
            return
        }

        val credentials = LoginCredentials(email, password)

        // Call API in background thread
        uiScope.launch {
            try {
                val response = RetrofitClient.apiService.loginUser(credentials)
                if (response.isSuccessful) {
                    _loginResponse.value = response.body()
                    _loginResponse.value?.let {
                        sharedPreference.clearToken()
                        sharedPreference.saveToken(it.token)
                        sharedPreference.saveLoginStatus(true)
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = extractErrorMessage(errorBody) ?: "Invalid credentials"
                    _loginError.value = errorMessage
                }
            } catch (e: HttpException) {
                _loginError.value = "Server error: ${e.message}"
            } catch (e: IOException) {
                _loginError.value = "Network error. Please check your internet connection."
            } catch (e: Exception) {
                _loginError.value = "Unexpected error: ${e.localizedMessage}"
            }
        }
    }

    private fun extractErrorMessage(errorBody: String?): String? {
        return try {
            val jsonObject = JSONObject(errorBody ?: "")
            jsonObject.getString("message") // Assuming error JSON has {"message": "Invalid credentials"}
        } catch (e: Exception) {
            null
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
