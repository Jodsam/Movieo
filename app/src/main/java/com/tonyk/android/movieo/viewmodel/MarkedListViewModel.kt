package com.tonyk.android.movieo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tonyk.android.movieo.database.MovieDatabaseRepository
import com.tonyk.android.movieo.model.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarkedListViewModel @Inject constructor(private val movieDatabase: MovieDatabaseRepository) : ViewModel() {

    private val _movies: MutableStateFlow<List<Movie>> = MutableStateFlow(emptyList())
    val movies: StateFlow<List<Movie>>
        get() = _movies.asStateFlow()

    init {
        viewModelScope.launch {
            movieDatabase.getMovies().collect {
                _movies.value = it
            }

        }
    }
}