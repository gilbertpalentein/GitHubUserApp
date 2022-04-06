package com.dicoding.githubuserapp.data.model

data class GitHubUser(
    val id: Int,
    val login: String,
    val type: String,
    val avatar_url: String
)