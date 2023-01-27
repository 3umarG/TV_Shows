package com.example.tvshows.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tvshows.pojo.TvShow

@Database(
    entities = [TvShow::class],
    version = 2
)

abstract class TvShowsDataBase : RoomDatabase() {

    abstract fun getDao() : TvShowsDao

    companion object {
        private var INSTANCE: TvShowsDataBase? = null
        private val LOCK  = Any()

        fun createDatabase(context: Context): TvShowsDataBase {
            if (INSTANCE != null) return INSTANCE!!
            else {
                synchronized(LOCK){
                    val newInst = Room.databaseBuilder(
                        context ,
                        TvShowsDataBase::class.java ,
                        "tvShows_db"
                    ).build()

                    INSTANCE = newInst
                    return newInst
                }
            }
        }


    }
}