package com.aryk.covid.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aryk.covid.extensions.toFormattedTimelineData
import com.aryk.covid.helper.Event
import com.aryk.covid.models.FormattedTimelineData
import com.aryk.covid.persistance.LocalDatabase
import com.aryk.covid.repositories.DataRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

interface TimelineViewModelInputs {
    fun onLoadHistoricalData(iso2: String?)
}

@ExperimentalCoroutinesApi
interface TimelineViewModelOutputs {
    val historicalData: MutableLiveData<Event<FormattedTimelineData>>
    val isLoading: MutableLiveData<Event<Boolean>>
    val showErrorView: MutableLiveData<Event<Boolean>>
}

@ExperimentalCoroutinesApi
interface TimelineViewModelInterface {
    val inputs: TimelineViewModelInputs
    val outputs: TimelineViewModelOutputs
}

@SuppressWarnings("ForbiddenComment")
@ExperimentalCoroutinesApi
class TimelineViewModel(
    private val dataRepository: DataRepository,
    private val localDatabase: LocalDatabase
) : ViewModel(), TimelineViewModelInterface, TimelineViewModelInputs, TimelineViewModelOutputs {
    override val historicalData: MutableLiveData<Event<FormattedTimelineData>> = MutableLiveData()
    override val isLoading: MutableLiveData<Event<Boolean>> = MutableLiveData()
    override val showErrorView: MutableLiveData<Event<Boolean>> = MutableLiveData()

    private val onLoadHistoricalDataProperty: Channel<String?> = Channel(1)
    override fun onLoadHistoricalData(iso2: String?) {
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
                countryISO2?.let {
                    dataRepository.getHistoricalData(it)
                        .onStart { isLoading.value = Event(true) }
                        .catch { exception -> /* _foo.value = error state */
                            isLoading.value = Event(false)
                            showErrorView.value = Event(true)
                        }
                        .collect { nonNullData ->
                            isLoading.value = Event(false)
                            historicalData.value = Event(nonNullData.toFormattedTimelineData())
                        }
                } ?: run {
                    // TODO: Handle this case
                }
            }
        }
    }

    override val inputs = this
    override val outputs = this
}
