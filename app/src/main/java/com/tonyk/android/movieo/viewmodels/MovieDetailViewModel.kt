package com.tonyk.android.movieo.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tonyk.android.movieo.repositories.MovieApiRepository
import com.tonyk.android.movieo.database.MovieDatabaseRepository
import com.tonyk.android.movieo.model.Movie
import com.tonyk.android.movieo.model.MovieDetailItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MovieDetailViewModel(imdbID: String): ViewModel() {
    private val movieRepository = MovieApiRepository()
    private val movieDB = MovieDatabaseRepository.get()
    var myratings = "N/A"

    var isAddedToWatchLists = false
    var alreadySaw = false

    var isDataLoaded = false


    private val _movieDetails: MutableStateFlow<MovieDetailItem> =
        MutableStateFlow<MovieDetailItem>(
            MovieDetailItem(
                "", "", "", "", "", "",
                emptyList(), "", "", "", "", ""
            )
        )
    val movieDetails: StateFlow<MovieDetailItem> = _movieDetails.asStateFlow()


    init {
        viewModelScope.launch{
                _movieDetails.value = movieRepository.searchDetails(imdbID)
                isDataLoaded = true
            }
    }
    suspend fun addMovie(imdbID: String, isAddedToWatchList: Boolean, isWatched: Boolean) {
        val movie = movieDB.getMovie(imdbID)

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



    class MovieDetailViewModelFactory(
        private val imdbID: String
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MovieDetailViewModel(imdbID) as T
        }
    }
