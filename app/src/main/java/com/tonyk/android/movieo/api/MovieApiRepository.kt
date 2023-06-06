package com.tonyk.android.movieo.api

import com.tonyk.android.movieo.model.MovieDetailsItem
import com.tonyk.android.movieo.model.MovieListItem

interface MovieApiRepository {

    suspend fun searchMovies(query: String, page: Int) : List<MovieListItem>

    suspend fun searchDetails(imdbID: String): MovieDetailsItem?

}