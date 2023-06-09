package com.tonyk.android.movieo.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tonyk.android.movieo.api.MovieApiRepository
import com.tonyk.android.movieo.model.MovieListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class SearchViewModel  @Inject constructor(private val movieApi: MovieApiRepository) : ViewModel() {

    private var _movieListItems: MutableStateFlow<List<MovieListItem>> =
        MutableStateFlow(emptyList())
    val movieListItems: StateFlow<List<MovieListItem>>
        get() = _movieListItems.asStateFlow()

    var currentPage = 1
    var currentQuery = ""
    var isLoading = false


    fun setQuery(query: String) {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                currentPage = 1
                currentQuery = query
                _movieListItems.value = emptyList()
                isLoading = true
            }

            _movieListItems.value = searchMovieItems(query, currentPage)
            withContext(Dispatchers.Main) {
                isLoading = false
                currentPage++
            }
        }
    }

    private suspend fun searchMovieItems(query: String, page: Int): List<MovieListItem> {
        return try {
            if (query.isNotEmpty()) {
                movieApi.searchMovies(query, page)
            } else {
                movieApi.searchMovies("", page)
            }
        } catch (e: HttpException) {
            Log.e("HttpError", "Failed to search for recipe items", e)
            return ArrayList()
        }
    }

    fun loadNextPage(query: String) {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                isLoading = true
            }
            val newItems = searchMovieItems(query, currentPage)
            withContext(Dispatchers.Main) {
                _movieListItems.value = _movieListItems.value + newItems
                currentPage++
                isLoading = false
            }
        }
    }
}