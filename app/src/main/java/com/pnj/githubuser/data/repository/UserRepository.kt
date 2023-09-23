package com.pnj.githubuser.data.repository

import androidx.lifecycle.LiveData
import com.pnj.githubuser.data.database.User
import com.pnj.githubuser.data.database.UserDao
import com.pnj.githubuser.data.model.response.UserResponse
import com.pnj.githubuser.data.retrofit.ApiService
import com.pnj.githubuser.helper.preferences.SettingPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository private constructor(
    private val preferences: SettingPreferences,
    private val apiService: ApiService,
    private val userDao: UserDao
) {
    fun getFavoriteUsers(): LiveData<List<User>> {
        return userDao.getAllUsers()
    }
}