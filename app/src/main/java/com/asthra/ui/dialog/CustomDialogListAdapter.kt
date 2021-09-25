package com.asthra.ui.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.asthra.R
import com.asthra.ui.dialog.model.DialogModel
import kotlinx.android.synthetic.main.row_custom_dialog.view.*

class CustomDialogListAdapter(
    val context: Context,
    val customDialogItemClickListener: CustomDialogItemClickListener
) : RecyclerView.Adapter<CustomDialogListAdapter.ViewHolder>() {

    val dialogItemList = mutableListOf<DialogModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.row_custom_dialog,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = dialogItemList.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindUI(position)
    }

    fun addCustomDialogListItem(_dialogItemList: List<DialogModel>) {
        dialogItemList.addAll(_dialogItemList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each animal to
        fun bindUI(position: Int) {
            view.apply {
                dialogItemList[position].let {
                    tvCustomDialog.text = it.name
                    setOnClickListener {
                        customDialogItemClickListener.clickCustomDialogListener(position)
                    }
                }
            }
        }
    }

    interface CustomDialogItemClickListener {
        fun clickCustomDialogListener(position: Int)
    }
}