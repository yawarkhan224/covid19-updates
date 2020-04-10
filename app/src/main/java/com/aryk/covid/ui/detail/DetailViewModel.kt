package com.aryk.covid.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aryk.covid.helper.Event
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

interface DetailViewModelInputs {
    fun onLoadData()
    fun onDataAvailable(data: CountryData)
    fun onLoadDataFromDb()
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
    private val dataRepository: DataRepository,
    private val localDatabase: LocalDatabase
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

    private val onLoadDataFromDbProperty: Channel<Unit> = Channel(1)
    override fun onLoadDataFromDb() {
        isLoading.value = Event(true)
        viewModelScope.launch {
            onLoadDataFromDbProperty.send(Unit)
        }
    }

    // Local Variable
    var countryName: String? = null

    override fun onCleared() {
        super.onCleared()

        onDataAvailableProperty.cancel()
    }

    init {
        viewModelScope.launch {
            onDataAvailableProperty.consumeEach { data ->
                countryName = data.country
                countryData.value = Event(data)
            }
        }

        viewModelScope.launch {
            onLoadDataProperty.consumeEach {
                countryData.value?.peekContent()?.country?.let { countryName ->
                    dataRepository.getCountryData(countryName)
                        .onStart { isLoading.value = Event(true) }
                        .catch {
                            onLoadDataFromDb()
                        }
                        .collect { nonNullData ->
                            isLoading.value = Event(false)
                            countryData.value = Event(nonNullData)
                        }
                } ?: run {
                    // TODO: Handle error for data loading failure
                }
            }
        }

        viewModelScope.launch {
            onLoadDataFromDbProperty.consumeEach {
                countryName?.let {
                    localDatabase.countryDataDao().getCountryByName(it)?.let {
                        countryData.value = Event(it)
                    }
                }
                isLoading.value = Event(false)
            }
        }
    }

    override val inputs = this
    override val outputs = this
}
