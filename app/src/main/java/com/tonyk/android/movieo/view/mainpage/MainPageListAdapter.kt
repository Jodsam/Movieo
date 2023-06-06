package com.tonyk.android.movieo.view.mainpage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.tonyk.android.movieo.databinding.MainPageMovieItemBinding
import com.tonyk.android.movieo.model.MovieDetailsItem

class MainListViewHolder(private val binding: MainPageMovieItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(movie: MovieDetailsItem, onMovieClicked: (imdbID: String) -> Unit) {
        binding.movieTitleMain.text = movie.title
        binding.moviePosterMain.load(movie.Poster)
        binding.root.setOnClickListener { onMovieClicked(movie.imdbID) }
    }
}

class MainListAdapter(
    private val onMovieClicked: (imdbID: String) -> Unit
) : ListAdapter<MovieDetailsItem, MainListViewHolder>(MovieDiffCallback()) {

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

class MovieDiffCallback : DiffUtil.ItemCallback<MovieDetailsItem>() {
    override fun areItemsTheSame(oldItem: MovieDetailsItem, newItem: MovieDetailsItem): Boolean {
        return oldItem.imdbID == newItem.imdbID
    }

    override fun areContentsTheSame(oldItem: MovieDetailsItem, newItem: MovieDetailsItem): Boolean {
        return oldItem == newItem
    }
}
