package com.tonyk.android.movieo.api

import android.util.Log
import com.squareup.moshi.JsonDataException
import com.tonyk.android.movieo.model.MovieDetailsItem
import com.tonyk.android.movieo.model.MovieListItem
import javax.inject.Inject
import javax.inject.Singleton



@Singleton
class MovieApiRepositoryImpl @Inject constructor(private val movieApi: MovieApi): MovieApiRepository {

    private val apiKey = "17a5d486"


    override suspend fun searchMovies(query: String, page: Int) : List<MovieListItem>  {
        return try {
            movieApi.searchMovies(apiKey, query, page).searchResults
        }
        catch (e: JsonDataException) {
            Log.e("MovieApiRepository", "Error parsing JSON response: ${e.message}")
            emptyList()
        }
    }


    override suspend fun searchDetails(imdbID: String): MovieDetailsItem? {
        return try {
            movieApi.searchDetails(apiKey, imdbID)
        } catch (e: Exception) {
            Log.e("MovieApiRepository", "Error fetching movie details: ${e.message}")
            null
        }
    }
}

