package com.tonyk.android.movieo.fragments




import android.content.Intent
import android.os.Bundle
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.tonyk.android.movieo.R
import com.tonyk.android.movieo.databinding.FragmentMovieDetailsBinding
import com.tonyk.android.movieo.viewmodels.MovieDetailViewModel
import com.tonyk.android.movieo.viewmodels.MovieDetailViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException


class MovieDetailsFragment : Fragment() {


    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private val args: MovieDetailsFragmentArgs by navArgs()
    private val movieDetailViewModel: MovieDetailViewModel by viewModels() {
        MovieDetailViewModelFactory(args.movieID)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            FragmentMovieDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        setFragmentResultListener(RatingDialogFragment.REQUEST_RATING) { _, bundle ->
            val myratings = bundle.getFloat("rating")
            movieDetailViewModel.myratings = (myratings*2).toString()
            viewLifecycleOwner.lifecycleScope.launch {
            movieDetailViewModel.updateMovieRating(args.movieID, myratings) }
            binding.ocenka.visibility = View.VISIBLE
            binding.ocenka.text = getString(R.string.your_rating, movieDetailViewModel.myratings)
        }

            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    movieDetailViewModel.getInfo(args.movieID)
                    movieDetailViewModel.movieDetails.collect {
                        withContext(Dispatchers.Main) {

                            binding.movieTitleD.text = it.title
                            if (it.Poster.length == 3) binding.moviePhoto.load(R.drawable.noposter)
                            else binding.moviePhoto.load(it.Poster)
                            binding.plot.text = getString(R.string.plot, it.plot)
                            binding.counry.text = getString(R.string.country, it.Country)
                            binding.language.text = getString(R.string.language, it.Language)
                            binding.date.text = getString(R.string.release_date, it.date)
                            binding.time.text = getString(R.string.runtime, it.runtime)
                            val ratings = it.Ratings
                            if (ratings.isNotEmpty()) {
                                val firstRating = ratings[0]
                                binding.rating.text =
                                    getString(R.string.ratings, firstRating.rating)
                            } else binding.rating.text = getString(R.string.ratings, "N/A")
                            binding.actors.text = getString(R.string.actors_with_label, it.Actors)
                            binding.genre.text = getString(R.string.genre, it.Genre)
                            binding.director.text = getString(R.string.director, it.Director)

                            if (movieDetailViewModel.myratings != "N/A") {
                                binding.ocenka.visibility = View.VISIBLE
                                binding.ocenka.text =
                                    getString(R.string.your_rating, movieDetailViewModel.myratings)
                            }

                            binding.saveShit.load(if (movieDetailViewModel.isAddedToWatchLists) R.drawable.ic_checked else R.drawable.movie_bookmark)
                            binding.sawButton.load(if (movieDetailViewModel.alreadySaw) R.drawable.ic_watched else R.drawable.ic_saw)


                        }

                    }
                }
            }
        binding.moviePhoto.setOnClickListener {
            findNavController().navigate(MovieDetailsFragmentDirections.checkPoster(movieDetailViewModel.movieDetails.value.Poster))
        }


        binding.rateShit.setOnClickListener {
            findNavController().navigate(MovieDetailsFragmentDirections.rateMovie()) }


        binding.sawButton.setOnClickListener {
                if (movieDetailViewModel.isDataLoaded) {
                viewLifecycleOwner.lifecycleScope.launch {
                    movieDetailViewModel.addMovie(args.movieID, false, true)
                    binding.sawButton.load(if (movieDetailViewModel.alreadySaw) R.drawable.ic_watched else R.drawable.ic_saw)
                    binding.saveShit.load(if (movieDetailViewModel.isAddedToWatchLists) R.drawable.ic_checked else R.drawable.movie_bookmark) }
                }
        }

        binding.saveShit.setOnClickListener {
            if (movieDetailViewModel.isDataLoaded) {
                viewLifecycleOwner.lifecycleScope.launch {
                    movieDetailViewModel.addMovie(args.movieID, true, false)
                    binding.saveShit.load(if (movieDetailViewModel.isAddedToWatchLists) R.drawable.ic_checked else R.drawable.movie_bookmark)
                    binding.sawButton.load(if (movieDetailViewModel.alreadySaw) R.drawable.ic_watched else R.drawable.ic_saw) }
                }

        }
        binding.movieShare.setOnClickListener { val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"

            putExtra(Intent.EXTRA_TEXT,
                getString(R.string.share_movie,args.movieID)  )

        }
            startActivity(shareIntent) }



    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
