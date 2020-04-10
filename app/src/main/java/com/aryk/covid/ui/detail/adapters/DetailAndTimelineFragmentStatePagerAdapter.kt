package com.aryk.covid.ui.detail.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.aryk.covid.ui.detail.DetailFragment
import com.aryk.covid.ui.detail.TimelineFragment
import com.aryk.network.models.ningaApi.CountryData
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * FragmentManager class for Detail and Timeline views
 *
 * @param fragmentManager [FragmentManager] instance to manage fragments
 */
@ExperimentalCoroutinesApi
class DetailAndTimelineFragmentStatePagerAdapter(
    fragmentManager: FragmentManager,
    private val countryData: CountryData
) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return if (position == 0) {
            DetailFragment.newInstance(countryData)
        } else {
            TimelineFragment.newInstance(countryData)
        }
    }

    override fun getCount(): Int {
        return 2
    }
}
