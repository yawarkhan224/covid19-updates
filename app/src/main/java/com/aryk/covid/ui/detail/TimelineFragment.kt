package com.aryk.covid.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.aryk.covid.R
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.fragment_timeline.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel


/**
 * A simple [Fragment] subclass.
 */
@ExperimentalCoroutinesApi
class TimelineFragment : Fragment() {
    companion object {
        private const val ARG_SELECTED_COUNTRY_ISO2 = "selected_country_iso2"

        fun newInstance(
            iso2: String?
        ): TimelineFragment {
            val args: Bundle = Bundle()
            iso2?.let {
                args.putString(ARG_SELECTED_COUNTRY_ISO2, iso2)
            }
            val fragment = TimelineFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private val timelineViewModel: TimelineViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_timeline, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getString(ARG_SELECTED_COUNTRY_ISO2)?.let {
            timelineViewModel.inputs.onLoadHistoricalData(it)
        } ?: kotlin.run {
            // TODO: Data Missing, Handle this case
        }

        timelineViewModel.outputs.historicalData.observe(viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.let { timelineData ->

                timelineData.let { map ->
                    val c = map.cases.keys
                    val d = map.deaths.keys
                    val r = map.recovered.keys

                    val casesSet: MutableList<Entry> = mutableListOf()
                    val deathsSet: MutableList<Entry> = mutableListOf()
                    val recoveredSet: MutableList<Entry> = mutableListOf()
                    c.forEach {
                        casesSet.add(Entry(it, map.cases[it]!!))
                    }
                    d.forEach {
                        deathsSet.add(Entry(it, map.deaths[it]!!))
                    }
                    r.forEach {
                        recoveredSet.add(Entry(it, map.recovered[it]!!))
                    }

                    val xAxis: XAxis = timelineChart.xAxis
                    xAxis.position = XAxisPosition.BOTTOM_INSIDE

                    timelineChart.setDrawBorders(false)
                    timelineChart.setDrawGridBackground(false)
                    timelineChart.data = LineData(
                        LineDataSet(casesSet, "cases"),
                        LineDataSet(deathsSet, "deaths"),
                        LineDataSet(recoveredSet, "recovered")
                    )
                }
            }
        })
    }
}
