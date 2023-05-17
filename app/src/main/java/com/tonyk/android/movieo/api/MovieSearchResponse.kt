package com.tonyk.android.movieo.api


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.tonyk.android.movieo.model.MovieListItem

@JsonClass(generateAdapter = true)
data class MovieSearchResponse(
    @Json(name = "Search") val searchResults: List<MovieListItem>,
    @Json(name = "totalResults") val totalResults: String,
    @Json(name = "Response") val response: String
)