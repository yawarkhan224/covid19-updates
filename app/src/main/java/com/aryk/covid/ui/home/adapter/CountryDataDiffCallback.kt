package com.aryk.covid.ui.home.adapter

import androidx.recyclerview.widget.DiffUtil
import com.aryk.network.models.ningaApi.CountryData

class CountryDataDiffCallback : DiffUtil.ItemCallback<CountryData>() {
    override fun areItemsTheSame(oldItem: CountryData, newItem: CountryData): Boolean {
        return oldItem.country == newItem.country
    }

    override fun areContentsTheSame(oldItem: CountryData, newItem: CountryData): Boolean {
        return oldItem == newItem
    }
}
