package com.aryk.covid.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aryk.covid.helper.Event
import com.aryk.covid.repositories.DataRepository
import com.aryk.network.models.data.CountryData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch

interface DetailViewModelInputs {
    fun onLoadData()
    fun onDataAvailable(data: CountryData)
}

@ExperimentalCoroutinesApi
interface DetailViewModelOutputs {
    val countryData: MutableLiveData<Event<CountryData>>
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
    override val isLoading: MutableLiveData<Event<Boolean>> = MutableLiveData()

    private val onLoadDataProperty: Channel<Unit> = Channel(1)
    override fun onLoadData() {
        viewModelScope.launch {
            isLoading.value = Event(true)
            onLoadDataProperty.send(Unit)
        }
    }

    private val onDataAvailableProperty: Channel<CountryData> = Channel(1)
    override fun onDataAvailable(data: CountryData) {
        viewModelScope.launch {
            onDataAvailableProperty.send(data)
        }
    }

    override fun onCleared() {
        super.onCleared()

        onDataAvailableProperty.cancel()
    }

    init {
        viewModelScope.launch {
            onDataAvailableProperty.consumeEach { data ->
                countryData.value = Event(data)
            }
        }

        viewModelScope.launch {
            onLoadDataProperty.consumeEach {
                val data: CountryData? =
                    dataRepository.getCountryData(
                        countryData.value!!.peekContent().country
                    )

                data?.let { nonNullData ->
                    isLoading.value = Event(false)
                    countryData.value = Event(nonNullData)
                } ?: kotlin.run {
                    isLoading.value = Event(false)
                    // TODO: Handle error for data loading failure
                }
            }
        }
    }

    override val inputs = this
    override val outputs = this
}
