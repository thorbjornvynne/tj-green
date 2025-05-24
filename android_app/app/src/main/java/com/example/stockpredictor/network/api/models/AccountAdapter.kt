
package com.example.stockpredictor.adapters

import android.content.Context
import android.widget.CompoundButton
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.stockpredictor.R
import com.example.stockpredictor.api.models.AccountInfo

class AccountAdapter(
    private val context: Context,
    private val accounts: List<AccountInfo>,
    private val toggleCallback: (AccountInfo, Boolean) -> Unit
) : RecyclerView.Adapter<AccountAdapter.AccountViewHolder>() {

    class AccountViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val usernameText: TextView = view.findViewById(R.id.usernameText)
        val enableSwitch: Switch = view.findViewById(R.id.enableSwitch)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_account_card, parent, false)
        return AccountViewHolder(view)
    }

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        val account = accounts[position]
        holder.usernameText.text = account.username
        holder.enableSwitch.isChecked = account.enabled
        holder.enableSwitch.setOnCheckedChangeListener(null)
        holder.enableSwitch.setOnCheckedChangeListener { _: CompoundButton, isChecked: Boolean ->
            toggleCallback(account, isChecked)
        }
    }

    override fun getItemCount() = accounts.size
}
