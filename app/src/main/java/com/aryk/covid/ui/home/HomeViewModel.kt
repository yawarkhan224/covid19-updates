package com.aryk.covid.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aryk.covid.enums.CountriesSortType
import com.aryk.covid.helper.Event
import com.aryk.covid.models.FormattedHistoricalData
import com.aryk.covid.repositories.DataRepository
import com.aryk.network.models.ningaApi.CountryData
import com.aryk.network.models.ningaApi.CountryHistoricalData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

interface HomeViewModelInputs {
    fun onLoadData()
    fun onLoadHistoricalData()
    fun onCountrySelected(id: Int)
    fun onSortData(sortBy: String)
}

@ExperimentalCoroutinesApi
interface HomeViewModelOutputs {
    val countriesData: MutableLiveData<Event<List<CountryData>>>
    val countriesHistoricalData: MutableLiveData<Event<List<CountryHistoricalData>>>
    val selectedCountry: MutableLiveData<Event<Pair<CountryData, FormattedHistoricalData?>>>
    val isLoading: MutableLiveData<Event<Boolean>>
}

@ExperimentalCoroutinesApi
interface HomeViewModelInterface {
    val inputs: HomeViewModelInputs
    val outputs: HomeViewModelOutputs
}

@SuppressWarnings("ForbiddenComment")
@ExperimentalCoroutinesApi
class HomeViewModel(
    private val dataRepository: DataRepository
) : ViewModel(), HomeViewModelInterface, HomeViewModelInputs, HomeViewModelOutputs {

    override var countriesData: MutableLiveData<Event<List<CountryData>>> = MutableLiveData()
    override var countriesHistoricalData: MutableLiveData<Event<List<CountryHistoricalData>>> =
        MutableLiveData()
    override var selectedCountry:
            MutableLiveData<Event<Pair<CountryData, FormattedHistoricalData?>>> = MutableLiveData()
    override val isLoading: MutableLiveData<Event<Boolean>> = MutableLiveData()

    private val onLoadDataProperty: Channel<Unit> = Channel(1)
    override fun onLoadData() {
        viewModelScope.launch {
            isLoading.value = Event(true)
            onLoadDataProperty.send(Unit)
        }
    }

    private val onLoadHistoricalDataProperty: Channel<Unit> = Channel(1)
    override fun onLoadHistoricalData() {
        viewModelScope.launch {
            // isLoading.value = Event(true)
            onLoadHistoricalDataProperty.send(Unit)
        }
    }

    private val onCountrySelectedProperty: Channel<Int> = Channel(1)
    override fun onCountrySelected(id: Int) {
        viewModelScope.launch {
            onCountrySelectedProperty.send(id)
        }
    }

    private val onSortDataProperty: Channel<String> = Channel(1)
    override fun onSortData(sortBy: String) {
        isLoading.value = Event(true)
        viewModelScope.launch {
            onSortDataProperty.send(sortBy)
        }
    }

    override fun onCleared() {
        super.onCleared()

        onLoadDataProperty.cancel()
        onCountrySelectedProperty.cancel()
        onLoadHistoricalDataProperty.cancel()
        onSortDataProperty.cancel()
    }

    init {
        viewModelScope.launch {
            onLoadDataProperty.consumeEach {
                dataRepository.getAllCountriesData(null)
                    .onStart { isLoading.value = Event(true) }
                    .catch { exception -> /* _foo.value = error state */
                        isLoading.value = Event(false)
                    }
                    .collect { nonNullList ->
                        isLoading.value = Event(false)
                        countriesData.value =
                            Event(nonNullList.sortedWith(compareBy { -(it.cases ?: 0) }))
                    }
            }
        }

        viewModelScope.launch {
            onLoadHistoricalDataProperty.consumeEach {
                val data: List<CountryHistoricalData>? = dataRepository.getHistoricalData()
                data?.let { nonNullList ->
                    // isLoading.value = Event(false)
                    countriesHistoricalData.value = Event(nonNullList)
                } ?: kotlin.run {
                    // isLoading.value = Event(false)
                    // TODO: Handle error for data loading failure
                }
            }
        }

        viewModelScope.launch {
            onCountrySelectedProperty.consumeEach { selectedIndex ->
                countriesData.value?.let { countries ->
                    val selectedCountryData = countries.peekContent()[selectedIndex]
//                    val selectedCountryHistoricalData =
//                        countriesHistoricalData.value!!.peekContent().last {
//                            it.country == selectedCountryData.country && it.province == null
//                        }

                    selectedCountry.value =
                        Event(
                            Pair(
                                selectedCountryData,
                                null
                            )
                        )
                } ?: run {
                    // TODO: Handle Error for empty country list
                }
            }
        }

        viewModelScope.launch {
            onSortDataProperty.consumeEach { sortBy ->
                countriesData.value?.peekContent()?.let { countries ->
                    when (sortBy) {
                        CountriesSortType.Country.key -> {
                            countriesData.value =
                                Event(countries.sortedWith(compareBy { it.country }))
                        }
                        CountriesSortType.Cases.key -> {
                            countriesData.value =
                                Event(countries.sortedWith(compareBy { it.cases }))
                        }
                        CountriesSortType.Deaths.key -> {
                            countriesData.value =
                                Event(countries.sortedWith(compareBy { it.deaths }))
                        }
                        CountriesSortType.Recovered.key -> {
                            countriesData.value =
                                Event(countries.sortedWith(compareBy { it.recovered }))
                        }
                    }

                    isLoading.value = Event(false)
                }
            }
        }
    }

    override val inputs = this
    override val outputs = this
}
