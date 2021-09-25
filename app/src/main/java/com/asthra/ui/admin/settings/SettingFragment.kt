package com.asthra.ui.admin.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import com.asthra.data.api.request.ChangePasswordRequest
import com.asthra.data.api.request.SaveSettingsRequest
import com.asthra.data.preference.IPreferenceManager
import com.asthra.data.viewmodel.DashboardViewModel
import com.asthra.databinding.FragmentSettingBinding
import com.asthra.di.blockUI
import com.asthra.di.isNetworkAvailable
import com.asthra.di.toast
import com.asthra.di.unBlockUI
import com.asthra.ui.BaseFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.common_progress_bar.*
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_dashboard.cb_disel
import kotlinx.android.synthetic.main.fragment_dashboard.cb_petrol
import kotlinx.android.synthetic.main.fragment_dashboard.save
import kotlinx.android.synthetic.main.fragment_setting.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingFragment : BaseFragment() {

    private lateinit var fragmentSettingBinding: FragmentSettingBinding
    private val viewModelScope by viewModel<DashboardViewModel>()
    private lateinit var preferenceManager: IPreferenceManager
    private var diselBool = 0
    private var petrolBool = 0

    override fun onStart() {
        super.onStart()
        activity?.inToolbar?.visibility = View.VISIBLE
        activity?.menuImg?.visibility = View.GONE
        activity?.backImg?.visibility = View.VISIBLE
        activity?.toobarTitle?.text = "Settings"
        activity?.drawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentSettingBinding = FragmentSettingBinding.inflate(inflater, container, false)
        fragmentSettingBinding.apply {
            lifecycleOwner = this@SettingFragment
            executePendingBindings()
        }
        return fragmentSettingBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        onCallSettingsDetails()

        save.setOnClickListener {
            callSaveFuelDetails()
        }

        savePassword.setOnClickListener {
            if (etOldPassword.text.toString().length <= 5) {
                tilOldPassword.error = "Old password must be 6 character"
            } else if (etNewPassword.text.toString().length <= 5) {
                tilNewPassword.error = "New password must be 6 character"
            } else if (etConfirmPassword.text.toString().length <= 5) {
                tilConfirmPassword.error = "Confirm password must be 6 character"
            } else if (!etNewPassword.text.toString().equals(etConfirmPassword.text.toString())) {
                etNewPassword.text!!.clear()
                etConfirmPassword.text!!.clear()
            } else {
                saveChangePassword()
            }
        }
    }

    private fun saveChangePassword() {
        progress.blockUI(activity!!, rlProgress)
        viewModelScope.onCallChangePassword(
            ChangePasswordRequest(
                1,
                etOldPassword.text.toString(),
                etNewPassword.text.toString(),
                etConfirmPassword.text.toString()
            ),
            onSuccess = {
                progress.unBlockUI(activity!!, rlProgress)
                activity?.toast("" + it.message)
                etOldPassword.text!!.clear()
                etNewPassword.text!!.clear()
                etConfirmPassword.text!!.clear()
            },
            onError = {
                progress.unBlockUI(activity!!, rlProgress)
                activity?.toast("" + it)
            })

    }

    private fun callSaveFuelDetails() {
        if (isNetworkAvailable(context!!)) {
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
        } else {
            activity!!.toast("No Internet Connection")
        }
    }

    private fun onCallSettingsDetails() {
        if (isNetworkAvailable(context!!)) {
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
        } else {
            activity!!.toast("No Internet Connection")
        }
    }
}