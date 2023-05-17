package com.tonyk.android.movieo.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tonyk.android.movieo.repositories.MovieApiRepository
import com.tonyk.android.movieo.model.MovieDetailItem
import com.tonyk.android.movieo.repositories.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class MainPageViewModel : ViewModel() {
    private val firebaseRepository = FirebaseRepository()
    private val movieApiRepository = MovieApiRepository()

    private val _movies: MutableStateFlow<List<MovieDetailItem>> = MutableStateFlow(emptyList())
    val movies: StateFlow<List<MovieDetailItem>> = _movies

    private val _newmovies: MutableStateFlow<List<MovieDetailItem>> = MutableStateFlow(emptyList())
    val newmovies: StateFlow<List<MovieDetailItem>> = _newmovies

    private val _featuredMovie = MutableStateFlow<MovieDetailItem?>(null)
    val featuredMovie: StateFlow<MovieDetailItem?> = _featuredMovie

    fun loadMovies() {
        viewModelScope.launch {
            val imdbIds = firebaseRepository.getMoviesFromFirebase()

            imdbIds.collectLatest { ids ->
                val movieList = mutableListOf<MovieDetailItem>()

                ids.forEach { imdbId ->
                    val movie = movieApiRepository.searchDetails(imdbId)
                    movieList.add(movie)
                }

                _movies.value = movieList.toList()
            }
        }
    }
    fun loadNewMovies() {
        viewModelScope.launch {
            val imdbIds = firebaseRepository.getNewMoviesFromFirebase()

            imdbIds.collectLatest { ids ->
                val movieList = mutableListOf<MovieDetailItem>()
                ids.forEach { imdbId ->
                    val movie = movieApiRepository.searchDetails(imdbId)
                    movieList.add(movie)
                }
                _newmovies.value = movieList.toList()
            }
        }
    }

    fun loadFeaturedMovie() {
        viewModelScope.launch {
             val imdbID = firebaseRepository.getFeaturedMovieFromFirebase()
            imdbID.collect {
                _featuredMovie.value = it?.let { it1 -> movieApiRepository.searchDetails(it1) }
            }
        }
    }
}
