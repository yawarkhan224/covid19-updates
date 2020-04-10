package com.aryk.covid.ui.home

import android.graphics.drawable.ClipDrawable.HORIZONTAL
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aryk.covid.R
import com.aryk.covid.enums.CountriesSortType
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
        private const val SORT_SPINNER = "sort_spinner"
    }

    private val homeViewModel: HomeViewModel by viewModel()
    private lateinit var countryListAdapter: CountryListAdapter
    private lateinit var spinner: Spinner
    private var selected = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        homeViewModel.inputs.onLoadData(getSortByParam(savedInstanceState))
    }

    private fun getSortByParam(
        savedInstanceState: Bundle?
    ): String? {
        var sortBy: String? = null

        savedInstanceState?.getInt(SORT_SPINNER)?.let {
            selected = it
            val x = (resources.getStringArray(R.array.sort_countries_list_by))[it]
            sortBy = CountriesSortType.fromSorterName(x)!!.key
        } ?: run {
            if (selected >= 0) {
                val x = (resources.getStringArray(R.array.sort_countries_list_by))[selected]
                sortBy = CountriesSortType.fromSorterName(x)!!.key
            }
        }
        return sortBy
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
            homeViewModel.inputs.onLoadData(getSortByParam(null))
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
            event.getContentIfNotHandled()?.let { countryData ->
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

    @SuppressWarnings("EmptyFunctionBlock")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)

        val item = menu.findItem(R.id.sortBy)
        spinner = item.actionView as Spinner

        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.sort_countries_list_by,
            R.layout.branch_spinner_item
        )
        adapter.setDropDownViewResource(R.layout.branch_spinner_checked_item)

        spinner.adapter = adapter

        if (selected != -1) {
            spinner.setSelection(selected)
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (selected == position) {
                    return
                }

                selected = position

                val x = (resources.getStringArray(R.array.sort_countries_list_by))[position]
                homeViewModel.inputs.onLoadData(CountriesSortType.fromSorterName(x)!!.key)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SORT_SPINNER, spinner.selectedItemPosition)
    }
}
