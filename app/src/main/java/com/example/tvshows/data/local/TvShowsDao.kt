package com.example.tvshows.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.tvshows.pojo.TvShow


@Dao
interface TvShowsDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertTvShow(show: TvShow)

    @Delete
    suspend fun deleteTvShow(show: TvShow)


    @Query("SELECT * FROM tvShows")
    fun getTvShowsWatchedList(): LiveData<List<TvShow>>
}