package ru.practicum.android.diploma.filters.presentation.rv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemCountryBinding
import ru.practicum.android.diploma.filters.domain.models.Industry

class IndustryAdapter(private val clickListener: IndustryClickListener) :
    RecyclerView.Adapter<IndustryViewHolder>() {
    var industryList = ArrayList<Industry>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = IndustryViewHolder(
        ItemCountryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount() = industryList.size
    override fun onBindViewHolder(holder: IndustryViewHolder, position: Int) {
        holder.bind(industryList[position])
        holder.itemView.setOnClickListener { clickListener.onIndustryClick(industryList[position]) }
    }
}

fun interface IndustryClickListener {
    fun onIndustryClick(industry: Industry)
}