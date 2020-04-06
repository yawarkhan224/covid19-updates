package com.aryk.covid.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aryk.covid.helper.Event
import com.aryk.covid.models.FormattedHistoricalData
import com.aryk.covid.repositories.DataRepository
import com.aryk.network.models.ningaApi.CountryData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch

interface DetailViewModelInputs {
    fun onLoadData()
    fun onDataAvailable(data: CountryData, historicalData: FormattedHistoricalData?)
    fun onShowHistoricalDataClicked()
}

@ExperimentalCoroutinesApi
interface DetailViewModelOutputs {
    val countryData: MutableLiveData<Event<CountryData>>
    val historicalCountryData: MutableLiveData<Event<Pair<FormattedHistoricalData?, Boolean>>>
    val showHistoricalData: MutableLiveData<Event<Unit>>
    val isLoading: MutableLiveData<Event<Boolean>>
}

@ExperimentalCoroutinesApi
interface DetailViewModelInterface {
    val inputs: DetailViewModelInputs
    val outputs: DetailViewModelOutputs
}

@SuppressWarnings("ForbiddenComment")
@ExperimentalCoroutinesApi
class DetailViewModel(
    private val dataRepository: DataRepository
) : ViewModel(), DetailViewModelInterface, DetailViewModelInputs, DetailViewModelOutputs {
    override val countryData: MutableLiveData<Event<CountryData>> = MutableLiveData()
    override val historicalCountryData:
            MutableLiveData<Event<Pair<FormattedHistoricalData?, Boolean>>> = MutableLiveData()
    override val showHistoricalData: MutableLiveData<Event<Unit>> = MutableLiveData()
    override val isLoading: MutableLiveData<Event<Boolean>> = MutableLiveData()

    private val onLoadDataProperty: Channel<Unit> = Channel(1)
    override fun onLoadData() {
        viewModelScope.launch {
            isLoading.value = Event(true)
            onLoadDataProperty.send(Unit)
        }
    }

    private val onDataAvailableProperty: Channel<CountryData> = Channel(1)
    private val onHistoricalDataAvailableProperty: Channel<FormattedHistoricalData?> = Channel(1)
    override fun onDataAvailable(data: CountryData, historicalData: FormattedHistoricalData?) {
        viewModelScope.launch {
            onHistoricalDataAvailableProperty.send(historicalData)
            onDataAvailableProperty.send(data)
        }
    }

    private val onShowHistoricalDataClickedProperty: Channel<Unit> = Channel(1)
    override fun onShowHistoricalDataClicked() {
        viewModelScope.launch {
            onShowHistoricalDataClickedProperty.send(Unit)
        }
    }

    override fun onCleared() {
        super.onCleared()

        onDataAvailableProperty.cancel()
        onHistoricalDataAvailableProperty.cancel()
    }

    init {
        viewModelScope.launch {
            onDataAvailableProperty.consumeEach { data ->
                countryData.value = Event(data)
            }
        }

        viewModelScope.launch {
            onHistoricalDataAvailableProperty.consumeEach { data ->
                historicalCountryData.value = Event(Pair(data, false))
            }
        }

        viewModelScope.launch {
            onLoadDataProperty.consumeEach {
                countryData.value!!.peekContent().country?.let { countryName ->
                    val data: CountryData? =
                        dataRepository.getCountryData(countryName)

                    data?.let { nonNullData ->
                        isLoading.value = Event(false)
                        countryData.value = Event(nonNullData)
                    } ?: kotlin.run {
                        isLoading.value = Event(false)
                        // TODO: Handle error for data loading failure
                    }
                } ?: run {
                    // TODO: Handle error for data loading failure
                }
            }
        }

        viewModelScope.launch {
            onShowHistoricalDataClickedProperty.consumeEach {
                showHistoricalData.value = Event(Unit)
                historicalCountryData.value =
                    Event(Pair(historicalCountryData.value?.peekContent()?.first, true))
            }
        }
    }

    override val inputs = this
    override val outputs = this
}
