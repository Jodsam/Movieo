package com.tonyk.android.movieo.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieListItem(
    @Json(name = "Title") val title: String,
    @Json(name = "Year") val year: String,
    @Json(name = "imdbID") val imdbId: String,
    @Json(name = "Type") val type: String,
    @Json(name = "Poster") val posterUrl: String
)