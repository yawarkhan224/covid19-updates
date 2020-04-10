package com.aryk.covid.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import coil.api.load
import com.aryk.covid.R
import com.aryk.covid.helper.DeviceHelper
import com.aryk.covid.helper.TimeHelper
import com.aryk.network.models.ningaApi.CountryData
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.fragment_timeline.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

/**
 * A simple [Fragment] subclass.
 */
@SuppressWarnings("ForbiddenComment", "MagicNumber", "LongMethod", "ComplexMethod")
@ExperimentalCoroutinesApi
class TimelineFragment : Fragment() {
    companion object {
        private const val ARG_SELECTED_COUNTRY = "selected_country"

        fun newInstance(
            country: CountryData
        ): TimelineFragment {
            val args: Bundle = Bundle()
            args.putParcelable(ARG_SELECTED_COUNTRY, country)
            val fragment = TimelineFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private val timelineViewModel: TimelineViewModel by viewModel()
    private val deviceHelper: DeviceHelper by inject()
    private val timeHelper: TimeHelper by inject()
    private var selectedCountryISO2: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_timeline, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getParcelable<CountryData>(ARG_SELECTED_COUNTRY)?.let {
            selectedCountryISO2 = it.countryInfo?.iso2 ?: it.country
            timelineViewModel.inputs.onDataAvailable(it)
            timelineViewModel.inputs.onLoadHistoricalData(selectedCountryISO2)
        } ?: kotlin.run {
            // TODO: Data Missing, Handle this case
        }

        timelineViewModel.outputs.countryData.observe(viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.let { countryData ->
                countryData.countryInfo?.flag?.let { url ->
                    flag.load(url)
                }
                countryName.text = countryData.country

                countryData.updated?.let { updatedAtString ->
                    updatedAt.text = getString(
                        R.string.last_updated,
                        timeHelper.unixTimeStampInSecondsToLongDateTime(
                            updatedAtString.toLong(),
                            Locale.getDefault()
                        )
                    )
                } ?: run {
                    updatedAt.text = "-"
                }
            }
        })

        timelineSwipeRefresh.setOnRefreshListener {
            timelineViewModel.inputs.onLoadHistoricalData(selectedCountryISO2)
            timelineSwipeRefresh.isRefreshing = false
        }

        timelineViewModel.outputs.historicalData.observe(viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.let { timelineData ->
                timelineChart.visibility = View.GONE
                infoCard.visibility = View.GONE
                errorView.visibility = View.GONE

                timelineData.let { map ->
                    val casesSet: MutableList<Entry> = mutableListOf()
                    val deathsSet: MutableList<Entry> = mutableListOf()
                    val recoveredSet: MutableList<Entry> = mutableListOf()

                    map.cases.keys.forEach { casesSet.add(Entry(it, map.cases[it]!!)) }
                    map.deaths.keys.forEach { deathsSet.add(Entry(it, map.deaths[it]!!)) }
                    map.recovered.keys.forEach { recoveredSet.add(Entry(it, map.recovered[it]!!)) }

                    val (casesDataSet, deathsDataSet, recoveredDataSet) = polishChartUi(
                        casesSet,
                        deathsSet,
                        recoveredSet
                    )

                    timelineChart.data = LineData(
                        casesDataSet,
                        deathsDataSet,
                        recoveredDataSet
                    )

                    timelineChart.visibility = View.VISIBLE
                    infoCard.visibility = View.VISIBLE
                }
            }
        })

        timelineViewModel.outputs.isLoading.observe(viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.let { isLoading ->
                timelineProgressBar.visibility = if (isLoading) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
        })

        timelineViewModel.outputs.showErrorView.observe(viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.let { (showError, optionalMessage) ->
                errorView.visibility = if (showError) {
                    optionalMessage?.let {
                        errorView.text = getString(it)
                    } ?: run {
                        errorView.text = getString(R.string.dataMissing)
                    }
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
        })
    }

    private fun polishChartUi(
        casesSet: MutableList<Entry>,
        deathsSet: MutableList<Entry>,
        recoveredSet: MutableList<Entry>
    ): Triple<LineDataSet, LineDataSet, LineDataSet> {
        // Hide description on the bottom right
        timelineChart.description.text = ""

        timelineChart.axisRight.mAxisMinimum = 0f

        // To hide grid
        timelineChart.xAxis.setDrawGridLines(false)

        // TO hide right y axis
        timelineChart.axisRight.isEnabled = false

        val xAxis: XAxis = timelineChart.xAxis
        xAxis.position = XAxisPosition.BOTTOM_INSIDE

        val dpInPx = deviceHelper.convertDpToPixel(1f)
        val threeDpInPx = deviceHelper.convertDpToPixel(3f)

        val casesDataSet = LineDataSet(casesSet, "cases")
        casesDataSet.setColors(
            intArrayOf(android.R.color.holo_orange_dark),
            requireContext()
        )
        casesDataSet.setCircleColors(
            intArrayOf(android.R.color.holo_orange_dark),
            requireContext()
        )
        casesDataSet.lineWidth = dpInPx.toFloat()
        casesDataSet.circleRadius = dpInPx.toFloat()
        casesDataSet.valueTextSize = threeDpInPx.toFloat()

        val deathsDataSet = LineDataSet(deathsSet, "deaths")
        deathsDataSet.setColors(
            intArrayOf(android.R.color.holo_red_dark),
            requireContext()
        )
        deathsDataSet.setCircleColors(
            intArrayOf(android.R.color.holo_red_dark),
            requireContext()
        )
        deathsDataSet.lineWidth = dpInPx.toFloat()
        deathsDataSet.circleRadius = dpInPx.toFloat()
        deathsDataSet.valueTextSize = threeDpInPx.toFloat()

        val recoveredDataSet = LineDataSet(recoveredSet, "recovered")
        recoveredDataSet.setColors(
            intArrayOf(android.R.color.holo_green_dark),
            requireContext()
        )
        recoveredDataSet.setCircleColors(
            intArrayOf(android.R.color.holo_green_dark),
            requireContext()
        )
        recoveredDataSet.lineWidth = dpInPx.toFloat()
        recoveredDataSet.circleRadius = dpInPx.toFloat()
        recoveredDataSet.valueTextSize = threeDpInPx.toFloat()

        return Triple(casesDataSet, deathsDataSet, recoveredDataSet)
    }
}
