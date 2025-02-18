package com.example.astros.updateprofile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.astros.app.data.dto.AuthResponse
import com.example.astros.app.data.dto.User
import com.example.astros.app.service.RetrofitClient
import com.example.astros.app.service.RetrofitClient.apiService
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import retrofit2.Response
import java.io.File

class UpdateProfileViewModel : ViewModel() {

    private val _updateResult = MutableLiveData<Result<AuthResponse>>()
    val updateResult: LiveData<Result<AuthResponse>> = _updateResult

    fun updateUserProfile(token: String, user: User) {
        viewModelScope.launch {
            try {
                val response: Response<AuthResponse> = apiService.updateUserProfile("Bearer $token", user)
                if (response.isSuccessful && response.body() != null) {
                    _updateResult.postValue(Result.success(response.body()!!))
                } else {
                    val errorMessage = extractErrorMessage(response)
                    _updateResult.postValue(Result.failure(Exception(errorMessage)))
                }
            } catch (e: Exception) {
                Log.e("UpdateProfileViewModel", "Error updating profile", e)
                _updateResult.postValue(Result.failure(e))
            }
        }
    }

    private fun extractErrorMessage(response: Response<AuthResponse>): String {
        return try {
            val errorBody = response.errorBody()?.string()
            val jsonObject = JSONObject(errorBody ?: "{}")
            jsonObject.optString("message", "Profile update failed")
        } catch (e: Exception) {
            "Profile update failed"
        }
    }

    fun updateProfilePhoto(token: String, imageFile: File, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val requestFile = imageFile.asRequestBody()
                val imagePart = MultipartBody.Part.createFormData("profilePhoto", imageFile.name, requestFile)

                val response = apiService.updateProfilePhoto("Bearer $token", imagePart)
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    onError("Failed to update profile photo")
                }
            } catch (e: Exception) {
                onError("Error: ${e.message}")
            }
        }
    }

}