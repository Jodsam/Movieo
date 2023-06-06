package com.tonyk.android.movieo.view.markedmovies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.tonyk.android.movieo.databinding.MovieItemBinding
import com.tonyk.android.movieo.model.Movie


class MarkedListViewHolder (
    private val binding: MovieItemBinding
) : RecyclerView.ViewHolder(binding.root){
    fun bind(movie: Movie, onMovieClicked: (imdbID: String) -> Unit) {
        binding.movieTitle.text = movie.title
        binding.movieYear.text = movie.myRating
        binding.recImage.load(movie.poster)
        binding.root.setOnClickListener {  onMovieClicked(movie.imdbID) }
    }
}

class MarkedListAdapter (
    private val movieList: List<Movie>,
    private val onMovieClicked: (imdbID: String) -> Unit
) : RecyclerView.Adapter<MarkedListViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MarkedListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MovieItemBinding.inflate(inflater, parent, false)
        return MarkedListViewHolder(binding)
    }
    override fun onBindViewHolder(holder: MarkedListViewHolder, position: Int) {
        val item = movieList[position]
        holder.bind(item, onMovieClicked)
    }
    override fun getItemCount() = movieList.size

}