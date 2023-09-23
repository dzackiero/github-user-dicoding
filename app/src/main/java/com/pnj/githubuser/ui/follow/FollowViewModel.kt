package com.pnj.githubuser.ui.follow

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pnj.githubuser.data.model.response.UserItem
import com.pnj.githubuser.data.retrofit.ApiConfig.Companion.getApiService
import com.pnj.githubuser.helper.SingleEvent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowViewModel : ViewModel() {

    private val _result = MutableLiveData<List<UserItem>>()
    val result: LiveData<List<UserItem>> = _result

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<SingleEvent<String>>()
    val errorMessage: LiveData<SingleEvent<String>> = _errorMessage


    fun getFollow(username: String, tab: Int) {
        _isLoading.value = true
        val client = when (tab) {
            FOLLOWERS -> getApiService().getUserFollowers(username)
            else -> getApiService().getUserFollowing(username)
        }
        client.enqueue(object : Callback<List<UserItem>> {
            override fun onResponse(
                call: Call<List<UserItem>>,
                response: Response<List<UserItem>>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    val responseBody = response.body()
                    responseBody?.let {
                        _result.value = it
                    }
                }

            }

            override fun onFailure(call: Call<List<UserItem>>, t: Throwable) {
                _isLoading.value = false
                Log.d("MainViewModel", "onFailure: ${t.message}")
            }

        })
    }

    companion object {
        const val TAB = "TAB"
        const val USERNAME = "USERNAME"
        const val FOLLOWERS = 0
        const val FOLLOWING = 1
    }

}
