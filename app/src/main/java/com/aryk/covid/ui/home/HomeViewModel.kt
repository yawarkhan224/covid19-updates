package com.aryk.covid.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aryk.covid.enums.CountriesSortType
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

interface HomeViewModelInputs {
    fun onLoadData(sortBy: String?)
    fun onCountrySelected(id: Int)
    fun onSortData(sortBy: String)
    fun onLoadDataFromDb()
}

@ExperimentalCoroutinesApi
interface HomeViewModelOutputs {
    val countriesData: MutableLiveData<Event<List<CountryData>>>
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
    private val dataRepository: DataRepository,
    private val localDatabase: LocalDatabase
) : ViewModel(), HomeViewModelInterface, HomeViewModelInputs, HomeViewModelOutputs {
    override var countriesData: MutableLiveData<Event<List<CountryData>>> = MutableLiveData()
    override var selectedCountry:
            MutableLiveData<Event<CountryData>> = MutableLiveData()
    override val isLoading: MutableLiveData<Event<Boolean>> = MutableLiveData()

    private val onLoadDataProperty: Channel<String?> = Channel(1)
    override fun onLoadData(sortBy: String?) {
        viewModelScope.launch {
            isLoading.value = Event(true)
            onLoadDataProperty.send(sortBy)
        }
    }

    private val onCountrySelectedProperty: Channel<Int> = Channel(1)
    override fun onCountrySelected(id: Int) {
        viewModelScope.launch {
            onCountrySelectedProperty.send(id)
        }
    }

    private val onSortDataProperty: MutableLiveData<Event<String>> = MutableLiveData()
    override fun onSortData(sortBy: String) {
        viewModelScope.launch {
            val selectedValue = onSortDataProperty.value?.peekContent() ?: ""
            if (sortBy != selectedValue) {
                if (!(selectedValue == "" && sortBy != CountriesSortType.Cases.key)) {
                    isLoading.value = Event(true)
                    onSortDataProperty.value = Event(sortBy)
                }
            }
        }
    }

    private val onLoadDataFromDbProperty: Channel<Unit> = Channel(1)
    override fun onLoadDataFromDb() {
        isLoading.value = Event(true)
        viewModelScope.launch {
            onLoadDataFromDbProperty.send(Unit)
        }
    }

    override fun onCleared() {
        super.onCleared()

        onLoadDataProperty.cancel()
        onCountrySelectedProperty.cancel()
        onLoadDataFromDbProperty.cancel()
    }

    init {
        viewModelScope.launch {
            onLoadDataProperty.consumeEach {
                var sortBy = it

//                if (onSortDataProperty.value != null) {
//                    onSortDataProperty.value?.peekContent()
//                } else {
//                    null
//                }

                dataRepository.getAllCountriesData(sortBy)
                    .onStart { isLoading.value = Event(true) }
                    .catch { exception -> /* _foo.value = error state */
                        onLoadDataFromDb()
                    }
                    .collect { nonNullList ->
                        isLoading.value = Event(false)
                        countriesData.value =
                            Event(nonNullList)
                    }
            }
        }

        viewModelScope.launch {
            onCountrySelectedProperty.consumeEach { selectedIndex ->
                countriesData.value?.let { countries ->
                    val selectedCountryData = countries.peekContent()[selectedIndex]

                    selectedCountry.value =
                        Event(selectedCountryData)
                } ?: run {
                    // TODO: Handle Error for empty country list
                }
            }
        }

        viewModelScope.launch {
            onSortDataProperty.observeForever { sortBy ->
                countriesData.value?.peekContent()?.let { countries ->
                    when (sortBy.peekContent()) {
                        CountriesSortType.Country.key -> {
                            countriesData.value =
                                Event(countries.sortedWith(compareBy { it.country }))
                        }
                        CountriesSortType.Cases.key -> {
                            countriesData.value =
                                Event(countries.sortedWith(compareBy { -(it.cases ?: 0) }))
                        }
                        CountriesSortType.Deaths.key -> {
                            countriesData.value =
                                Event(countries.sortedWith(compareBy { -(it.deaths ?: 0) }))
                        }
                        CountriesSortType.Recovered.key -> {
                            countriesData.value =
                                Event(countries.sortedWith(compareBy { -(it.recovered ?: 0) }))
                        }
                    }

                    isLoading.value = Event(false)
                }
            }
        }

        viewModelScope.launch {
            onLoadDataFromDbProperty.consumeEach {
                val sortBy = onSortDataProperty.value?.peekContent() ?: CountriesSortType.Cases.key
                countriesData.value = Event(localDatabase.countryDataDao().getCountries(sortBy))
                isLoading.value = Event(false)
            }
        }
    }

    override val inputs = this
    override val outputs = this
}
