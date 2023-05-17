package com.tonyk.android.movieo.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.tonyk.android.movieo.databinding.MainPageMovieItemBinding
import com.tonyk.android.movieo.model.MovieDetailItem

class MainListViewHolder(private val binding: MainPageMovieItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(movie: MovieDetailItem, onMovieClicked: (imdbID: String) -> Unit) {
        binding.movieTitleMain.text = movie.title
        binding.moviePosterMain.load(movie.Poster)
        binding.root.setOnClickListener { onMovieClicked(movie.imdbID) }
    }
}

class MainListAdapter(
    private val onMovieClicked: (imdbID: String) -> Unit
) : ListAdapter<MovieDetailItem, MainListViewHolder>(MovieDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MainPageMovieItemBinding.inflate(inflater, parent, false)
        return MainListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainListViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onMovieClicked)
    }
}

class MovieDiffCallback : DiffUtil.ItemCallback<MovieDetailItem>() {
    override fun areItemsTheSame(oldItem: MovieDetailItem, newItem: MovieDetailItem): Boolean {
        return oldItem.imdbID == newItem.imdbID
    }

    override fun areContentsTheSame(oldItem: MovieDetailItem, newItem: MovieDetailItem): Boolean {
        return oldItem == newItem
    }
}
