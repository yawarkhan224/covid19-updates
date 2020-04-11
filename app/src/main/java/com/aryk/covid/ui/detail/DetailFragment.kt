package com.aryk.covid.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import coil.api.load
import com.aryk.covid.R
import com.aryk.covid.extensions.toLocalFormattedString
import com.aryk.covid.helper.TimeHelper
import com.aryk.network.models.ningaApi.CountryData
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

@SuppressWarnings("ForbiddenComment", "LongMethod", "ComplexMethod", "MagicNumber")
@ExperimentalCoroutinesApi
class DetailFragment : Fragment() {
    companion object {
        private const val ARG_SELECTED_COUNTRY = "selected_country"

        fun newInstance(
            country: CountryData
        ): DetailFragment {
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
            it.countryInfo?.flag?.let { url ->
                flag.load(url)
            } ?: run {
                // TODO: Handle error for data loading failure
            }

            detailViewModel.inputs.onDataAvailable(it)
        } ?: kotlin.run {
            // TODO: Data Missing, Handle this case
        }

        detailSwipeRefresh.setOnRefreshListener {
            todayCasesValue.text = "_"
            todayDeathsValue.text = "_"

            perMillionCasesValue.text = "_"
            perMillionDeathsValue.text = "_"
            perMillionTestsValue.text = "_"

            totalCasesValue.text = "_"
            totalDeathsValue.text = "_"
            totalTestsValue.text = "_"
            totalRecoveredValue.text = "_"
            totalCriticalValue.text = "_"

            detailViewModel.inputs.onLoadData()
            detailSwipeRefresh.isRefreshing = false
        }

        detailViewModel.outputs.countryData.observe(viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.let { countryData ->

                countryName.text = countryData.country

                todayDate.text = timeHelper.getCurrentDate(Locale.getDefault())

                todayCasesValue.text = countryData.todayCases.toLocalFormattedString()
                todayDeathsValue.text = countryData.todayDeaths.toLocalFormattedString()

                perMillionCasesValue.text = countryData.casesPerOneMillion.toLocalFormattedString()
                perMillionDeathsValue.text =
                    countryData.deathsPerOneMillion.toLocalFormattedString()
                perMillionTestsValue.text = countryData.testsPerOneMillion.toLocalFormattedString()

                totalCasesValue.text = countryData.cases.toLocalFormattedString()
                totalDeathsValue.text = countryData.deaths.toLocalFormattedString()
                totalTestsValue.text = countryData.tests.toLocalFormattedString()
                totalRecoveredValue.text = countryData.recovered.toLocalFormattedString()
                totalCriticalValue.text = countryData.critical.toLocalFormattedString()

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
