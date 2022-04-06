package com.dicoding.githubuserapp.api

import com.dicoding.githubuserapp.data.model.DetailUserResponse
import com.dicoding.githubuserapp.data.model.GitHubUser
import com.dicoding.githubuserapp.data.model.GithubUserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    @Headers("Authorization: token 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")
    fun getSearchUsers(
        @Query("q") query: String
    ): Call<GithubUserResponse>

    @GET("users/{username}")
    @Headers("Authorization: token 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")
    fun getUserDetail(
        @Path("username") username: String
    ): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: token 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")
    fun getUserFollowers(
        @Path("username") username: String
    ): Call<ArrayList<GitHubUser>>

    @GET("users/{username}/following")
    @Headers("Authorization: token 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")
    fun getUserFollowing(
        @Path("username") username: String
    ): Call<ArrayList<GitHubUser>>
}
