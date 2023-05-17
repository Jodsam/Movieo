package com.tonyk.android.movieo.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.tonyk.android.movieo.R
import com.tonyk.android.movieo.databinding.MovieItemBinding
import com.tonyk.android.movieo.fragments.SearchFragmentDirections
import com.tonyk.android.movieo.model.MovieListItem

class MovieSearchViewHolder(private val binding: MovieItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(movieListItem: MovieListItem, navController: NavController) {
        val log = movieListItem.posterUrl.length
        if (log == 3) binding.recImage.load(R.drawable.noposter)
        else binding.recImage.load(movieListItem.posterUrl)
        binding.movieTitle.text = movieListItem.title
        binding.movieType.text = movieListItem.type
        binding.movieYear.text = movieListItem.year
        binding.root.setOnClickListener {
            val action = SearchFragmentDirections.searchFragmentToDetailsFragment(movieListItem.imdbId)
            navController.navigate(action)
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