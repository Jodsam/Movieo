package com.tonyk.android.movieo.view.detailsmovie

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
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
import com.tonyk.android.movieo.viewmodel.MovieDetailsViewModel
import com.tonyk.android.movieo.view.detailsmovie.detaildialogs.RatingDialogFragment
import dagger.hilt.android.AndroidEntryPoint

import kotlinx.coroutines.launch


@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {

    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private val args: MovieDetailsFragmentArgs by navArgs()
    private val movieDetailsViewModel: MovieDetailsViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            FragmentMovieDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setFragmentResultListener(RatingDialogFragment.REQUEST_RATING) { _, bundle ->
            val myratings = bundle.getFloat("rating")
            movieDetailsViewModel.myratings = (myratings*2).toString()
            viewLifecycleOwner.lifecycleScope.launch {
            movieDetailsViewModel.updateMovieRating(args.movieID, myratings) }
            binding.showRatings.visibility = View.VISIBLE
            binding.showRatings.text = getString(R.string.your_rating, movieDetailsViewModel.myratings)
        }

            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    movieDetailsViewModel.getInfo(args.movieID)
                    movieDetailsViewModel.movieDetails.collect {

                            binding.apply {
                            movieTitleD.text = it.title
                            if (it.Poster.length == 3) { binding.moviePoster.load(R.drawable.noposter)
                                pbarPoster.visibility = View.GONE
                                moviePoster.isClickable = false  }

                            else {moviePoster.load(it.Poster) {
                                listener(
                                    onSuccess = { _, _ ->  pbarPoster.visibility = View.GONE  }
                                ) } }

                            plot.text = getString(R.string.plot, it.plot)
                            country.text = getString(R.string.country, it.Country)
                            language.text = getString(R.string.language, it.Language)
                            date.text = getString(R.string.release_date, it.date)
                            time.text = getString(R.string.runtime, it.runtime)
                            val ratings = it.Ratings
                            if (ratings.isNotEmpty()) {
                                val firstRating = ratings[0]
                                rating.text =
                                    getString(R.string.ratings, firstRating.rating)
                            } else rating.text = getString(R.string.ratings, "N/A")
                            actors.text = getString(R.string.actors_with_label, it.Actors)
                            genre.text = getString(R.string.genre, it.Genre)
                            director.text = getString(R.string.director, it.Director)

                            if (movieDetailsViewModel.myratings != "N/A") {
                                showRatings.visibility = View.VISIBLE
                                showRatings.text =
                                    getString(R.string.your_rating, movieDetailsViewModel.myratings)
                            }
                            setButtonState()


                        }
                    }
                }
            }
        binding.moviePoster.setOnClickListener {
            if (movieDetailsViewModel.isDataLoaded) {
                val currentDestination = findNavController().currentDestination
                if (currentDestination?.id == R.id.MovieDetailsFragment) {
                    findNavController().navigate(
                        MovieDetailsFragmentDirections.checkPoster(
                            movieDetailsViewModel.movieDetails.value.Poster
                        )
                    )
                }
            }
        }

        binding.rateMovieButton.setOnClickListener {
            if (movieDetailsViewModel.isDataLoaded) {
            findNavController().navigate(MovieDetailsFragmentDirections.rateMovie()) } }

        binding.sawMovieButton.setOnClickListener {
                if (movieDetailsViewModel.isDataLoaded) {
                viewLifecycleOwner.lifecycleScope.launch {

                    movieDetailsViewModel.addMovie(args.movieID, false, true)
                    setButtonState()
                    Toast.makeText(context, if (movieDetailsViewModel.alreadySaw) "Marked As Watched" else "Marked As Not Watched", Toast.LENGTH_SHORT).show()
                     }
                }
        }
        binding.saveMovieButton.setOnClickListener {
            if (movieDetailsViewModel.isDataLoaded) {
                viewLifecycleOwner.lifecycleScope.launch {
                    movieDetailsViewModel.addMovie(args.movieID, true, false)
                    setButtonState()
                    Toast.makeText(context, if (movieDetailsViewModel.isAddedToWatchLists) "Added To WatchList" else "Removed From WatchList", Toast.LENGTH_SHORT).show()
                    }
                }
        }
        binding.movieShareButton.setOnClickListener { val shareIntent = Intent(Intent.ACTION_SEND).apply {
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
    private fun setButtonState() {
        val context = requireContext()

        val sawButtonTextRes = if (movieDetailsViewModel.alreadySaw) R.string.watched_mark else R.string.not_watched_mark
        binding.sawMovieButton.setText(sawButtonTextRes)
        binding.sawMovieButton.setTextColor(ContextCompat.getColor(context, if (movieDetailsViewModel.alreadySaw) R.color.green else R.color.yellow))

        val saveButtonTextRes = if (movieDetailsViewModel.isAddedToWatchLists) R.string.added_to_watchlist else R.string.add_watchlist
        binding.saveMovieButton.setText(saveButtonTextRes)
        binding.saveMovieButton.setTextColor(ContextCompat.getColor(context, if (movieDetailsViewModel.isAddedToWatchLists) R.color.green else R.color.yellow))
    }
}
