package com.tonyk.android.movieo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tonyk.android.movieo.api.MovieApiRepository
import com.tonyk.android.movieo.model.MovieDetailsItem
import com.tonyk.android.movieo.firebase.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainPageViewModel @Inject constructor(private val movieApi: MovieApiRepository, private val firebaseRepository: FirebaseRepository)  : ViewModel() {


    private val _movies: MutableStateFlow<List<MovieDetailsItem>> = MutableStateFlow(emptyList())
    val movies: StateFlow<List<MovieDetailsItem>> = _movies

    private val _newmovies: MutableStateFlow<List<MovieDetailsItem>> = MutableStateFlow(emptyList())
    val newmovies: StateFlow<List<MovieDetailsItem>> = _newmovies

    private val _featuredMovie = MutableStateFlow<MovieDetailsItem?>(null)
    val featuredMovie: StateFlow<MovieDetailsItem?> = _featuredMovie

    fun loadMovies() {
        viewModelScope.launch {
            val imdbIds = firebaseRepository.getMoviesFromFirebase()

            imdbIds.collectLatest { ids ->
                val movieList = mutableListOf<MovieDetailsItem>()

                ids.forEach { imdbId ->
                    val movie = movieApi.searchDetails(imdbId)
                    if (movie != null) {
                        movieList.add(movie)
                    }
                }

                _movies.value = movieList.toList()
            }
        }
    }
    fun loadNewMovies() {
        viewModelScope.launch {
            val imdbIds = firebaseRepository.getNewMoviesFromFirebase()

            imdbIds.collectLatest { ids ->
                val movieList = mutableListOf<MovieDetailsItem>()
                ids.forEach { imdbId ->
                    val movie = movieApi.searchDetails(imdbId)
                    if (movie != null) {
                        movieList.add(movie)
                    }
                }
                _newmovies.value = movieList.toList()
            }
        }
    }

    fun loadFeaturedMovie() {
        viewModelScope.launch {
             val imdbID = firebaseRepository.getFeaturedMovieFromFirebase()
            imdbID.collect {
                _featuredMovie.value = it?.let { it1 -> movieApi.searchDetails(it1) }
            }
        }
    }
}
