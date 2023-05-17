package com.tonyk.android.movieo.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Movie (
    @PrimaryKey val imdbID: String,
    val title: String,
    val poster: String,
    val date: String,
    var myRating: String,
    var isAddedtoWatchList: Boolean,
    var isWatched: Boolean
    )