package com.tonyk.android.movieo.repositories

import android.util.Log
import com.squareup.moshi.JsonDataException
import com.tonyk.android.movieo.api.MovieApiService
import com.tonyk.android.movieo.model.MovieDetailItem
import com.tonyk.android.movieo.model.MovieListItem
import javax.inject.Inject
import javax.inject.Singleton


class MovieApiRepository @Inject constructor(private val movieApi: MovieApiService) {

    private val apiKey = "17a5d486"

    suspend fun searchMovies(query: String, page: Int) : List<MovieListItem>  {
        return try {
            movieApi.searchMovies(apiKey, query, page).searchResults
        }
        catch (e: JsonDataException) {
            Log.e("MovieApiRepository", "Error parsing JSON response: ${e.message}")
            emptyList()
        }
    }

    suspend fun searchDetails(imdbID: String): MovieDetailItem? {
        return try {
            movieApi.searchDetails(apiKey, imdbID)
        } catch (e: Exception) {
            Log.e("MovieApiRepository", "Error fetching movie details: ${e.message}")
            null
        }
    }
}

