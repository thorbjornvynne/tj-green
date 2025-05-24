
package com.example.stockpredictor.adapters

import android.content.Context
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.stockpredictor.R
import com.example.stockpredictor.api.models.StockAccuracy

class StockAccuracyAdapter(
    private val context: Context,
    private val data: List<StockAccuracy>
) : RecyclerView.Adapter<StockAccuracyAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val label: TextView = view.findViewById(R.id.stockLabel)
        val stats: TextView = view.findViewById(R.id.accuracyStats)
        val bar: ProgressBar = view.findViewById(R.id.accuracyBar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.item_stock_accuracy, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.label.text = "${item.name} (${item.ticker})"
        holder.stats.text = "Total: ${item.total_predictions} • High: ${item.high_confidence} • Accuracy: ${item.reliability_score * 100}%"
        holder.bar.progress = (item.reliability_score * 100).toInt()
    }

    override fun getItemCount() = data.size
}
