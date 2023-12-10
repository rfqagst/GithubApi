package com.example.githubapisub.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.githubapisub.database.entity.FavUser


@Dao
interface FavUserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favUser: FavUser)

    @Update
    fun update(favUser: FavUser)

    @Delete
    fun delete(favUser: FavUser)

    @Query("SELECT * from favUser WHERE username != '' AND avatarUrl IS NOT NULL")
    fun getAllFavUsers(): LiveData<List<FavUser>>

    @Query("SELECT COUNT(*) FROM favUser WHERE username = :username AND  avatarUrl IS NOT NULL")
    fun isUserFavorited(username: String): LiveData<Int>

    @Query("SELECT * FROM favUser WHERE username = :username")
    fun getFavoriteUserByUsername(username: String): LiveData<FavUser>

}
