package com.aryk.covid.ui.home

import android.graphics.drawable.ClipDrawable.HORIZONTAL
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aryk.covid.R
import com.aryk.covid.helper.ItemClickSupport
import com.aryk.covid.ui.detail.DetailAndTimelineFragment
import com.aryk.covid.ui.home.adapter.CountryListAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalCoroutinesApi
class HomeFragment : Fragment() {
    companion object {
        private const val FRAGMENT_TAG = "home_fragment"
    }

    private val homeViewModel: HomeViewModel by viewModel()
    private lateinit var countryListAdapter: CountryListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homeViewModel.inputs.onLoadData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        countriesRecyclerView?.apply {
            countryListAdapter = CountryListAdapter()
            layoutManager = LinearLayoutManager(activity)
            adapter = countryListAdapter

            val itemDecor = DividerItemDecoration(context, HORIZONTAL)
            addItemDecoration(itemDecor)

            ItemClickSupport.addTo(this).onItemClickListener =
                object : ItemClickSupport.OnItemClickListener {
                    override fun onItemClicked(recyclerView: RecyclerView, position: Int, v: View) {
                        if (position == RecyclerView.NO_POSITION) {
                            return
                        }

                        homeViewModel.inputs.onCountrySelected(position)
                    }
                }
        }

        countriesSwipeRefresh.setOnRefreshListener {
            countriesRecyclerView?.apply {
                countryListAdapter = CountryListAdapter()
                layoutManager = LinearLayoutManager(activity)
                adapter = countryListAdapter
            }

            errorView.visibility = View.GONE
            homeViewModel.inputs.onLoadData()
            countriesSwipeRefresh.isRefreshing = false
        }

        homeViewModel.outputs.countriesData.observe(viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.let {
                if (it.isEmpty()) {
                    errorView.visibility = View.VISIBLE
                } else {
                    countryListAdapter.submitList(it)
                    countryListAdapter.notifyDataSetChanged()
                }
            }
        })

        homeViewModel.outputs.isLoading.observe(viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.let { isLoading ->
                progressBar.visibility = if (isLoading) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
        })

        homeViewModel.outputs.selectedCountry.observe(viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.let { (countryData, historicalData) ->
                parentFragmentManager
                    .beginTransaction()
                    .addToBackStack(FRAGMENT_TAG)
                    .add(
                        R.id.nav_host_fragment,
                        DetailAndTimelineFragment.newInstance(countryData),
                        "detail_fragment"
                    )
                    .commit()
            }
        })
    }
}
