package com.tonyk.android.movieo.database

import android.content.Context
import androidx.room.Room
import com.tonyk.android.movieo.model.Movie
import kotlinx.coroutines.flow.Flow

private const val DATABASE_NAME = "movie-database"

class MovieDatabaseRepository private constructor(context: Context
) {


    private val database: MovieDatabase = Room
        .databaseBuilder(
            context.applicationContext,
            MovieDatabase::class.java,
            DATABASE_NAME
        )
        .addMigrations(migration_1_2)
        .build()


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


    companion object {
        private var INSTANCE: MovieDatabaseRepository? = null
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = MovieDatabaseRepository(context)
            }
        }
        fun get(): MovieDatabaseRepository {
            return INSTANCE ?:
            throw IllegalStateException("MovieRepositoryDB must be initialized")
        }
    }
}