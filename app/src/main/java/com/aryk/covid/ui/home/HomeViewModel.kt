package com.aryk.covid.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aryk.covid.helper.Event
import com.aryk.covid.models.FormattedHistoricalData
import com.aryk.covid.repositories.DataRepository
import com.aryk.network.models.data.CountryData
import com.aryk.network.models.data.CountryHistoricalData
import com.aryk.network.models.data.TimelineData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

interface HomeViewModelInputs {
    fun onLoadData()
    fun onLoadHistoricalData()
    fun onCountrySelected(id: Int)
}

@ExperimentalCoroutinesApi
interface HomeViewModelOutputs {
    val countriesData: MutableLiveData<Event<List<CountryData>>>
    val countriesHistoricalData: MutableLiveData<Event<List<CountryHistoricalData>>>
    val selectedCountry: MutableLiveData<Event<Pair<CountryData, FormattedHistoricalData?>>>
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
    override var selectedCountry:
            MutableLiveData<Event<Pair<CountryData, FormattedHistoricalData?>>> = MutableLiveData()
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
                    val selectedCountryData = countries.peekContent()[selectedIndex]
                    val selectedCountryHistoricalData =
                        countriesHistoricalData.value!!.peekContent().last {
                            it.country == selectedCountryData.country && it.province == null
                        }


                    selectedCountry.value =
                        Event(Pair(selectedCountryData, selectedCountryHistoricalData.timeline.toFormattedData()))
                } ?: run {
                    // TODO: Handle Error for empty country list
                }
            }
        }
    }

    override val inputs = this
    override val outputs = this
}

private fun TimelineData.toFormattedData(): FormattedHistoricalData {
    val casesKeys = this.cases.keys
    val deathsKeys = this.deaths.keys
    val recoveredKeys = this.recovered.keys

    var toReturn: FormattedHistoricalData? = null
    val casesMap: MutableMap<Int,Int> = mutableMapOf()
    val deathsMap: MutableMap<Int,Int> = mutableMapOf()
    val recoveredMap: MutableMap<Int,Int> = mutableMapOf()

    if (casesKeys.isNotEmpty()) {
        val (m,d,y) = casesKeys.first().split("/")
        val calendar = Calendar.getInstance()
        calendar.set((y+y).toInt(),m.toInt()-1,d.toInt(),0,0)
        val week1 = calendar.get(Calendar.WEEK_OF_YEAR)
        val dow = calendar.get(Calendar.DAY_OF_WEEK)

        var weekCount = week1

        val casesMapToList = this.cases.toList()

        var x = dow
        while (x <= recoveredKeys.size) {
            println(x)
            if (x == dow) {
                casesMap[week1] = casesMapToList[x].second
                x += 7 - dow
            } else {
                casesMap[weekCount] = casesMapToList[x].second
                x += 7
            }
            weekCount += 1
        }

    }

    if (deathsKeys.isNotEmpty()) {
        val (m,d,y) = casesKeys.first().split("/")
        val calendar = Calendar.getInstance()
        calendar.set((y+y).toInt(),m.toInt()-1,d.toInt(),0,0)
        val week1 = calendar.get(Calendar.WEEK_OF_YEAR)
        val dow = calendar.get(Calendar.DAY_OF_WEEK)

        var weekCount = week1

        val deathsMapToList = this.deaths.toList()

        var x = dow
        while (x <= recoveredKeys.size) {
            println(x)
            if (x == dow) {
                deathsMap[week1] = deathsMapToList[x].second
                x += 7 - dow
            } else {
                deathsMap[weekCount] = deathsMapToList[x].second
                x += 7
            }
            weekCount += 1
        }

    }

    if (recoveredKeys.isNotEmpty()) {
        val (m,d,y) = casesKeys.first().split("/")
        val calendar = Calendar.getInstance()
        calendar.set((y+y).toInt(),m.toInt()-1,d.toInt(),0,0)
        val week1 = calendar.get(Calendar.WEEK_OF_YEAR)
        val dow = calendar.get(Calendar.DAY_OF_WEEK)

        var weekCount = week1

        val recoveredMapToList = this.recovered.toList()

        var x = dow
        while (x <= recoveredKeys.size) {
            println(x)
            if (x == dow) {
                recoveredMap[week1] = recoveredMapToList[x].second
                x += 7 - dow
            } else {
                recoveredMap[weekCount] = recoveredMapToList[x].second
                x += 7
            }
            weekCount += 1
        }
    }

    toReturn = FormattedHistoricalData(
        casesMap,
        deathsMap,
        recoveredMap
    )

   return  toReturn
}
