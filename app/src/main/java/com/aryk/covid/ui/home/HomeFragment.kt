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
import com.aryk.covid.R
import com.aryk.covid.ui.home.adapter.CountryListAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModel()
    private val countryListAdapter: CountryListAdapter = CountryListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        countriesRecyclerView?.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = countryListAdapter

            val itemDecor = DividerItemDecoration(context, HORIZONTAL)
            addItemDecoration(itemDecor)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homeViewModel.getData.observe(this, Observer {
            countryListAdapter.submitList(it)
            countryListAdapter.notifyDataSetChanged()
        })
    }
}
