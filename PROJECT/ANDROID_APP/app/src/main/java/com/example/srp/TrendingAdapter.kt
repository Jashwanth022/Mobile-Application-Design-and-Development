package com.example.srp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class TrendingAdapter(private val stocks: List<TrendingStock>) :
    RecyclerView.Adapter<TrendingAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvTrendingName)
        val tvChange: TextView = view.findViewById(R.id.tvTrendingChange)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_trending_stock, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val stock = stocks[position]
        holder.tvName.text = stock.name
        holder.tvChange.text = stock.change
        
        val color = if (stock.isPositive) R.color.positive_green else R.color.negative_red
        holder.tvChange.setTextColor(ContextCompat.getColor(holder.itemView.context, color))
    }

    override fun getItemCount() = stocks.size
}