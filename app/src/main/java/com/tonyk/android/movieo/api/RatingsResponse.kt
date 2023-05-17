package com.tonyk.android.movieo.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RatingsResponse(
    @Json(name = "Source") val ratingsSource: String,
    @Json(name = "Value") val rating: String
)