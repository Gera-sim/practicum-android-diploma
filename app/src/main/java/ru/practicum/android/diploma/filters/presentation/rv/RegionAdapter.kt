package ru.practicum.android.diploma.filters.presentation.rv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemRegionBinding
import ru.practicum.android.diploma.search.data.dto.response_models.Area

class RegionAdapter(private val clickListener: RegionClickListener) :
    RecyclerView.Adapter<RegionViewHolder>() {
    var regionList = ArrayList<Area>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = RegionViewHolder(
        ItemRegionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )


    override fun getItemCount() = regionList.size
    override fun onBindViewHolder(holder: RegionViewHolder, position: Int) {
        holder.bind(regionList[position])
        holder.itemView.setOnClickListener {
            clickListener.onTrackClick(regionList[position])
            holder.radioButton.isChecked = !holder.radioButton.isChecked
        }

    }

}


fun interface RegionClickListener {
    fun onTrackClick(country: Area)
}
