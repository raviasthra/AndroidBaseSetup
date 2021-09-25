package com.asthra.ui.admin

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.asthra.R
import com.asthra.data.api.request.CustomerListRequest
import com.asthra.data.api.request.SaveSettingsRequest
import com.asthra.data.preference.IPreferenceManager
import com.asthra.data.viewmodel.DashboardViewModel
import com.asthra.databinding.FragmentDashboardBinding
import com.asthra.di.*
import com.asthra.ui.BaseFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.common_progress_bar.*
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_employee.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class DashboardFragment : BaseFragment(), CustomerListItemListener {

    private lateinit var fragmentDashboardBinding: FragmentDashboardBinding
    private val viewModelScope by viewModel<DashboardViewModel>()
    private lateinit var preferenceManager: IPreferenceManager
    private var diselBool = 0
    private var petrolBool = 0
    private var apiCall = true

    override fun onStart() {
        super.onStart()
        activity?.inToolbar?.visibility = View.VISIBLE
        activity?.menuImg?.visibility = View.VISIBLE
        activity?.backImg?.visibility = View.GONE
        activity?.toobarTitle?.text = "Admin"
        activity?.drawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

    }

    override fun onResume() {
        super.onResume()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentDashboardBinding = FragmentDashboardBinding.inflate(inflater, container, false)
        fragmentDashboardBinding.apply {
            lifecycleOwner = this@DashboardFragment
            executePendingBindings()
        }

        return fragmentDashboardBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferenceManager = IPreferenceManager.getPrefInstance(context!!)

        val layoutManager = LinearLayoutManager(requireContext())
        rvCustomersList.setLayoutManager(layoutManager)
        rvCustomersList.adapter = CustomerListAdapter(context!!, this)
        (rvCustomersList.adapter as CustomerListAdapter).clearAllMatchesList()

        rvCustomersList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (rvCustomersList.childCount != 0 && viewModelScope.totalRecord.value != layoutManager.itemCount) {
                    val view =
                        rvCustomersList.getChildAt(rvCustomersList.childCount - 1) as View
                    val diff: Int =
                        view.bottom - (rvCustomersList.height + rvCustomersList.scrollY)
                    if (diff <= 0) {
                        val visibleItemCount = layoutManager.childCount
                        val totalItemCount =
                            viewModelScope.totalRecord.value!! //layoutManager.itemCount
                        val pastVisiblesItems = layoutManager.findFirstVisibleItemPosition()
                        // val currentPage = absentViewModel.currentPageCount
                        if (viewModelScope.myLoading.value!!) {
                            if (visibleItemCount + pastVisiblesItems < totalItemCount) {
                                viewModelScope.myLoading.value = false
                                viewModelScope.currentPage.value =
                                    viewModelScope.currentPage.value!! + 1
                                onCallHistoryDetails(viewModelScope.currentPage.value!!)
                            }
                        }
                    }
                }
            }
        })

        cb_disel.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                diselBool = 1
            } else {
                diselBool = 0
            }
        }

        cb_petrol.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                petrolBool = 1
            } else {
                petrolBool = 0
            }
        }

        tv_history.setOnClickListener {
            (rvCustomersList.adapter as CustomerListAdapter).clearAllMatchesList()
            tvStartDate.setText("")
            tvEndDate.setText("")
            etSearch.setText("")
            ll_history.visibility = View.VISIBLE
            ll_settings.visibility = View.GONE
            tv_history.setTextColor(resources.getColor(R.color.colorWhite))
            tv_settings.setTextColor(resources.getColor(R.color.color_gray_text_light))
            onCallHistoryDetails(viewModelScope.currentPage.value!!)
        }

        tv_settings.setOnClickListener {
            (rvCustomersList.adapter as CustomerListAdapter).clearAllMatchesList()
            tvStartDate.setText("")
            tvEndDate.setText("")
            etSearch.setText("")
            ll_settings.visibility = View.VISIBLE
            ll_history.visibility = View.GONE
            tv_settings.setTextColor(resources.getColor(R.color.colorWhite))
            tv_history.setTextColor(resources.getColor(R.color.color_gray_text_light))
            onCallSettingsDetails()
        }

        save.setOnClickListener {
            callSaveFuelDetails()
        }

        llSearch.setOnClickListener {
            tvStartDate.setText("")
            tvEndDate.setText("")
            (rvCustomersList.adapter as CustomerListAdapter).clearAllMatchesList()
            onCallHistoryDetails(viewModelScope.currentPage.value!!)
        }

        tvStartDate.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val dpd = DatePickerDialog(
                activity!!,
                AlertDialog.THEME_DEVICE_DEFAULT_DARK,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    // Display Selected date in textbox
                    var dayOfMonths = dayOfMonth.toString()
                    var monthOfYears = (monthOfYear + 1).toString()

                    if (monthOfYear < 10) {
                        monthOfYears = "0" + (monthOfYear + 1).toString()
                    }
                    if (dayOfMonth < 10) {
                        dayOfMonths = "0" + dayOfMonth;
                    }
                    tvEndDate.setText("")
                    tvStartDate.setText("" + dayOfMonths + "-" + monthOfYears + "-" + year)
                },
                year,
                month,
                day
            )
            dpd.datePicker.setMaxDate(Date().getTime());
            dpd.show()
        }

        tvEndDate.setOnClickListener {
            if (tvStartDate.text.toString().length == 0) {
                activity!!.toast("Please select start date")
            } else {
                val c = Calendar.getInstance()
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)
                val dpd = DatePickerDialog(
                    activity!!,
                    AlertDialog.THEME_DEVICE_DEFAULT_DARK,
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        // Display Selected date in textbox
                        var dayOfMonths = dayOfMonth.toString()
                        var monthOfYears = (monthOfYear + 1).toString()

                        if (monthOfYear < 10) {
                            monthOfYears = "0" + (monthOfYear + 1).toString()
                        }
                        if (dayOfMonth < 10) {
                            dayOfMonths = "0" + dayOfMonth;
                        }
                        tvEndDate.setText("" + dayOfMonths + "-" + monthOfYears + "-" + year)
                    },
                    year,
                    month,
                    day
                )
                val date = SimpleDateFormat("dd-MM-yyyy").parse(tvStartDate.getText().toString())
                dpd.datePicker.setMinDate(date.time)
                dpd.datePicker.setMaxDate(Date().getTime());
                dpd.show()
            }
        }

        dateClear.setOnClickListener {
            tvStartDate.setText("")
            tvEndDate.setText("")
            (rvCustomersList.adapter as CustomerListAdapter).clearAllMatchesList()
            onCallHistoryDetails(viewModelScope.currentPage.value!!)
        }

        ivSearchDate.setOnClickListener {
            (rvCustomersList.adapter as CustomerListAdapter).clearAllMatchesList()
            etSearch.text?.clear()
            onCallHistoryDetails(viewModelScope.currentPage.value!!)
        }
        onCallHistoryDetails(viewModelScope.currentPage.value!!)
    }

    private fun callSaveFuelDetails() {
        progress.blockUI(activity!!, rlProgress)
        viewModelScope.onCallSaveSettingsDetails(
            SaveSettingsRequest(1, petrolBool, 2, diselBool),
            onSuccess = {
                progress.unBlockUI(activity!!, rlProgress)
                activity?.toast("" + it.message)
            },
            onError = {
                progress.unBlockUI(activity!!, rlProgress)
                activity?.toast("" + it)
            })
    }

    private fun onCallSettingsDetails() {
        progress.blockUI(activity!!, rlProgress)
        viewModelScope.onCallDashboardSettingDetails(
            onSuccess = {
                progress.unBlockUI(activity!!, rlProgress)
                if (it.data.get(0).status == 0) {
                    cb_petrol.isChecked = false
                } else {
                    cb_petrol.isChecked = true
                }
                if (it.data.get(1).status == 0) {
                    cb_disel.isChecked = false
                } else {
                    cb_disel.isChecked = true
                }
            },
            onError = {
                progress.unBlockUI(activity!!, rlProgress)
                activity?.toast(it)
            })
    }

    private fun onCallHistoryDetails(page: Int) {
        apiCall = false
        if (isNetworkAvailable(context!!)) {
            progress.blockUI(activity!!, rlProgress)
            viewModelScope.onCallDashboardDetailsCustomerList(
                CustomerListRequest(
                    "" + etSearch.text.toString(),
                    "" + tvStartDate.text.toString(),
                    "" + tvEndDate.text.toString(),
                    page
                ),
                onSuccess = {
                    apiCall = true
                    viewModelScope.totalRecord.value = it.data.TotalCount
                    viewModelScope.myLoading.value = true
                    progress.unBlockUI(activity!!, rlProgress)
                    if (it.data.Records.size == 0) {
                        if (it.data.TotalCount == 0) {
                            activity?.toast("No records found")
                        }
                    } else {
                        (rvCustomersList.adapter as CustomerListAdapter).addAllMatchesList(it.data.Records)
                    }
                },
                onError = {
                    apiCall = true
                    viewModelScope.myLoading.value = true
                    progress.unBlockUI(activity!!, rlProgress)
                })
        } else {
            activity!!.toast("No Internet Connection")
        }
    }

    override fun clickItem(position: Int) {

//        val dialog =
//            Dialog(requireContext(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen)
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        //dialog.setCancelable(false)
//        dialog.setContentView(R.layout.dialog_admin)
//        val lp = WindowManager.LayoutParams()
//        lp.copyFrom(dialog.window!!.attributes)
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
//        lp.gravity = Gravity.CENTER
//        dialog.window!!.attributes = lp
//
//        val body = dialog.findViewById(R.id.tvTitle) as TextView
//        body.text = "Redeem"
//
//        val yesBtn = dialog.findViewById(R.id.tvSubmit) as TextView
//
//        yesBtn.setOnClickListener {
//            dialog.dismiss()
//        }
//        dialog.show()
    }


}
