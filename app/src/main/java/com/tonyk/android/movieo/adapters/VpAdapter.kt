package com.tonyk.android.movieo.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tonyk.android.movieo.fragments.NewMoviesFragment
import com.tonyk.android.movieo.fragments.RecommendedFragment

class VpAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> NewMoviesFragment()
            1 -> RecommendedFragment()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}
