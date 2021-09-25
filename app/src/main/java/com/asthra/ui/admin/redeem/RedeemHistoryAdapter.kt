package com.asthra.ui.admin.redeem

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.asthra.R
import com.asthra.data.api.response.HistoryRecords
import com.asthra.data.api.response.Records
import com.asthra.data.api.response.RedeemHistoryList
import com.asthra.data.api.response.RedeemListResponse
import kotlinx.android.synthetic.main.row_redeem_history.view.*

class RedeemHistoryAdapter(
    val context: Context,
    val redeemHistoryItemListener: RedeemHistoryItemListener
) : RecyclerView.Adapter<RedeemHistoryAdapter.CustomerViewHolder>() {

    val redeemListData = mutableListOf<HistoryRecords>()


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RedeemHistoryAdapter.CustomerViewHolder = CustomerViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.row_redeem_history, parent, false
        )
    )

    inner class CustomerViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bindUI(position: Int) {
            view.apply {
                redeemListData[position].let {
                    tvMobileNumber.text = it.mobile_number
                    tvPoints.text = it.points
                    tvFuelType.text = if (it.fuel_type == 1) "Petrol" else "Diesel"
                    tvTotalLitters.text = it.total_liters.toString()
                    tvCreatedDate.text = it.created_at.toString()


//                    llrowItem.setOnClickListener {
//                        redeemHistoryItemListener.clickItem(position)
//                    }
                }
            }
        }
    }

    fun addAAllData(_redeemList: List<HistoryRecords>) {
        redeemListData.addAll(_redeemList)
        notifyDataSetChanged()
    }

    fun clearAllData() {
        redeemListData.clear()
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RedeemHistoryAdapter.CustomerViewHolder, position: Int) {
        holder.bindUI(position)
    }

    override fun getItemCount(): Int {
        return redeemListData.size
    }
}

interface RedeemHistoryItemListener {
    fun clickItem(position: Int);
}