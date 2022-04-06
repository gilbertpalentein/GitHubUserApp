package com.dicoding.githubuserapp.data.model

import android.app.Application
import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.githubuserapp.api.ApiConfig
import com.dicoding.githubuserapp.data.database.FavoriteGithubUser
import com.dicoding.githubuserapp.data.database.FavoriteGithubUserDao
import com.dicoding.githubuserapp.data.database.FavoriteGithubUserDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel(application: Application) : AndroidViewModel(application) {
    val githubUser = MutableLiveData<DetailUserResponse>()

    private var userDao: FavoriteGithubUserDao?
    private var userDatabase: FavoriteGithubUserDatabase?

    init {
        userDatabase = FavoriteGithubUserDatabase.getInstance(application)
        userDao = userDatabase?.favoriteGithubUserDao()
    }

    fun setGitHubUserDetail(username: String) {
        ApiConfig.getApiService().getUserDetail(username).enqueue(object :
            Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                if (response.isSuccessful) {
                    githubUser.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                Log.e(TAG, "Failure: ${t.message}")
            }

        })
    }

    fun getGitHubUserDetail(): LiveData<DetailUserResponse> {
        return githubUser
    }

    fun addToFavGithubUser(id: Int, username: String, type: String, avatarUrl: String){
        CoroutineScope(Dispatchers.IO).launch {
            var githubUser = FavoriteGithubUser(
                id,
                username,
                type,
                avatarUrl
            )
            userDao?.addToFavorite(githubUser)
        }
    }

    suspend fun checkUserFav(id: Int) = userDao?.checkUserIsFav(id)

    fun removeFromFav(id: Int){
        CoroutineScope(Dispatchers.IO).launch {
            userDao?.removeFromUserFav(id)
        }
    }
}