package com.aryk.covid.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aryk.covid.helper.Event
import com.aryk.covid.repositories.DataRepository
import com.aryk.network.models.data.CountryData
import com.aryk.network.models.data.CountryHistoricalData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch

interface HomeViewModelInputs {
    fun onLoadData()
    fun onLoadHistoricalData()
    fun onCountrySelected(id: Int)
}

@ExperimentalCoroutinesApi
interface HomeViewModelOutputs {
    val countriesData: MutableLiveData<Event<List<CountryData>>>
    val countriesHistoricalData: MutableLiveData<Event<List<CountryHistoricalData>>>
    val selectedCountry: MutableLiveData<Event<CountryData>>
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
    override var selectedCountry: MutableLiveData<Event<CountryData>> = MutableLiveData()
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

    override fun onCleared() {
        super.onCleared()

        onLoadDataProperty.cancel()
        onCountrySelectedProperty.cancel()
        onLoadHistoricalDataProperty.cancel()
    }

    init {
        viewModelScope.launch {
            onLoadDataProperty.consumeEach {
                val data: List<CountryData>? = dataRepository.getAllCountriesData(null)
                data?.let { nonNullList ->
                    isLoading.value = Event(false)
                    countriesData.value = Event(nonNullList)
                } ?: kotlin.run {
                    isLoading.value = Event(false)
                    // TODO: Handle error for data loading failure
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
                    selectedCountry.value = Event(countries.peekContent()[selectedIndex])
                } ?: run {
                    // TODO: Handle Error for empty country list
                }
            }
        }
    }

    override val inputs = this
    override val outputs = this
}
