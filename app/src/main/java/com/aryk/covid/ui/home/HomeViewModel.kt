package com.aryk.covid.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aryk.covid.repositories.DataRepository
import com.aryk.network.models.data.CountryData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import kotlin.random.Random

interface HomeViewModelInputs {
    fun onLoadData()
    fun onCountrySelected(id: Int)
}

@ExperimentalCoroutinesApi
interface HomeViewModelOutputs {
    val countriesData: MutableLiveData<List<CountryData>>
    val selectedCountry: Channel<CountryData>
    val isLoading: ConflatedBroadcastChannel<Boolean>
}

@ExperimentalCoroutinesApi
interface HomeViewModelInterface {
    val inputs: HomeViewModelInputs
    val outputs: HomeViewModelOutputs
}

@ExperimentalCoroutinesApi
class HomeViewModel(
    private val dataRepository: DataRepository
) : ViewModel(), HomeViewModelInterface, HomeViewModelInputs, HomeViewModelOutputs {

    override var countriesData: MutableLiveData<List<CountryData>> = MutableLiveData()
    override val selectedCountry: Channel<CountryData> = Channel()
    override val isLoading: ConflatedBroadcastChannel<Boolean> = ConflatedBroadcastChannel()

    private val onLoadDataProperty: Channel<Int> = Channel(1)
    override fun onLoadData() {
        viewModelScope.launch {
            isLoading.send(true)
            onLoadDataProperty.send(Random.nextInt())
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
    }

    init {
        viewModelScope.launch {
            onLoadDataProperty.consumeEach {
                val data: List<CountryData>? = dataRepository.getAllCountriesData(null)
                data?.let { nonNullList ->
                    isLoading.send(false)
                    countriesData.value = nonNullList
                } ?: kotlin.run {
                    isLoading.send(false)
                    // TODO: Handle error for data loading failure
                }
            }
        }

        viewModelScope.launch {
            onCountrySelectedProperty.consumeEach { selectedIndex ->
                countriesData.value?.let { countries ->
                    selectedCountry.send(countries[selectedIndex])
                } ?: run {
                    // TODO: Handle Error for empty country list
                }
            }
        }
    }

    override val inputs = this
    override val outputs = this
}
