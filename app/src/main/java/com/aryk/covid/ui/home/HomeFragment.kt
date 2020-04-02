package com.aryk.covid.ui.home

import android.graphics.drawable.ClipDrawable.HORIZONTAL
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aryk.covid.R
import com.aryk.covid.helper.ItemClickSupport
import com.aryk.covid.ui.home.adapter.CountryListAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.consumeEach
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalCoroutinesApi
class HomeFragment : Fragment() {

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

                        homeViewModel.inputs.onCountryItemClicked(position)
                    }
                }
        }

        countriesSwipeRefresh.setOnRefreshListener {
            countriesRecyclerView?.apply {
                countryListAdapter = CountryListAdapter()
                layoutManager = LinearLayoutManager(activity)
                adapter = countryListAdapter
            }

            homeViewModel.inputs.onLoadData()
            countriesSwipeRefresh.isRefreshing = false
        }

        homeViewModel.outputs.countriesData.observe(viewLifecycleOwner, Observer {
            countryListAdapter.submitList(it)
            countryListAdapter.notifyDataSetChanged()
        })

        lifecycleScope.launchWhenStarted {
            homeViewModel.outputs.isLoading.consumeEach { isLoading ->
                progressBar.visibility = if (isLoading) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
        }
    }
}
