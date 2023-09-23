package com.pnj.githubuser.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pnj.githubuser.data.model.UserDetail
import com.pnj.githubuser.data.model.response.UserResponse
import com.pnj.githubuser.data.retrofit.ApiConfig
import com.pnj.githubuser.helper.SingleEvent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel() {
    private val _userDetail = MutableLiveData<UserDetail>()
    val userDetail: LiveData<UserDetail> = _userDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<SingleEvent<String>>()
    val errorMessage: LiveData<SingleEvent<String>> = _errorMessage

    fun getUserDetail(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val user = UserDetail(
                            responseBody.login,
                            responseBody.name ?: responseBody.login,
                            responseBody.avatarUrl ?: "",
                            responseBody.followers,
                            responseBody.following

                        )
                        _userDetail.value = user
                    }
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.d("MainViewModel", "onFailure: ${t.message}")
            }

        })
    }
}