package com.dicoding.githubuserapp.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavoriteGithubUserDao {
    @Insert
    suspend fun addToFavorite(favoriteGithubUser: FavoriteGithubUser)

    @Query("SELECT * FROM favoriteUser")
    fun getFavoriteUser(): LiveData<List<FavoriteGithubUser>>

    @Query("SELECT count(*) FROM favoriteUser WHERE favoriteUser.id = :id")
    suspend fun checkUserIsFav(id: Int): Int

    @Query("DELETE FROM favoriteUser WHERE favoriteUser.id = :id")
    suspend fun removeFromUserFav(id: Int): Int
}