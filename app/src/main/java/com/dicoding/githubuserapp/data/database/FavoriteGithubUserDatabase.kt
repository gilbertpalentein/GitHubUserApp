package com.dicoding.githubuserapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavoriteGithubUser::class], version = 1)
abstract class FavoriteGithubUserDatabase : RoomDatabase() {
    companion object {
        private var INSTANCE: FavoriteGithubUserDatabase? = null

        fun getInstance(context: Context): FavoriteGithubUserDatabase? {
            if (INSTANCE == null) {
                synchronized(FavoriteGithubUserDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        FavoriteGithubUserDatabase::class.java,
                        "favoriteUserDB"
                    ).build()
                }
            }
            return INSTANCE
        }
    }

    abstract fun favoriteGithubUserDao(): FavoriteGithubUserDao
}