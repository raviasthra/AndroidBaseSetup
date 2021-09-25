package com.asthra.ui.admin.fuel

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.SmsManager
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.asthra.R
import com.asthra.data.api.request.CustomerSaveDetailsRequest
import com.asthra.data.preference.IPreferenceManager
import com.asthra.data.viewmodel.EmployeeViewModel
import com.asthra.databinding.FragmentAdminFuelBinding
import com.asthra.di.blockUI
import com.asthra.di.isNetworkAvailable
import com.asthra.di.toast
import com.asthra.di.unBlockUI
import com.asthra.di.utility.SimUtil
import com.asthra.ui.BaseFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.common_progress_bar.*
import kotlinx.android.synthetic.main.fragment_employee.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class FuelFragment : BaseFragment() {

    private lateinit var fragmentAdminFuelBinding: FragmentAdminFuelBinding
    private val viewModelScope by viewModel<EmployeeViewModel>()
    private lateinit var preferenceManager: IPreferenceManager
    private var isPetrol: Boolean = false
    val REQUEST_LOCATION = 0
    private var petrolStatus: Boolean = true
    private var diselStatus: Boolean = true

    var mobile_number_ = ""
    var vehicle_number = ""
    var fuel_filled = ""
    var points = ""
    var total_petrol = ""
    var total_diesel = ""
    var new_petrol : Float = 0F
    var new_diesel : Float = 0F

    override fun onStart() {
        super.onStart()
        activity?.inToolbar?.visibility = View.VISIBLE
        activity?.menuImg?.visibility = View.GONE
        activity?.backImg?.visibility = View.VISIBLE
        activity?.toobarTitle?.text = "Fuel"
        activity?.drawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentAdminFuelBinding = FragmentAdminFuelBinding.inflate(inflater, container, false)
        fragmentAdminFuelBinding.apply {
            lifecycleOwner = this@FuelFragment
            executePendingBindings()
        }
        return fragmentAdminFuelBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isNetworkAvailable(context!!)) {
            progress.blockUI(activity!!, rlProgress)
            viewModelScope.onCallFuelSetting(onSuccess = {
                progress.unBlockUI(activity!!, rlProgress)
                if (it.data.get(0).status == 0) {
                    tvPetrol.visibility = View.GONE
                } else {
                    tvPetrol.visibility = View.VISIBLE
                    petrolStatus = true
                    isPetrol = true
                    tvPetrol.setTextColor(context!!.getColor(R.color.colorWhite))
                    tvDisel.setTextColor(context!!.getColor(R.color.color_on_surface))
                    tvPetrol.background =
                        context!!.resources.getDrawable(R.drawable.white_border_shape)
                    tvDisel.background =
                        context!!.resources.getDrawable(R.drawable.gray_border_shape)
                }
                if (it.data.get(1).status == 0) {
                    tvDisel.visibility = View.GONE
                } else {
                    tvDisel.visibility = View.VISIBLE
                    diselStatus = true
                    if (!isPetrol) {
                        tvDisel.setTextColor(context!!.getColor(R.color.colorWhite))
                        tvPetrol.setTextColor(context!!.getColor(R.color.color_on_surface))
                        tvPetrol.background =
                            context!!.resources.getDrawable(R.drawable.gray_border_shape)
                        tvDisel.background =
                            context!!.resources.getDrawable(R.drawable.white_border_shape)
                    }
                }
                progress.unBlockUI(activity!!, rlProgress)
            }, onError = {
                progress.unBlockUI(activity!!, rlProgress)
                activity?.toast(it)
            })
        } else {
            activity!!.toast("No Internet Connection")
        }
        etVehicleNumber.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(arg0: CharSequence, arg1: Int, arg2: Int, arg3: Int) {}
            override fun beforeTextChanged(
                arg0: CharSequence, arg1: Int, arg2: Int,
                arg3: Int
            ) {
            }

            override fun afterTextChanged(et: Editable) {
                etVehicleNumber.removeTextChangedListener(this)
                var s = et.toString()
                if (s != s.toUpperCase()) {
                    s = s.toUpperCase()
                    etVehicleNumber.setText(s)
                    etVehicleNumber.setSelection(etVehicleNumber.length()) //fix reverse texting
                } else {
                    etVehicleNumber.setText(s)
                    etVehicleNumber.setSelection(etVehicleNumber.length()) //fix reverse texting
                }
                etVehicleNumber.addTextChangedListener(this)
            }
        })

        tvPetrol.setOnClickListener {
            isPetrol = true
            tvPetrol.setTextColor(context!!.getColor(R.color.colorWhite))
            tvDisel.setTextColor(context!!.getColor(R.color.color_on_surface))
            tvPetrol.background = context!!.resources.getDrawable(R.drawable.white_border_shape)
            tvDisel.background = context!!.resources.getDrawable(R.drawable.gray_border_shape)
        }

        tvDisel.setOnClickListener {
            isPetrol = false
            tvDisel.setTextColor(context!!.getColor(R.color.colorWhite))
            tvPetrol.setTextColor(context!!.getColor(R.color.color_on_surface))
            tvPetrol.background = context!!.resources.getDrawable(R.drawable.gray_border_shape)
            tvDisel.background = context!!.resources.getDrawable(R.drawable.white_border_shape)
        }

        tvSubmit.setOnClickListener {
            if (petrolStatus == true || diselStatus == true) {
                if (validation()) {
                    val dialog =
                        Dialog(
                            requireContext(),
                            android.R.style.Theme_Translucent_NoTitleBar_Fullscreen
                        )
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    //dialog.setCancelable(false)
                    dialog.setContentView(R.layout.dialog_preview_details)
                    val lp = WindowManager.LayoutParams()
                    lp.copyFrom(dialog.window!!.attributes)
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT
                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT
                    lp.gravity = Gravity.CENTER
                    dialog.window!!.attributes = lp

                    val mobile_number_dialog =
                        dialog.findViewById(R.id.tv_mobile_number) as TextView
                    val vehicle_number_dialog =
                        dialog.findViewById(R.id.tv_vehicle_number) as TextView
                    val fuel_type_dialog = dialog.findViewById(R.id.tv_fuel_type) as TextView
                    val litter = dialog.findViewById(R.id.tv_litter) as TextView

                    val tvSubmitDialog = dialog.findViewById(R.id.tvSubmitDialog) as TextView
                    val tvCancelDialog = dialog.findViewById(R.id.tvCancel) as TextView

                    mobile_number_dialog.text = etMobileNumber.text.toString()
                    vehicle_number_dialog.text = etVehicleNumber.text.toString()
                    fuel_type_dialog.text = if (isPetrol) "Petrol" else "Diesel"
                    litter.text = etFuelDetails.text.toString()

                    tvCancelDialog.setOnClickListener {
                        dialog.dismiss()
                    }

                    tvSubmitDialog.setOnClickListener {

                        if (ContextCompat.checkSelfPermission(
                                context!!,
                                Manifest.permission.SEND_SMS
                            ) != PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(
                                context!!,
                                Manifest.permission.SEND_SMS
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            dialog.dismiss()
                            requestPermissions(
                                arrayOf(Manifest.permission.SEND_SMS),
                                REQUEST_LOCATION
                            )
                        } else {
                            dialog.dismiss()
                            callSaveCustomerDetails()
                        }
                    }
                    dialog.show()
                }
            } else {
                activity?.toast("Not allowed")
            }
        }
    }

    private fun callSaveCustomerDetails() {

        if (isNetworkAvailable(context!!)) {
            progress.blockUI(activity!!, rlProgress)
            viewModelScope.onCallEmploySaveApi(
                CustomerSaveDetailsRequest(
                    etMobileNumber.text.toString(),
                    etVehicleNumber.text.toString(),
                    etFuelDetails.text.toString(),
                    if (isPetrol) 1 else 2
                ),
                onSuccess = {
                    if (it.status_code == 200) {
                        activity?.toast(it.message)
                        mobile_number_ = it.data.get(0).mobile_number
                        points = it.data.get(0).total_points
                        total_petrol = it.data.get(0).total_petrol_ltr
                        total_diesel = it.data.get(0).total_diesel_ltr
                        new_petrol = it.data.get(0).total_petrol_ltr.replace(",","").toFloat() - it.data.get(0).total_redeemed_petrol_ltr.replace(",","").toFloat()
                        new_diesel =  it.data.get(0).total_diesel_ltr.replace(",","").toFloat() - it.data.get(0).total_redeemed_diesel_ltr.replace(",","").toFloat()
                        vehicle_number = etVehicleNumber.text.toString()
                        fuel_filled = etFuelDetails.text.toString()
                        callSendSMS(mobile_number_)
                    } else {
                        progress.unBlockUI(activity!!, rlProgress)
                        activity?.toast(it.message)
                    }
                },
                onError = {
                    progress.unBlockUI(activity!!, rlProgress)
                    activity?.toast(it)
                })
        } else {
            activity!!.toast("No Internet Connection")
        }
    }

    private fun callSendSMS(etMobileNumber_: String) {

        val sms = SmsManager.getDefault()

        val headerName = "PonKrishna Fuels "
        val vehicleNumber = "Vehicle : " + vehicle_number
        val myPoints = "Points : " + points
        var fuelType = ""
        if (isPetrol) {
            fuelType = "Fuel filled: Petrol " + etFuelDetails.text.toString() + " L"
        } else {
            fuelType = "Fuel filled: Diesel " + etFuelDetails.text.toString() + " L"
        }

        //val redeem_petrol_ = "Bal Petrol : " + new_petrol
        //val redeem_diesel_ = "Bal Diesel : " + new_diesel

        //  val total_petrol_ = "Total Petrol : " + total_petrol
        //  val total_diesel_ = "Total Diesel : " + total_diesel

        val new_bal = "New Bal: Petrol " + new_petrol + " L," + "Diesel " + new_diesel + " L"
        val total_bal = "Total Fuel: Petrol " + total_petrol + " L," + "Diesel " + total_diesel + " L"

        val msgDetails: SpannableStringBuilder = SpannableStringBuilder()
        msgDetails.append(headerName + '\n')
        msgDetails.append(vehicleNumber + '\n')
       // msgDetails.append(myPoints + '\n')
        msgDetails.append(fuelType + '\n')
        msgDetails.append(new_bal + '\n')
        msgDetails.append(total_bal + '\n')
        //    msgDetails.append(total_petrol_ + '\n')
        //    msgDetails.append(total_diesel_ + '\n')

        val textMsg = msgDetails.toString()
        sms.sendTextMessage(
            "+91" + etMobileNumber_,
            "",
            "" + textMsg,
            null,
            null
        )

//        val mobileNumber: String =
//            ("+91" + etMobileNumber_).trim()
//        val smsManager = SmsManager.getDefault() as SmsManager
//        smsManager.sendTextMessage(mobileNumber, null, textMsg, null, null)

// Not working
//     val simUtil = SimUtil()
//        simUtil.sendSMS(
//            context!!,
//            1,
//            "9865579797",
//            null,
//            "Hi Stackoverflow! its me Ravi. Sent by sim1",
//            null,
//            null
//        );

//        SimUtil.sendSMS(
//            activity!!,
//            0,
//            "+91" + etMobileNumber_,
//            null,
//            textMsg,
//            null,
//            null
//        );
        progress.unBlockUI(activity!!, rlProgress)
        etMobileNumber.text?.clear()
        etVehicleNumber.text?.clear()
        etFuelDetails.text?.clear()
        etMobileNumber.requestFocus()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_LOCATION -> {
                if (grantResults.size > 0
                    && grantResults[0] === PackageManager.PERMISSION_GRANTED
                ) {
                    if (isNetworkAvailable(context!!)) {
                        callSaveCustomerDetails()
                    } else {
                        activity!!.toast("No Internet Connection")
                    }
                    //call your action
                } else {
                    Toast.makeText(activity, "Permission denied", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun validation(): Boolean {
        tilMobileNumber.error = ""
        tilVehicleNumber.error = ""
        tilFuelDetails.error = ""
        if (etMobileNumber.text.toString().length != 10) {
            tilMobileNumber.error = "Please enter your 10 digit mobile number"
            return false
        } else if (etVehicleNumber.text.toString().length < 6) {
            tilVehicleNumber.error = "Please enter vehicle number"
            return false
        } else if (etFuelDetails.text.toString().length == 0 || etFuelDetails.text.toString()
                .equals(".") || etFuelDetails.text.toString()
                .toDouble() == 0.0
        ) {
            tilFuelDetails.error = "Please enter fuel details"
            return false
        } else {
            return true
        }
    }
}

