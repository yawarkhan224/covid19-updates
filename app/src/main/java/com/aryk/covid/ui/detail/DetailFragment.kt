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
import com.aryk.network.models.data.CountryData
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

@SuppressWarnings("ForbiddenComment")
@ExperimentalCoroutinesApi
class DetailFragment : Fragment() {
    companion object {
        private const val ARG_SELECTED_COUNTRY = "selected_country"

        fun newInstance(country: CountryData): DetailFragment {
            val args: Bundle = Bundle()
            args.putParcelable(ARG_SELECTED_COUNTRY, country)
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
            flag.load(it.countryInfo.flag)
            detailViewModel.inputs.onDataAvailable(it)
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

                country.text = countryData.country

                casesValue.text = countryData.cases.toString()
                todayCasesValue.text = countryData.todayCases.toString()

                deathsValue.text = countryData.deaths.toString()
                todayDeathsValue.text = countryData.todayDeaths.toString()

                recoveredCasesValue.text = countryData.recovered.toString()
                activeValue.text = countryData.active.toString()
                criticalValue.text = countryData.critical.toString()

                casesPerMillionValue.text = countryData.casesPerOneMillion.toString()
                deathsPerMillionValue.text = countryData.deathsPerOneMillion.toString()

                updatedAt.text = getString(
                    R.string.last_updated,
                    timeHelper.unixTimeStampInSecondsToLongDateTime(
                        countryData.updated.toLong(),
                        Locale.getDefault()
                    )
                )
            }
        })
    }
}
