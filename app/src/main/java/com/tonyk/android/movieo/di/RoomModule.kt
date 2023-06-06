package com.tonyk.android.movieo.di

import android.content.Context
import androidx.room.Room
import com.tonyk.android.movieo.database.MovieDatabase
import com.tonyk.android.movieo.database.MovieDatabaseRepository
import com.tonyk.android.movieo.database.MovieDatabaseRepositoryImpl
import com.tonyk.android.movieo.database.migration_1_2
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


private const val DATABASE_NAME = "movie-database"

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideMovieDatabase(@ApplicationContext context: Context): MovieDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            MovieDatabase::class.java,
            DATABASE_NAME
        )
            .addMigrations(migration_1_2)
            .build()
    }

    @Provides
    @Singleton
    fun provideMovieDatabaseRepository(database: MovieDatabase): MovieDatabaseRepository {
        return MovieDatabaseRepositoryImpl(database)
    }
}