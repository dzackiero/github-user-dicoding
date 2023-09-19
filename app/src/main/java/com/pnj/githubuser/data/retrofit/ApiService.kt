package com.pnj.githubuser.data.retrofit

import com.pnj.githubuser.data.response.GithubResponse
import com.pnj.githubuser.data.response.UserItem
import com.pnj.githubuser.data.response.UserResponse
import retrofit2.Call
import retrofit2.http.*

const val TOKEN = "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"

interface ApiService {

    @Headers("Authorization: token $TOKEN")
    @GET("search/users")
    fun getSearchUser(
        @Query("q") username: String
    ): Call<GithubResponse>

    @Headers("Authorization: token $TOKEN")
    @GET("users/{username}")
    fun getDetailUser(
        @Path("username") username: String
    ): Call<UserResponse>

    @Headers("Authorization: token $TOKEN")
    @GET("users/{username}/followers")
    fun getUserFollowers(
        @Path("username") username: String
    ): Call<List<UserItem>>

    @GET("users/{username}/following")
    fun getUserFollowing(
        @Path("username") username: String
    ): Call<List<UserItem>>
}