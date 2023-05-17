package com.tonyk.android.movieo.model

import androidx.room.Entity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.tonyk.android.movieo.api.RatingsResponse


@Entity
@JsonClass(generateAdapter = true)
data class MovieDetailItem(
    @Json(name = "Title") val title: String,
    @Json(name = "Released") val date: String,
    @Json(name = "Runtime") val runtime: String,
    @Json(name = "Plot") val plot: String,
    val Country: String,
    val Language: String,
    val Ratings: List<RatingsResponse>,
    val Poster: String,
    val Actors: String,
    val Genre: String,
    val Director: String,
    val imdbID: String
)