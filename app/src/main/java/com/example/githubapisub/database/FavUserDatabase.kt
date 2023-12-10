package com.example.githubapisub.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.githubapisub.database.dao.FavUserDao
import com.example.githubapisub.database.entity.FavUser

@Database(entities = [FavUser::class], version = 1)
abstract class FavUserDatabase : RoomDatabase() {
    abstract fun favUserDao(): FavUserDao

    companion object {
        @Volatile
        private var INSTANCE: FavUserDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): FavUserDatabase {
            if (INSTANCE == null) {
                synchronized(FavUserDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        FavUserDatabase::class.java, "favUser_database"
                    )
                        .build()
                }
            }
            return INSTANCE as FavUserDatabase
        }
    }
}