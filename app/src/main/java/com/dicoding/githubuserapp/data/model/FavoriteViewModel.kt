package com.dicoding.githubuserapp.data.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.dicoding.githubuserapp.data.database.FavoriteGithubUser
import com.dicoding.githubuserapp.data.database.FavoriteGithubUserDao
import com.dicoding.githubuserapp.data.database.FavoriteGithubUserDatabase

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {
    private var userDao: FavoriteGithubUserDao?
    private var userDatabase: FavoriteGithubUserDatabase?

    init {
        userDatabase = FavoriteGithubUserDatabase.getInstance(application)
        userDao = userDatabase?.favoriteGithubUserDao()
    }

    fun getFavUser(): LiveData<List<FavoriteGithubUser>>? {
        return userDao?.getFavoriteUser()
    }
}