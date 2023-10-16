package ru.practicum.android.diploma.search.presentation.rv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.search.domain.models.Vacancy

class VacancyAdapter(private val items: List<Vacancy>) :
    RecyclerView.Adapter<VacancyAdapter.ViewHolder>() {

    var itemClickListener: ((Int, Vacancy) -> Unit)? = null

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.image)
        val title: TextView = itemView.findViewById(R.id.title)
        val company: TextView = itemView.findViewById(R.id.company)
        val value: TextView = itemView.findViewById(R.id.value)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
        holder.itemView.setOnClickListener { itemClickListener?.invoke(position, item) }
    }

    private fun ViewHolder.bind(item: Vacancy) {
        Glide.with(itemView)
            .load(item.logoUrl)
            .placeholder(R.drawable.vacancy_placeholder)
            .transform(RoundedCorners(12))
            .into(image)
        title.text = item.title
        company.text = item.company
        value.text = item.salary
    }
}