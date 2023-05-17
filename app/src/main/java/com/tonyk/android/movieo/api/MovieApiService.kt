package com.tonyk.android.movieo.api

import com.tonyk.android.movieo.model.MovieDetailItem
import retrofit2.http.GET
import retrofit2.http.Query



interface MovieApiService {
    @GET("/")
    suspend fun searchMovies(
        @Query("apikey") apiKey: String,
        @Query("s") query: String,
        @Query("page") page: Int
    ): MovieSearchResponse

    @GET("/")
    suspend fun searchDetails(
    @Query("apikey") apiKey: String,
    @Query("i") imdbID: String
    ) : MovieDetailItem
}