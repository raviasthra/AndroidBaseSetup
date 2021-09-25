package com.asthra.ui.admin

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.asthra.R
import com.asthra.data.api.response.HistoryList
import com.asthra.data.api.response.CustomerListResponse
import kotlinx.android.synthetic.main.row_customer_list.view.*

class CustomerListAdapter(
    val context: Context,
    val customerListItemListener: CustomerListItemListener
) : RecyclerView.Adapter<CustomerListAdapter.CustomerViewHolder>() {

    val customerListData = mutableListOf<HistoryList>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CustomerListAdapter.CustomerViewHolder = CustomerViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.row_customer_list, parent, false
        )
    )

    inner class CustomerViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bindUI(position: Int) {
            view.apply {
                customerListData[position].let {
                    tvMobileNumber.text = it.mobile_number
                    tvVehicleNumber.text = it.vehicle_number
                    tvFuelType.text = if (it.fuel_type == 1) "Petrol" else "Diesel"
                    tvLitters.text = it.total_liters.toString()
                    tvDate.text = it.created_at.toString()

                    llrowItem.setOnClickListener {
                        customerListItemListener.clickItem(position)
                    }
                }
            }
        }
    }

    fun addAllMatchesList(_customerListData: List<HistoryList>) {
        Log.e("addAllMatchesList","---")
        customerListData.addAll(_customerListData)
        notifyDataSetChanged()
    }

    fun clearAllMatchesList() {
        customerListData.clear()
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: CustomerViewHolder, position: Int) {
        holder.bindUI(position)
    }

    override fun getItemCount(): Int {
        return customerListData.size
    }
}

interface CustomerListItemListener {
    fun clickItem(position: Int);
}