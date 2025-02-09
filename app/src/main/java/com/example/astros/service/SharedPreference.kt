package com.example.astros.service

import android.content.Context
import android.content.SharedPreferences

class SharedPreference(context: Context) {

    private val PREF_NAME = "AstrosPrefs"

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun saveSelectedLanguage(language: String) {
        sharedPreferences.edit().putString("selected_language", language).apply()
    }

    fun getSelectedLanguage(): String? {
        return sharedPreferences.getString("selected_language", null)
    }

    // Save the name (example using SharedPreferences)
    fun saveName(name: String) {
        sharedPreferences.edit().putString("user_name", name).apply()
    }

    fun getName(): String {
        return sharedPreferences.getString("user_name", "Default Name") ?: "Default Name"
    }

    fun savePhoneNo(phoneNo: String) {
        sharedPreferences.edit().putString("user_phone_no", phoneNo).apply()
    }

    fun getPhoneNo(): String {
        return sharedPreferences.getString("user_phone_no", "Default Name") ?: "Default Name"
    }

    fun saveEmail(email: String) {
        sharedPreferences.edit().putString("user_email", email).apply()
    }

    fun getEmail(): String {
        return sharedPreferences.getString("user_email", "Not found") ?: "Not found"
    }

    fun savePassword(pwd: String) {
        sharedPreferences.edit().putString("user_password", pwd).apply()
    }

    fun getPassword(): String {
        return sharedPreferences.getString("user_password", "Not found") ?: "Not found"
    }

    fun saveGender(gender: String) {
        sharedPreferences.edit().putString("user_gender", gender).apply()
    }

    fun getGender(): String {
        return sharedPreferences.getString("user_gender", "Not found") ?: "Not found"
    }

    fun saveMaritalStatus(maritalStatus: String) {
        sharedPreferences.edit().putString("user_marital_status", maritalStatus).apply()
    }

    fun getMaritalStatus(): String {
        return sharedPreferences.getString("user_marital_status", "Not found") ?: "Not found"
    }

    fun saveDOB(DOB: String) {
        sharedPreferences.edit().putString("user_DOB", DOB).apply()
    }

    fun getDOB(): String {
        return sharedPreferences.getString("user_DOB", "Not found") ?: "Not found"
    }

    fun saveBirthTime(time: String) {
        sharedPreferences.edit().putString("user_Time_of_birth", time).apply()
    }

    fun getBirthTime(): String {
        return sharedPreferences.getString("user_Time_of_birth", "Not found") ?: "Not found"
    }

    fun saveBirthPlace(place: String) {
        sharedPreferences.edit().putString("user_Place_of_birth", place).apply()
    }

    fun getBirthPlace(): String {
        return sharedPreferences.getString("user_Place_of_birth", "Not found") ?: "Not found"
    }

}
