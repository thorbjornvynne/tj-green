
package com.example.stockpredictor.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.*
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.stockpredictor.R
import com.example.stockpredictor.StockDetailActivity
import com.example.stockpredictor.api.models.StockInfo

class StockAdapter(
    private val context: Context,
    private val stocks: List<StockInfo>
) : RecyclerView.Adapter<StockAdapter.StockViewHolder>() {

    class StockViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameText: TextView = view.findViewById(R.id.stockName)
        val statusText: TextView = view.findViewById(R.id.stockStatus)
        val reliabilityText: TextView = view.findViewById(R.id.stockReliability)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_stock_card, parent, false)
        return StockViewHolder(view)
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        val stock = stocks[position]
        holder.nameText.text = "${stock.name} (${stock.ticker})"
        holder.statusText.text = "Status: ${if (stock.enabled) "Enabled" else "Disabled"}"
        holder.reliabilityText.text = "Reliability: ${stock.reliability ?: "N/A"}"
        holder.reliabilityText.setTextColor(
            when (stock.reliability ?: 0) {
                in 80..100 -> Color.parseColor("#2E7D32")
                in 50..79 -> Color.parseColor("#F9A825")
                else -> Color.parseColor("#C62828")
            }
        )
        holder.itemView.setOnClickListener {
            val intent = Intent(context, StockDetailActivity::class.java)
            intent.putExtra("ticker", stock.ticker)
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = stocks.size
}
