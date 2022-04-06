package com.dicoding.githubuserapp.data.model

data class DetailUserResponse(
    val id: String,
    val name: String,
    val login: String,
    val publicRepos: String,
    val company: String,
    val location: String,
    val avatar_url: String,
    val followersUrl: String,
    val followingUrl: String,
    val followers: Int,
    val following: Int,
)
