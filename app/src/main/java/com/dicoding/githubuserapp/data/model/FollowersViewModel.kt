package com.dicoding.githubuserapp.data.model

import android.service.controls.ControlsProviderService
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubuserapp.api.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersViewModel : ViewModel() {
    private val listUserFollowers = MutableLiveData<ArrayList<GitHubUser>>()

    fun setListUserFollowers(username: String) {
        ApiConfig.getApiService().getUserFollowers(username)
            .enqueue(object : Callback<ArrayList<GitHubUser>> {
                override fun onResponse(
                    call: Call<ArrayList<GitHubUser>>,
                    response: Response<ArrayList<GitHubUser>>
                ) {
                    if (response.isSuccessful) {
                        listUserFollowers.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<ArrayList<GitHubUser>>, t: Throwable) {
                    Log.e(ControlsProviderService.TAG, "Failure: ${t.message}")
                }

            })
    }

    fun getListUserFollowers(): LiveData<ArrayList<GitHubUser>> {
        return listUserFollowers
    }
}