package com.tonyk.android.movieo.database

import com.tonyk.android.movieo.model.Movie
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class MovieDatabaseRepository @Inject constructor(private val database: MovieDatabase) {


    fun getMovies(): Flow<List<Movie>> = database.movieDao().getMovies()

    suspend fun getMovie(imdb: String): Movie = database.movieDao().getMovie(imdb)

    suspend fun addMovie(movie: Movie) {
        database.movieDao().addMovie(movie)
    }

    suspend fun updateMovie(movie: Movie) {
        database.movieDao().updateMovie(movie)
    }

    suspend fun deleteMovie(movie: Movie) {
        database.movieDao().deleteMovie(movie)
    }

}