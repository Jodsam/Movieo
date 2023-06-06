package com.tonyk.android.movieo.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tonyk.android.movieo.api.MovieApiRepository
import com.tonyk.android.movieo.database.MovieDatabaseRepository
import com.tonyk.android.movieo.model.Movie
import com.tonyk.android.movieo.model.MovieDetailsItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(private val movieApi: MovieApiRepository, state: SavedStateHandle, private val movieDB: MovieDatabaseRepository): ViewModel() {

    var myratings = "N/A"
    var isAddedToWatchLists = false
    var alreadySaw = false
    var isDataLoaded = false
    val imdbID = state.get<String>("movieID")

    private val _movieDetails: MutableStateFlow<MovieDetailsItem> =
        MutableStateFlow(
            MovieDetailsItem(
                "", "", "", "", "", "",
                emptyList(), "", "", "", "", ""
            )
        )
    val movieDetails: StateFlow<MovieDetailsItem> = _movieDetails.asStateFlow()


    init {
        viewModelScope.launch{
                _movieDetails.value = imdbID?.let { movieApi.searchDetails(it) }!!
                isDataLoaded = true
            }
    }
    suspend fun addMovie(imdbID: String, isAddedToWatchList: Boolean, isWatched: Boolean) {
        val movie = movieDB.getMovie(imdbID) ?: null
        if (movie == null) {
            val newMovie = Movie(
                movieDetails.value.imdbID,
                movieDetails.value.title,
                movieDetails.value.Poster,
                movieDetails.value.date,
                myratings,
                isAddedToWatchList,
                isWatched
            )
            movieDB.addMovie(newMovie)
            isAddedToWatchLists = isAddedToWatchList
            alreadySaw = isWatched
        }
        else  {
            if (isAddedToWatchLists == isAddedToWatchList && alreadySaw == isWatched) {
                deleteMovie(movie)
                isAddedToWatchLists = false
                alreadySaw = false
         }   else { isAddedToWatchLists = isAddedToWatchList
                alreadySaw = isWatched
                movie.isAddedtoWatchList = isAddedToWatchLists
                movie.isWatched = alreadySaw
                movieDB.updateMovie(movie) }

                                     }
    }

    suspend fun getInfo(imdbID: String) {
        val movie = movieDB.getMovie(imdbID)
        if (movie != null) { myratings = movie.myRating
        isAddedToWatchLists = movie.isAddedtoWatchList
        alreadySaw = movie.isWatched   }
    }

    suspend fun updateMovieRating(imdbID: String, rating: Float) {
        val movie = movieDB.getMovie(imdbID)
        if (movie != null) {
            movie.myRating = (rating*2).toString()
            myratings = movie.myRating
            movieDB.updateMovie(movie)
        }
    }

    private suspend fun deleteMovie(movie: Movie){
        movieDB.deleteMovie(movie)
    }
}




