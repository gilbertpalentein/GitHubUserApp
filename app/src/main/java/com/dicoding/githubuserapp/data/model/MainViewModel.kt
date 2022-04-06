package com.dicoding.githubuserapp.data.model

import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import androidx.lifecycle.*
import com.dicoding.githubuserapp.api.ApiConfig
import com.dicoding.githubuserapp.setting.SettingPreferences
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val pref: SettingPreferences) : ViewModel() {
    val listUsers = MutableLiveData<ArrayList<GitHubUser>>()

    fun setSearchGithubUser(query: String) {
        ApiConfig.getApiService().getSearchUsers(query).enqueue(object :
            Callback<GithubUserResponse> {
            override fun onResponse(
                call: Call<GithubUserResponse>,
                response: Response<GithubUserResponse>
            ) {
                if (response.isSuccessful) {
                    listUsers.postValue(response.body()?.items)
                }
            }

            override fun onFailure(call: Call<GithubUserResponse>, t: Throwable) {
                Log.e(TAG, "Failure: ${t.message}")
            }

        })
    }

    fun getSearchGithubuser(): LiveData<ArrayList<GitHubUser>>{
        return listUsers
    }

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }
}