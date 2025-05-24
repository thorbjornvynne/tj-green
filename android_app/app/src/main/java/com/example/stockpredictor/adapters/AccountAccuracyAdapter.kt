
package com.example.stockpredictor.adapters

import android.content.Context
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.stockpredictor.R
import com.example.stockpredictor.api.models.AccountAccuracy

class AccountAccuracyAdapter(
    private val context: Context,
    private val data: List<AccountAccuracy>
) : RecyclerView.Adapter<AccountAccuracyAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val username: TextView = view.findViewById(R.id.accountUsername)
        val stats: TextView = view.findViewById(R.id.accountStats)
        val bar: ProgressBar = view.findViewById(R.id.accountAccuracyBar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.item_account_accuracy, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.username.text = "@${item.username}"
        holder.stats.text = "Total: ${item.total_predictions} • High: ${item.high_confidence} • Accuracy: ${item.accuracy * 100}%"
        holder.bar.progress = (item.accuracy * 100).toInt()
    }

    override fun getItemCount() = data.size
}
