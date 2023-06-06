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
import androidx.viewpager2.widget.ViewPager2
import coil.load
import com.google.android.material.tabs.TabLayoutMediator
import com.tonyk.android.movieo.R
import com.tonyk.android.movieo.databinding.FragmentMainPageBinding
import com.tonyk.android.movieo.viewmodel.MainPageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainPageFragment : Fragment()  {


    private val mainpageViewModel: MainPageViewModel by viewModels()

    private var _binding: FragmentMainPageBinding? = null
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
            FragmentMainPageBinding.inflate(inflater, container, false)



        return binding.root }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.botNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.action_search -> { findNavController().navigate(R.id.main_to_search) }
                R.id.action_saved -> { findNavController().navigate(R.id.main_to_watchlist) }
                R.id.action_watched -> { findNavController().navigate(R.id.main_to_watchedList)}
            }
            true
        }
        mainpageViewModel.loadFeaturedMovie()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainpageViewModel.featuredMovie.collect  {movie ->
                    if (movie != null) {

                        binding.apply {
                        mainTitle.text = movie.title
                        mainDate.text = movie.date
                        mainPlot.text = movie.plot
                        mainPoster.load(movie.Poster)
                        mainPoster.setOnClickListener {
                            findNavController().navigate(
                                MainPageFragmentDirections.mainToDetails(
                                    movie.imdbID
                                )
                            )
                        }
                    }

                    }
                }
            }
        }

        val viewPager: ViewPager2 = binding.vp2
        val adapter = VpAdapter(childFragmentManager, lifecycle)
        viewPager.adapter = adapter
        TabLayoutMediator(binding.mainTabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "New Releases"
                1 -> tab.text = "Recommended"
            }
        }.attach()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
