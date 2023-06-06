package com.tonyk.android.movieo.database

import com.tonyk.android.movieo.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieDatabaseRepository {
    fun getMovies(): Flow<List<Movie>>
    suspend fun getMovie(imdb: String): Movie
    suspend fun addMovie(movie: Movie)
    suspend fun updateMovie(movie: Movie)
    suspend fun deleteMovie(movie: Movie)
}