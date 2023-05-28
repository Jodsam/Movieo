package com.tonyk.android.movieo.di

import com.tonyk.android.movieo.api.MovieApiService
import com.tonyk.android.movieo.repositories.FirebaseRepository
import com.tonyk.android.movieo.repositories.MovieApiRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMovieApi(): MovieApiService {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://www.omdbapi.com/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        return retrofit.create(MovieApiService::class.java)
    }
    @Provides
    @Singleton
    fun provideMovieApiRepository(movieApi: MovieApiService): MovieApiRepository {
        return MovieApiRepository(movieApi)
    }

    @Provides
    @Singleton
    fun provideFirebaseRepository(): FirebaseRepository {
        return FirebaseRepository()
    }

}

