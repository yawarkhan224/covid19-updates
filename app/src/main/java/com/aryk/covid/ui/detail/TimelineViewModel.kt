package com.aryk.covid.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aryk.covid.R
import com.aryk.covid.extensions.toFormattedTimelineData
import com.aryk.covid.helper.Event
import com.aryk.covid.models.FormattedTimelineData
import com.aryk.covid.persistance.LocalDatabase
import com.aryk.covid.repositories.DataRepository
import com.aryk.network.models.ningaApi.CountryData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import retrofit2.HttpException

interface TimelineViewModelInputs {
    fun onLoadHistoricalData(iso2: String?)
    fun onDataAvailable(data: CountryData)
}

@ExperimentalCoroutinesApi
interface TimelineViewModelOutputs {
    val historicalData: MutableLiveData<Event<FormattedTimelineData>>
    val timelineData: MutableLiveData<Event<FormattedTimelineData>>
    val countryData: MutableLiveData<Event<CountryData>>
    val isLoading: MutableLiveData<Event<Boolean>>
    val showErrorView: MutableLiveData<Event<Pair<Boolean, Int?>>>
}

@ExperimentalCoroutinesApi
interface TimelineViewModelInterface {
    val inputs: TimelineViewModelInputs
    val outputs: TimelineViewModelOutputs
}

@SuppressWarnings("ForbiddenComment", "MagicNumber")
@ExperimentalCoroutinesApi
class TimelineViewModel(
    private val dataRepository: DataRepository,
    private val localDatabase: LocalDatabase
) : ViewModel(), TimelineViewModelInterface, TimelineViewModelInputs, TimelineViewModelOutputs {
    override val historicalData: MutableLiveData<Event<FormattedTimelineData>> = MutableLiveData()
    override val timelineData: MutableLiveData<Event<FormattedTimelineData>> = MutableLiveData()
    override val countryData: MutableLiveData<Event<CountryData>> = MutableLiveData()
    override val isLoading: MutableLiveData<Event<Boolean>> = MutableLiveData()
    override val showErrorView: MutableLiveData<Event<Pair<Boolean, Int?>>> = MutableLiveData()

    private val onLoadHistoricalDataProperty: Channel<String?> = Channel(1)
    override fun onLoadHistoricalData(iso2: String?) {
        viewModelScope.launch {
            onLoadHistoricalDataProperty.send(iso2)
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

        onLoadHistoricalDataProperty.cancel()
        onDataAvailableProperty.cancel()
    }

    init {
        viewModelScope.launch {
            onLoadHistoricalDataProperty.consumeEach { countryISO2 ->
                countryISO2?.let {
                    // This commented out code is using TheVirusTracker API
//                    dataRepository.getHistoricalData(it)
//                        .onStart { isLoading.value = Event(true) }
//                        .catch { exception -> /* _foo.value = error state */
//                            isLoading.value = Event(false)
//                            showErrorView.value = Event(true)
//                        }
//                        .collect { nonNullData ->
//                            isLoading.value = Event(false)
//                            historicalData.value = Event(nonNullData.toFormattedTimelineData())
//                        }

                    dataRepository.getCountryTimelineData(it)
                        .onStart { isLoading.value = Event(true) }
                        .catch { exception ->
                            isLoading.value = Event(false)
                            when (exception) {
                                is HttpException -> {
                                    if (exception.code() == 404) {
                                        showErrorView.value =
                                            Event(Pair(true, R.string.historicalDataMissing))
                                    } else {
                                        showErrorView.value = Event(Pair(true, null))
                                    }
                                }
                                else -> {
                                    showErrorView.value = Event(Pair(true, null))
                                }
                            }
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

        viewModelScope.launch {
            onDataAvailableProperty.consumeEach { data ->
                countryData.value = Event(data)
            }
        }
    }

    override val inputs = this
    override val outputs = this
}
