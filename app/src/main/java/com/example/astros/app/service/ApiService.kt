package com.example.astros.app.service

import com.example.astros.app.data.dto.AuthResponse
import com.example.astros.app.data.dto.LoginCredentials
import com.example.astros.app.data.dto.User
import com.example.astros.app.data.dto.UserProfile
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part

interface ApiService {

    @POST("auth/register")
    suspend fun registerUser(
        @Body user: User
    ): Response<AuthResponse>

    @POST("auth/login")
    suspend fun loginUser(
        @Body credentials: LoginCredentials
    ): Response<AuthResponse>

    @GET("user/profile")
    suspend fun getUserProfile(
        @Header("Authorization") token: String
    ): Response<User>

    @PUT("user/updateProfile")
    suspend fun updateUserProfile(
        @Header("Authorization") token: String,
        @Body user: User
    ): Response<AuthResponse>

    @Multipart
    @PUT("user/updateProfile") // Adjust API endpoint
    suspend fun updateProfilePhoto(
        @Header("Authorization") token: String,
        @Part profilePhoto: MultipartBody.Part
    ): Response<ResponseBody>

    @DELETE("user/delete")
    suspend fun deleteUser(
        @Header("Authorization") token: String
    ): Response<AuthResponse>
}
