package com.asthra.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.asthra.databinding.CustomDialogListBinding
import com.asthra.ui.dialog.model.DialogModel
import kotlinx.android.synthetic.main.custom_dialog_list.*

class CustomDialogList(
    val dialogList: ArrayList<DialogModel>,
    val title: String,
    val listener: OnItemSelect
) : DialogFragment(), CustomDialogListAdapter.CustomDialogItemClickListener {

    private lateinit var customDialogListBinding: CustomDialogListBinding
    private lateinit var customDialogListAdapter: CustomDialogListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        customDialogListBinding =
            CustomDialogListBinding.inflate(inflater, container, false)

        //        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return customDialogListBinding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        tvCustomDialogTitle.text = title
        customDialogListBinding.lifecycleOwner = this.viewLifecycleOwner

        rvSelectItem.layoutManager = LinearLayoutManager(requireContext())

        customDialogListAdapter = CustomDialogListAdapter(requireContext(), this)
        rvSelectItem.adapter = customDialogListAdapter

        customDialogListAdapter.addCustomDialogListItem(dialogList)
    }

    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams = dialog!!.window!!.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams
    }

    override fun clickCustomDialogListener(position: Int) {
        listener.onSelectItemClick(dialogList[position])
        dismiss()
    }
}


interface OnItemSelect {
    fun onSelectItemClick(dialogModel: DialogModel)
}