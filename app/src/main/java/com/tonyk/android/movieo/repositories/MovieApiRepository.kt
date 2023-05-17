package com.tonyk.android.movieo.repositories

import android.util.Log
import com.squareup.moshi.JsonDataException
import com.tonyk.android.movieo.api.MovieApiService
import com.tonyk.android.movieo.model.MovieDetailItem
import com.tonyk.android.movieo.model.MovieListItem
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MovieApiRepository {

    private val movieApi: MovieApiService
    private val apiKey = "17a5d486"

    init {


        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://www.omdbapi.com/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        movieApi = retrofit.create(MovieApiService::class.java)
    }
    suspend fun searchMovies(query: String, page: Int) : List<MovieListItem>  {
        return try {
            movieApi.searchMovies(apiKey, query, page).searchResults
        }
        catch (e: JsonDataException) {
            Log.e("MovieApiRepository", "Error parsing JSON response: ${e.message}")
            emptyList()
        }
    }

    suspend fun searchDetails(imdbID: String): MovieDetailItem {
      return  movieApi.searchDetails(apiKey, imdbID)
    }

}

