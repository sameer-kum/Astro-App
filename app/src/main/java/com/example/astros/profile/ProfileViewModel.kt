package com.example.astros.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.astros.app.data.dto.AuthResponse
import com.example.astros.app.service.RetrofitClient.apiService
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response

class ProfileViewModel : ViewModel() {

    private val _deleteAccountResult = MutableLiveData<Result<String>>()
    val deleteAccountResult: LiveData<Result<String>> = _deleteAccountResult

    fun deleteAccount(token: String) {
        if (token.isNullOrEmpty()) {
            _deleteAccountResult.postValue(Result.failure(Exception("User not logged in")))
            return
        }

        viewModelScope.launch {
            try {
                val response: Response<AuthResponse> = apiService.deleteUser("Bearer $token")
                if (response.isSuccessful) {
                    _deleteAccountResult.postValue(Result.success("Account deleted successfully"))
                } else {
                    val errorMessage = extractErrorMessage(response)
                    _deleteAccountResult.postValue(Result.failure(Exception(errorMessage)))
                }
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error deleting account", e)
                _deleteAccountResult.postValue(Result.failure(e))
            }
        }
    }

    private fun extractErrorMessage(response: Response<AuthResponse>): String {
        return try {
            val errorBody = response.errorBody()?.string()
            val jsonObject = JSONObject(errorBody ?: "{}")
            jsonObject.optString("message", "Failed to delete account")
        } catch (e: Exception) {
            "Failed to delete account"
        }
    }
}
