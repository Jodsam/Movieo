package com.tonyk.android.movieo.database

import com.tonyk.android.movieo.model.Movie
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MovieDatabaseRepositoryImpl @Inject constructor(private val database: MovieDatabase): MovieDatabaseRepository {

    override fun getMovies(): Flow<List<Movie>> = database.movieDao().getMovies()

    override suspend fun getMovie(imdb: String): Movie = database.movieDao().getMovie(imdb)

    override suspend fun addMovie(movie: Movie) {
        database.movieDao().addMovie(movie)
    }

    override suspend fun updateMovie(movie: Movie) {
        database.movieDao().updateMovie(movie)
    }

    override suspend fun deleteMovie(movie: Movie) {
        database.movieDao().deleteMovie(movie)
    }

}