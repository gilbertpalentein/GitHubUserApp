package com.dicoding.githubuserapp.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "favoriteUser")
data class FavoriteGithubUser(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "username")
    val login: String,

    @ColumnInfo(name = "type")
    val type: String,

    @ColumnInfo(name = "avater")
    val avatarUrl : String

) : Serializable
