package com.aryk.covid.ui.detail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.aryk.covid.MainActivity
import com.aryk.covid.R
import com.aryk.covid.ui.detail.adapters.DetailAndTimelineFragmentStatePagerAdapter
import com.aryk.network.models.ningaApi.CountryData
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_detail_and_timeline.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@SuppressWarnings("ForbiddenComment")
@ExperimentalCoroutinesApi
class DetailAndTimelineFragment : Fragment() {
    companion object {
        private const val ARG_SELECTED_COUNTRY = "selected_country"

        fun newInstance(
            country: CountryData
        ): DetailAndTimelineFragment {
            val args: Bundle = Bundle()
            args.putParcelable(ARG_SELECTED_COUNTRY, country)
            val fragment = DetailAndTimelineFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var detailAndTimelinePagerAdapter: DetailAndTimelineFragmentStatePagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail_and_timeline, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getParcelable<CountryData>(ARG_SELECTED_COUNTRY)?.let {
            detailAndTimelinePagerAdapter =
                DetailAndTimelineFragmentStatePagerAdapter(
                    childFragmentManager,
                    it
                )
        } ?: kotlin.run {
            // TODO: Data Missing, Handle this case
        }

        container.adapter = detailAndTimelinePagerAdapter

        tabs.addOnTabSelectedListener(
            TabLayout.ViewPagerOnTabSelectedListener(container)
        )
        container.addOnPageChangeListener(
            TabLayout.TabLayoutOnPageChangeListener(tabs)
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).supportActionBar?.setDisplayShowHomeEnabled(true)

        arguments?.getParcelable<CountryData>(ARG_SELECTED_COUNTRY)?.let {
            activity?.title = it.country
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        (activity as MainActivity).supportActionBar?.setDisplayShowHomeEnabled(false)

        activity?.title = getString(R.string.app_name)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.sortBy).isVisible = false
        menu.findItem(R.id.aboutApp).isVisible = false
    }
}
