package com.aryk.covid.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import coil.api.load
import com.aryk.covid.R
import com.aryk.covid.helper.TimeHelper
import com.aryk.covid.models.FormattedHistoricalData
import com.aryk.network.models.ningaApi.CountryData
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

@SuppressWarnings("ForbiddenComment", "LongMethod", "ComplexMethod", "MagicNumber")
@ExperimentalCoroutinesApi
class DetailFragment : Fragment() {
    companion object {
        private const val ARG_SELECTED_COUNTRY = "selected_country"
        private const val ARG_HISTORICAL_DATA = "historical_data"

        fun newInstance(
            country: CountryData,
            historicalData: FormattedHistoricalData?
        ): DetailFragment {
            val args: Bundle = Bundle()
            args.putParcelable(ARG_SELECTED_COUNTRY, country)
            args.putParcelable(ARG_HISTORICAL_DATA, historicalData)
            val fragment = DetailFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private val detailViewModel: DetailViewModel by viewModel()
    private val timeHelper: TimeHelper by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getParcelable<CountryData>(ARG_SELECTED_COUNTRY)?.let {
            it.countryInfo?.flag?.let { url ->
                flag.load(url)
            } ?: run {
                // TODO: Handle error for data loading failure
            }

            detailViewModel.inputs.onDataAvailable(
                it,
                arguments?.getParcelable<FormattedHistoricalData>(ARG_HISTORICAL_DATA)
            )
        } ?: kotlin.run {
            // TODO: Data Missing, Handle this case
        }

        detailSwipeRefresh.setOnRefreshListener {
            casesValue.text = "_"
            todayCasesValue.text = "_"

            deathsValue.text = "_"
            todayDeathsValue.text = "_"

            recoveredCasesValue.text = "_"
            activeValue.text = "_"
            criticalValue.text = "_"

            casesPerMillionValue.text = "_"
            deathsPerMillionValue.text = "_"

            detailViewModel.inputs.onLoadData()
            detailSwipeRefresh.isRefreshing = false
        }

        detailViewModel.outputs.countryData.observe(viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.let { countryData ->

                casesValue.text = countryData.cases.toString()
                todayCasesValue.text = countryData.todayCases.toString()

                deathsValue.text = countryData.deaths.toString()
                todayDeathsValue.text = countryData.todayDeaths.toString()

                recoveredCasesValue.text = countryData.recovered.toString()
                activeValue.text = countryData.active.toString()
                criticalValue.text = countryData.critical.toString()

                casesPerMillionValue.text = countryData.casesPerOneMillion.toString()
                deathsPerMillionValue.text = countryData.deathsPerOneMillion.toString()

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
    }
}
