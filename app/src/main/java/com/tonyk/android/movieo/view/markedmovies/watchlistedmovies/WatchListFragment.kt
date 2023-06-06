package com.tonyk.android.movieo.view.markedmovies.watchlistedmovies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tonyk.android.movieo.view.markedmovies.MarkedListAdapter
import com.tonyk.android.movieo.databinding.FragmentWatchlistBinding
import com.tonyk.android.movieo.viewmodel.MarkedListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WatchListFragment: Fragment() {
    private val markedListViewModel: MarkedListViewModel by viewModels()
    private var _binding: FragmentWatchlistBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWatchlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val rcView = binding.watchlistRc
        val layoutManager = LinearLayoutManager(context)
        rcView.layoutManager = layoutManager


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                markedListViewModel.movies.collect { movies ->
                    val watchListMovies = movies.filter { it.isAddedtoWatchList }
                    binding.watchlistRc.adapter = MarkedListAdapter(watchListMovies) { imdbID ->
                        findNavController().navigate(
                            WatchListFragmentDirections.actionWatchListFragmentToMovieDetailsFragment(
                                imdbID
                            )
                        ) }
                }

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}