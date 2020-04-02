package com.aryk.covid.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
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

interface DetailViewModelInputs {
    fun onLoadData()
    fun onCountryItemClicked(id: Int)
}

@ExperimentalCoroutinesApi
interface DetailViewModelOutputs {
    val countriesData: MutableLiveData<List<CountryData>>
    val selectedCountry: Channel<Int>
    val isLoading: ConflatedBroadcastChannel<Boolean>
}

@ExperimentalCoroutinesApi
interface DetailViewModelInterface {
    val inputs: DetailViewModelInputs
    val outputs: DetailViewModelOutputs
}

@ExperimentalCoroutinesApi
class DetailViewModel(
    private val dataRepository: DataRepository
) : ViewModel(), DetailViewModelInterface, DetailViewModelInputs, DetailViewModelOutputs {

    override var countriesData: MutableLiveData<List<CountryData>> = MutableLiveData()
    override val selectedCountry: Channel<Int> = Channel()
    override val isLoading: ConflatedBroadcastChannel<Boolean> = ConflatedBroadcastChannel()

    private val onLoadDataProperty: Channel<Int> = Channel(1)
    override fun onLoadData() {
        viewModelScope.launch {
            isLoading.send(true)
            onLoadDataProperty.send(Random.nextInt())
        }
    }

    override fun onCountryItemClicked(id: Int) {
        Log.d("", "")
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    override fun onCleared() {
        super.onCleared()

        onLoadDataProperty.cancel()
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
                }
            }
        }
    }

    override val inputs = this
    override val outputs = this
}
