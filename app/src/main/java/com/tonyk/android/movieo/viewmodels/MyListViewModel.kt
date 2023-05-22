package com.tonyk.android.movieo.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tonyk.android.movieo.database.MovieDatabaseRepository
import com.tonyk.android.movieo.model.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MyListViewModel : ViewModel() {

    private val movieDatabaseRepository = MovieDatabaseRepository.get()

    private val _movies: MutableStateFlow<List<Movie>> = MutableStateFlow(emptyList())
    val movies: StateFlow<List<Movie>>
        get() = _movies.asStateFlow()

    init {
        viewModelScope.launch {
            movieDatabaseRepository.getMovies().collect {
                _movies.value = it
            }

        }
    }
}