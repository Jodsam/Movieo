package com.tonyk.android.movieo.view.mainpage

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
import androidx.recyclerview.widget.GridLayoutManager
import com.tonyk.android.movieo.databinding.FragmentRecommendedBinding
import com.tonyk.android.movieo.viewmodel.MainPageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class RecommendedFragment: Fragment()  {

    private val mainpageViewModel: MainPageViewModel by viewModels()
    private var _binding: FragmentRecommendedBinding? = null
    private val binding
        get () = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            FragmentRecommendedBinding.inflate(inflater, container, false)
        return binding.root }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        mainpageViewModel.loadMovies()

        val rcView = binding.recommendedRcv
        val layoutManager = GridLayoutManager(context, 2)
        rcView.layoutManager = layoutManager
        val adapter = MainListAdapter { imdbID ->
            findNavController().navigate(MainPageFragmentDirections.mainToDetails(imdbID))
        }
        rcView.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launch {

            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainpageViewModel.movies.collectLatest() {
                      movieList ->
                        adapter.submitList(movieList)

                }
            }
        }
    }





    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
