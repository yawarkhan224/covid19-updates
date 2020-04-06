package com.aryk.covid.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.aryk.covid.R
import com.aryk.covid.extensions.toShorterFormatString
import com.aryk.network.models.ningaApi.CountryData
import kotlinx.android.synthetic.main.countries_list_item.view.*

class CountryListAdapter :
    ListAdapter<CountryData, CountryListAdapter.CountryDataViewHolder>(
        CountryDataDiffCallback()
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryDataViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.countries_list_item, parent, false)

        return CountryDataViewHolder(view)
    }

    override fun onBindViewHolder(holder: CountryDataViewHolder, position: Int) {
        val country = getItem(position)

        country.countryInfo?.let { countryInfo ->
            holder.countryFlag.load(countryInfo.flag)
            holder.countryName.text = countryInfo.iso3 ?: "-"
            holder.totalCasesValue.text = country.cases?.toShorterFormatString() ?: "-"
            holder.totalDeathsValue.text = country.deaths?.toShorterFormatString() ?: "-"
            holder.recoveredValue.text = country.recovered?.toShorterFormatString() ?: "-"
        }
    }

    class CountryDataViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal val countryName = view.countryName
        internal val countryFlag = view.countryFlag
        internal val totalCasesValue = view.totalCasesValue
        internal val totalDeathsValue = view.totalDeathsValue
        internal val recoveredValue = view.recoveredValue
    }
}
