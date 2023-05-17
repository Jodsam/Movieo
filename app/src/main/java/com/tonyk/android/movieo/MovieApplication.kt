package com.tonyk.android.movieo

import android.app.Application
import com.tonyk.android.movieo.database.MovieDatabaseRepository

class MovieApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        MovieDatabaseRepository.initialize(this)
    }
}