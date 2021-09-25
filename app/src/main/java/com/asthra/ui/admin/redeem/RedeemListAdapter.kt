package com.asthra.ui.admin.redeem

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.asthra.R
import com.asthra.data.api.response.Records
import kotlinx.android.synthetic.main.row_redeem_history.view.llrowItem
import kotlinx.android.synthetic.main.row_redeem_history.view.tvMobileNumber
import kotlinx.android.synthetic.main.row_redeem_list.view.*

class RedeemListAdapter(
    val context: Context,
    val redeemHistoryItemListener: RedeemListItemListener
) : RecyclerView.Adapter<RedeemListAdapter.CustomerViewHolder>() {

    val redeemListData = mutableListOf<Records>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RedeemListAdapter.CustomerViewHolder = CustomerViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.row_redeem_list, parent, false
        )
    )

    inner class CustomerViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bindUI(position: Int) {
            view.apply {
                redeemListData[position].let {
                    tvMobileNumber.text = it.mobile_number
                    tvTotalPoints.text =
                        if (it.total_points == null || it.total_points.isEmpty()) "0.00" else it.total_points
                    tvTotalLitterOfPetrol.text = it.total_petrol_ltr.toString()
                    tvTotalLitterOfDiesel.text = it.total_diesel_ltr.toString()
                    tvTotalRedeemOfPetrol.text = it.total_redeemed_petrol_ltr.toString()
                    tvTotalRedeemOfDiesel.text = it.total_redeemed_diesel_ltr.toString()
                    tvBalanceRedeemOfPetrol.text =
                        (it.total_petrol_ltr.replace(",", "")
                            .toFloat() - it.total_redeemed_petrol_ltr.replace(",", "")
                            .toFloat()).toString()
                    tvBalanceRedeemOfDiesel.text =
                        (it.total_diesel_ltr.replace(",", "")
                            .toFloat() - it.total_redeemed_diesel_ltr.replace(",", "")
                            .toFloat()).toString()
                    llrowItem.setOnClickListener {
                        redeemHistoryItemListener.clickItem(position)
                    }
                }
            }
        }
    }

    fun addAAllData(_redeemList: List<Records>) {
        redeemListData.addAll(_redeemList)
        notifyDataSetChanged()
    }

    fun clearAllData() {
        redeemListData.clear()
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RedeemListAdapter.CustomerViewHolder, position: Int) {
        holder.bindUI(position)
    }

    override fun getItemCount(): Int {
        return redeemListData.size
    }
}

interface RedeemListItemListener {
    fun clickItem(position: Int);
}