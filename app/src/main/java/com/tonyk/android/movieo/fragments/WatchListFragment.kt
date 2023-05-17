package com.tonyk.android.movieo.fragments

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
import com.tonyk.android.movieo.adapters.WatchListAdapter
import com.tonyk.android.movieo.databinding.FragmentWatchlistBinding
import com.tonyk.android.movieo.viewmodels.MyListViewModel
import kotlinx.coroutines.launch

class WatchListFragment: Fragment() {
    private val myListViewModel: MyListViewModel by viewModels()
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
                myListViewModel.movies.collect {  movies ->
                    val watchListMovies = movies.filter { it.isAddedtoWatchList }
                    binding.watchlistRc.adapter = WatchListAdapter(watchListMovies) { imdbID ->
                        findNavController().navigate(WatchListFragmentDirections.actionWatchListFragmentToMovieDetailsFragment(imdbID)) }
                }

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}