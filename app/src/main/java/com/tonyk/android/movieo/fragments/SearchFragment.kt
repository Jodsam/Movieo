package com.tonyk.android.movieo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.tonyk.android.movieo.R
import com.tonyk.android.movieo.viewmodels.SearchViewModel
import com.tonyk.android.movieo.adapters.MovieSearchAdapter
import com.tonyk.android.movieo.databinding.FragmentSearchBinding
import kotlinx.coroutines.launch


class SearchFragment : Fragment()  {
    private var _binding: FragmentSearchBinding? = null
    private val binding
        get () = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }
    private val searchViewModel: SearchViewModel by viewModels()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            FragmentSearchBinding.inflate(inflater, container, false)



        return binding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        val layoutManager = LinearLayoutManager(context)
        binding.searchList.layoutManager = layoutManager
        val navController = findNavController()
        val adapter = MovieSearchAdapter(navController)
        binding.searchList.adapter = adapter
        binding.searchList.addOnScrollListener(scrollListener)

        val searchView = binding.searchView
        val queryTextListener: SearchView.OnQueryTextListener = object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null && searchViewModel.currentQuery != query) {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.logoImage.visibility = View.GONE
                    binding.sloganText.visibility = View.GONE
                    binding.withText.visibility = View.GONE
                searchViewModel.setQuery(query) }
                searchView.clearFocus()

                return false
            }
        }


        searchView.setOnQueryTextListener(queryTextListener)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchViewModel.movieListItems.collect {
                    items ->
                    adapter.submitList(items)
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
            val totalItemCount = layoutManager.itemCount
            if (lastVisibleItemPosition >= totalItemCount - 2 && !searchViewModel.isLoading) {
                    binding.progressBar.visibility = View.VISIBLE
                    searchViewModel.loadNextPage(searchViewModel.currentQuery)
            }
            if (lastVisibleItemPosition == totalItemCount- 1) {
                binding.progressBar.visibility = View.GONE
            }

        }
    }
}
