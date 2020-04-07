package com.aryk.covid.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aryk.covid.extensions.toFormattedTimelineData
import com.aryk.covid.helper.Event
import com.aryk.covid.models.FormattedTimelineData
import com.aryk.covid.repositories.DataRepository
import com.aryk.network.models.virusTrackerApi.CountryTimelineResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch

interface TimelineViewModelInputs {
    fun onLoadHistoricalData(iso2: String)
}

@ExperimentalCoroutinesApi
interface TimelineViewModelOutputs {

    val historicalData: MutableLiveData<Event<FormattedTimelineData>>
    val isLoading: MutableLiveData<Event<Boolean>>
}

@ExperimentalCoroutinesApi
interface TimelineViewModelInterface {
    val inputs: TimelineViewModelInputs
    val outputs: TimelineViewModelOutputs
}

@SuppressWarnings("ForbiddenComment")
@ExperimentalCoroutinesApi
class TimelineViewModel(
    private val dataRepository: DataRepository
) : ViewModel(), TimelineViewModelInterface, TimelineViewModelInputs, TimelineViewModelOutputs {
    override val historicalData: MutableLiveData<Event<FormattedTimelineData>> = MutableLiveData()
    override val isLoading: MutableLiveData<Event<Boolean>> = MutableLiveData()

    private val onLoadHistoricalDataProperty: Channel<String> = Channel(1)
    override fun onLoadHistoricalData(iso2: String) {
        viewModelScope.launch {
            onLoadHistoricalDataProperty.send(iso2)
        }
    }

    override fun onCleared() {
        super.onCleared()

        onLoadHistoricalDataProperty.cancel()
    }

    init {
        viewModelScope.launch {
            onLoadHistoricalDataProperty.consumeEach { countryISO2 ->
                val data: CountryTimelineResponse? =
                    dataRepository.getHistoricalData2(countryISO2)

                data?.let { nonNullData ->
                    historicalData.value = Event(nonNullData.toFormattedTimelineData())
                } ?: kotlin.run {
                    // TODO: Handle error for data loading failure
                }
            }
        }
    }

    override val inputs = this
    override val outputs = this
}