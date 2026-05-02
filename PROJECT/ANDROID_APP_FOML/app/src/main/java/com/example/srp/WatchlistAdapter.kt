package com.example.srp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class WatchlistAdapter(
    private var stocks: List<WatchlistEntity>,
    private val onItemClick: (WatchlistEntity) -> Unit
) : RecyclerView.Adapter<WatchlistAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvStockName: TextView = view.findViewById(R.id.tvStockName)
        val tvAccuracy: TextView = view.findViewById(R.id.tvAccuracy)
        val tvChangePercent: TextView = view.findViewById(R.id.tvChangePercent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_stock, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val stock = stocks[position]
        holder.tvStockName.text = stock.symbol
        holder.tvAccuracy.text = "Accuracy: ${stock.accuracy}%"
        
        val prefix = if (stock.changePercent >= 0) "+" else ""
        holder.tvChangePercent.text = "$prefix${stock.changePercent}%"
        
        val color = if (stock.changePercent >= 0) {
            ContextCompat.getColor(holder.itemView.context, R.color.positive_green)
        } else {
            ContextCompat.getColor(holder.itemView.context, R.color.negative_red)
        }
        holder.tvChangePercent.setTextColor(color)

        holder.itemView.setOnClickListener {
            onItemClick(stock)
        }
    }

    override fun getItemCount() = stocks.size

    fun updateData(newStocks: List<WatchlistEntity>) {
        stocks = newStocks
        notifyDataSetChanged()
    }
}