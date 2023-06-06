package com.tonyk.android.movieo.view.searchmovies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.tonyk.android.movieo.R
import com.tonyk.android.movieo.databinding.MovieItemBinding

import com.tonyk.android.movieo.model.MovieListItem

class MovieSearchViewHolder(private val binding: MovieItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(movieListItem: MovieListItem, navController: NavController) {
        binding.apply {
        if (movieListItem.posterUrl.length == 3) recImage.load(R.drawable.noposter)
        else recImage.load(movieListItem.posterUrl)
        movieTitle.text = movieListItem.title
        movieType.text = movieListItem.type
        movieYear.text = movieListItem.year
        root.setOnClickListener {
            val action =
                SearchFragmentDirections.searchFragmentToDetailsFragment(movieListItem.imdbId)
            navController.navigate(action)
        }
        }
    }
}

class MovieSearchAdapter(private val navController: NavController) :
    ListAdapter<MovieListItem, MovieSearchViewHolder>(MovieItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieSearchViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MovieItemBinding.inflate(inflater, parent, false)
        return MovieSearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieSearchViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, navController)

    }
}

class MovieItemDiffCallback : DiffUtil.ItemCallback<MovieListItem>() {
    override fun areItemsTheSame(oldItem: MovieListItem, newItem: MovieListItem): Boolean {
        return oldItem.imdbId == newItem.imdbId
    }

    override fun areContentsTheSame(oldItem: MovieListItem, newItem: MovieListItem): Boolean {
        return oldItem == newItem
    }
}