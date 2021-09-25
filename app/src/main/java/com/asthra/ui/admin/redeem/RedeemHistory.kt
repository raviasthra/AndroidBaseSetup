package com.asthra.ui.admin.redeem

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.TextView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.asthra.R
import com.asthra.data.api.request.RedeemHistoryRequest
import com.asthra.data.api.request.RedeemListRequest
import com.asthra.data.api.response.Records
import com.asthra.data.api.response.RedeemRequest
import com.asthra.data.preference.IPreferenceManager
import com.asthra.data.viewmodel.AdminRedeemHistoryViewModel
import com.asthra.databinding.FragmentRedeemHistoryBinding
import com.asthra.di.blockUI
import com.asthra.di.isNetworkAvailable
import com.asthra.di.toast
import com.asthra.di.unBlockUI
import com.asthra.ui.BaseFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.common_progress_bar.*
import kotlinx.android.synthetic.main.dialog_admin.*
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_redeem_history.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class RedeemHistory : BaseFragment(), RedeemListItemListener, RedeemHistoryItemListener {

    private lateinit var fragmentRedeemHistoryBinding: FragmentRedeemHistoryBinding
    private val viewModelScope by viewModel<AdminRedeemHistoryViewModel>()
    private lateinit var preferenceManager: IPreferenceManager

    var redeemList: Int = 0
    var fuel_type: Int = 1

    var selectedPosition: Int = 1

    val redeemListData = mutableListOf<Records>()
    val redeemHistoryData = mutableListOf<Records>()


    override fun onStart() {
        super.onStart()
        activity?.inToolbar?.visibility = View.VISIBLE
        activity?.menuImg?.visibility = View.GONE
        activity?.backImg?.visibility = View.VISIBLE
        activity?.toobarTitle?.text = "Redeem List"
        activity?.drawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentRedeemHistoryBinding =
            FragmentRedeemHistoryBinding.inflate(inflater, container, false)
        fragmentRedeemHistoryBinding.apply {
            lifecycleOwner = this@RedeemHistory
            executePendingBindings()
        }
        return fragmentRedeemHistoryBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vHistory.setBackgroundColor(resources.getColor(R.color.colorOrange))
        vRedeems.setBackgroundColor(resources.getColor(R.color.indicator))
        ll_date_filter.visibility = View.GONE

        val layoutManager = LinearLayoutManager(requireContext())
        rvRedeemList.setLayoutManager(layoutManager)
        rvRedeemList.adapter = RedeemListAdapter(context!!, this)

        tv_redeems.setOnClickListener {
            vHistory.setBackgroundColor(resources.getColor(R.color.colorOrange))
            vRedeems.setBackgroundColor(resources.getColor(R.color.indicator))
            tv_redeems.setTextColor(resources.getColor(R.color.colorWhite))
            tv_redeem_history.setTextColor(resources.getColor(R.color.color_gray_text_light))
            redeemList = 0
            redeemListData.clear()
            rvRedeemList.adapter = RedeemListAdapter(context!!, this)
            (rvRedeemList.adapter as RedeemListAdapter).clearAllData()
            onCallRedeemList(etSearchRedeem.text!!.toString())
        }

        tv_redeem_history.setOnClickListener {
            vHistory.setBackgroundColor(resources.getColor(R.color.indicator))
            vRedeems.setBackgroundColor(resources.getColor(R.color.colorOrange))
            tv_redeem_history.setTextColor(resources.getColor(R.color.colorWhite))
            tv_redeems.setTextColor(resources.getColor(R.color.color_gray_text_light))
            redeemList = 1
            rvRedeemList.adapter = RedeemHistoryAdapter(context!!, this)
            (rvRedeemList.adapter as RedeemHistoryAdapter).clearAllData()
            onCallHistoryDetails(
                etSearchRedeem.text!!.toString(),
                viewModelScope.currentPage.value!!
            )
        }

        ivSearch.setOnClickListener {
            if (etSearchRedeem.text!!.toString().length > 5) {
                if (redeemList == 0) {
                    redeemListData.clear()
                    (rvRedeemList.adapter as RedeemListAdapter).clearAllData()
                    onCallRedeemList(etSearchRedeem.text!!.toString())
                } else {
                    (rvRedeemList.adapter as RedeemHistoryAdapter).clearAllData()
                    onCallHistoryDetails(
                        etSearchRedeem.text!!.toString(),
                        viewModelScope.currentPage.value!!
                    )
                }
            } else {
                activity!!.toast("Please enter your phone number")
            }
        }

        rvRedeemList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (rvRedeemList.childCount != 0) {
                    val view =
                        rvRedeemList.getChildAt(rvRedeemList.childCount - 1) as View
                    val diff: Int =
                        view.bottom - (rvRedeemList.height + rvRedeemList.scrollY)
                    if (diff <= 0) {
                        val visibleItemCount = layoutManager.childCount
                        val totalItemCount =
                            viewModelScope.totalRecord.value!! //layoutManager.itemCount
                        //val totalItemCount = layoutManager.itemCount
                        val pastVisiblesItems = layoutManager.findFirstVisibleItemPosition()
                        // val currentPage = absentViewModel.currentPageCount
                        Log.e(
                            "totalItemCount",
                            "" + visibleItemCount + "---" + totalItemCount + "---" + pastVisiblesItems + "---" +
                                    viewModelScope.myLoading.value!!
                        )
                        if (viewModelScope.myLoading.value!!) {
                            if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
                                viewModelScope.currentPage.value =
                                    viewModelScope.currentPage.value!! + 1
                                viewModelScope.myLoading.value = false
                                onCallHistoryDetails(
                                    etSearchRedeem.text!!.toString(),
                                    viewModelScope.currentPage.value!!
                                )
                            }
                        }
                    }
                }
            }
        })
        onCallRedeemList("")
    }

    private fun onCallHistoryDetails(mobile_number: String, page: Int) {
        if (isNetworkAvailable(context!!)) {
            progress.blockUI(activity!!, rlProgress)
            viewModelScope.onCallRedeemHistory(
                RedeemHistoryRequest(
                    mobile_number, "", "", page
                ),
                onSuccess = {
                    progress.unBlockUI(activity!!, rlProgress)
                    if (it.data.Records.size > 0) {
                        viewModelScope.totalRecord.value = it.data.TotalCount
                        (rvRedeemList.adapter as RedeemHistoryAdapter).addAAllData(it.data.Records)
                    } else {
                        if (it.data.TotalCount == 0) {
                            activity?.toast("No records found")
                        }
                    }
                },
                onError = {
                    viewModelScope.myLoading.value = true
                    progress.unBlockUI(activity!!, rlProgress)
                })
        } else {
            activity?.toast("No Internet Connection")
        }
    }

    private fun onCallRedeemList(mobileNumber: String) {
        if (isNetworkAvailable(context!!)) {
            progress.blockUI(activity!!, rlProgress)
            viewModelScope.onCallRedeemList(
                RedeemListRequest(mobileNumber),
                onSuccess = {
                    progress.unBlockUI(activity!!, rlProgress)
                    if (it.data.Records.size > 0) {
                        redeemListData.addAll(it.data.Records)
                        (rvRedeemList.adapter as RedeemListAdapter).addAAllData(redeemListData)
                    } else {
                        activity?.toast("No records found")
                    }
                },
                onError = {
                    progress.unBlockUI(activity!!, rlProgress)
                    activity!!.toast(it.toString())
                })
        } else {
            activity!!.toast("No Internet Connection")
        }
    }

    override fun clickItem(position: Int) {
        if (redeemList == 0) {
            selectedPosition = position
            val dialog =
                Dialog(requireContext(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            //dialog.setCancelable(false)
            dialog.setContentView(R.layout.dialog_admin)
            val lp = WindowManager.LayoutParams()
            lp.copyFrom(dialog.window!!.attributes)
            lp.width = WindowManager.LayoutParams.MATCH_PARENT
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT
            lp.gravity = Gravity.CENTER
            dialog.window!!.attributes = lp

            val body = dialog.findViewById(R.id.tvTitle) as TextView
            body.text = "Redeem"

            val submit = dialog.findViewById(R.id.tvSubmit) as TextView
            val cancel = dialog.findViewById(R.id.tvCancel) as TextView
            val tv_redeem_petrol = dialog.findViewById(R.id.tv_redeem_petrol) as TextView
            val tv_redeem_diesel = dialog.findViewById(R.id.tv_redeem_diesel) as TextView
            val tvMobileNumber = dialog.findViewById(R.id.tvMobileNumber) as TextView
            val tv_available_petrol = dialog.findViewById(R.id.tv_available_petrol) as TextView
            val tv_available_diesel = dialog.findViewById(R.id.tv_available_diesel) as TextView
            val tv_points = dialog.findViewById(R.id.tv_points) as TextView
            val et_points = dialog.findViewById(R.id.et_points) as TextView

            val et_enter_fuel = dialog.findViewById(R.id.et_enter_fuel) as EditText

            et_enter_fuel.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

                override fun afterTextChanged(s: Editable?) {
                    if (!s.toString().isEmpty() && !s.toString().equals(".")) {
                        if (fuel_type == 1) {
                            val maxPetrol: Float =
                                redeemListData.get(position).total_petrol_ltr.replace(",", "")
                                    .toFloat() - redeemListData.get(
                                    position
                                ).total_redeemed_petrol_ltr.replace(",", "").toFloat()

                            if (maxPetrol == 0.toFloat() || s.toString()
                                    .toFloat() > maxPetrol
                            ) {
                                activity?.toast("You have entered invalid redeem details")
                                et_enter_fuel.text.clear()
                            }
                        } else {
                            val maxDiesel: Float =
                                redeemListData.get(position).total_diesel_ltr.replace(",", "")
                                    .toFloat() - redeemListData.get(
                                    position
                                ).total_redeemed_diesel_ltr.replace(",", "").toFloat()
                            if (maxDiesel == 0.toFloat() || s.toString().toFloat() > maxDiesel) {
                                et_enter_fuel.text.clear()
                                activity?.toast("You have entered invalid redeem details")
                            }
                        }
                    } else {
                        et_enter_fuel.text.clear()
                    }
                }
            })

            tvMobileNumber.text = redeemListData.get(position).mobile_number
            tv_available_petrol.text =
                (redeemListData.get(position).total_petrol_ltr.replace(",", "")
                    .toFloat() - redeemListData.get(
                    position
                ).total_redeemed_petrol_ltr.replace(",", "").toFloat()).toString()
            tv_available_diesel.text =
                (redeemListData.get(position).total_diesel_ltr.replace(",", "")
                    .toFloat() - redeemListData.get(
                    position
                ).total_redeemed_diesel_ltr.replace(",", "").toFloat()).toString()

            tv_redeem_petrol.setOnClickListener {
                et_enter_fuel.text.clear()
                fuel_type = 1
                tv_redeem_petrol.setTextColor(resources.getColor(R.color.colorWhite))
                tv_redeem_diesel.setTextColor(resources.getColor(R.color.color_gray_text_light))
            }

            tv_redeem_diesel.setOnClickListener {
                et_enter_fuel.text.clear()
                fuel_type = 2
                tv_redeem_diesel.setTextColor(resources.getColor(R.color.colorWhite))
                tv_redeem_petrol.setTextColor(resources.getColor(R.color.color_gray_text_light))
            }

            tv_points.text = redeemListData.get(position).total_points

            cancel.setOnClickListener {
                dialog.dismiss()
            }

            submit.setOnClickListener {
                val points = et_points.text.toString()

                if (!et_enter_fuel.text.toString().isEmpty() && !et_enter_fuel.text.toString()
                        .equals(".") && et_enter_fuel.text.toString()
                        .toFloat() > 0
                ) {
                    if (isNetworkAvailable(context!!)) {
                        callRedeemApi(
                            points,
                            tvMobileNumber.text.toString(),
                            fuel_type,
                            et_enter_fuel.text.toString().toFloat()
                        )
                    } else {
                        activity?.toast("No Internet Connection")
                    }
                    dialog.dismiss()
                } else {
                    activity?.toast("Please enter redeem details")
                }
            }
            dialog.show()
        } else {

        }
    }

    private fun callRedeemApi(
        points: String,
        mobileNumber: String,
        fuel_types: Int,
        et_enter_fuel: Float
    ) {
        progress.blockUI(activity!!, rlProgress)
        viewModelScope.onCallRedeem(
            RedeemRequest(mobileNumber, fuel_types, et_enter_fuel, points),
            onSuccess = {
                progress.unBlockUI(activity!!, rlProgress)
                activity?.toast("" + it.message)
                if (it.status_code == 200) {
                    if (fuel_type == 1) {
                        redeemListData.get(selectedPosition).total_redeemed_petrol_ltr =
                            (redeemListData.get(selectedPosition).total_redeemed_petrol_ltr.replace(
                                ",",
                                ""
                            ).toFloat() + et_enter_fuel.toFloat()).toString()
                        redeemListData.get(selectedPosition).total_points =
                            (redeemListData.get(selectedPosition).total_points.replace(
                                ",",
                                ""
                            ).toFloat() + points.toFloat()).toString()
                        (rvRedeemList.adapter as RedeemListAdapter).notifyItemChanged(
                            selectedPosition
                        )
                    } else {
                        redeemListData.get(selectedPosition).total_redeemed_diesel_ltr =
                            (redeemListData.get(selectedPosition).total_redeemed_diesel_ltr.replace(
                                ",",
                                ""
                            ).toFloat() + et_enter_fuel.toFloat()).toString()
                        (rvRedeemList.adapter as RedeemListAdapter).notifyItemChanged(
                            selectedPosition
                        )
                        redeemListData.get(selectedPosition).total_points =
                            (redeemListData.get(selectedPosition).total_points.replace(
                                ",",
                                ""
                            ).toFloat() + points.toFloat()).toString()
                        (rvRedeemList.adapter as RedeemListAdapter).notifyItemChanged(
                            selectedPosition
                        )
                    }
                }
            },
            onError = {
                progress.unBlockUI(activity!!, rlProgress)
                activity!!.toast(it.toString())
            })
    }
}