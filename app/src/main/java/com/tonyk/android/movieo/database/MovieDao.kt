package com.tonyk.android.movieo.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

import com.tonyk.android.movieo.model.Movie
import kotlinx.coroutines.flow.Flow


@Dao
interface MovieDao {
    @Query("SELECT  * FROM movie")
    fun getMovies(): Flow<List<Movie>>

    @Query("SELECT * FROM movie WHERE imdbID=(:imdbID)")
    suspend fun getMovie(imdbID: String) : Movie

    @Insert
    suspend fun addMovie(movie: Movie)

    @Update
    suspend fun updateMovie(movie: Movie)

    @Delete
    suspend fun deleteMovie(movie: Movie)
}