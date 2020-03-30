package com.aryk.covid.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.aryk.covid.repositories.DataRepository
import kotlinx.coroutines.Dispatchers

class HomeViewModel(
    private val dataRepository: DataRepository
) : ViewModel() {

    val getData =
        liveData(Dispatchers.Main) {
            val x = dataRepository.getAllCountriesData(null)
            emit(x)
        }

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
}
