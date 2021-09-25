package com.asthra.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.findNavController
import com.asthra.R
import com.asthra.data.api.request.LoginRequest
import com.asthra.data.preference.IPreferenceManager
import com.asthra.data.viewmodel.LoginViewModel
import com.asthra.data.viewmodel.SplashViewModel
import com.asthra.databinding.FragmentLoginBinding
import com.asthra.di.*
import com.asthra.di.utility.Pref.USER_EMAIL
import com.asthra.di.utility.Pref.USER_ID
import com.asthra.di.utility.Pref.USER_NAME
import com.asthra.ui.BaseFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.common_progress_bar.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.regex.Pattern

class LoginFragment : BaseFragment() {

    private lateinit var fragmentLoginBinding: FragmentLoginBinding
    private val viewModelScope by viewModel<LoginViewModel>()
    private lateinit var preferenceManager: IPreferenceManager

    override fun onStart() {
        super.onStart()
        activity?.inToolbar?.visibility = View.GONE
        activity?.menuImg?.visibility = View.GONE
        activity?.backImg?.visibility = View.GONE
        activity?.drawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentLoginBinding = FragmentLoginBinding.inflate(inflater, container, false)
        fragmentLoginBinding.apply {
            lifecycleOwner = this@LoginFragment
            executePendingBindings()
        }

        return fragmentLoginBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        preferenceManager = IPreferenceManager.getPrefInstance(context!!)

        tvLoginBtn.setOnClickListener {
            activity?.hideKeyboard(it)
            if (isNetworkAvailable(context!!)) {
                if (onValidation()) {
                    callLoginApi()
                }
            } else {
                activity!!.toast("No internet connection")
            }
        }
    }

    private fun callLoginApi() {
        progress.blockUI(activity!!, rlProgress)
        viewModelScope.onCallLoginApi(
            LoginRequest(
                etMobileNumber.text.toString(),
                etPassword.text.toString()
            ), onSuccess = {
                progress.unBlockUI(activity!!, rlProgress)
                if (it.status_code == 200) {
                    activity?.toast(it.message)
                    preferenceManager.saveInt(USER_ID, it.data.id)
                    preferenceManager.saveString(USER_NAME, it.data.name)
                    preferenceManager.saveString(USER_EMAIL, it.data.email)

                    if (it.data.id == 1) {
                        when (findNavController().currentDestination?.id) {
                            R.id.loginFragment -> {
                                findNavController().navigate(
                                    R.id.action_loginFragment_to_employeeFragment, bundleOf()
                                )
                            }
                        }
                    } else if (it.data.id == 2) {
                        when (findNavController().currentDestination?.id) {
                            R.id.loginFragment -> {
                                findNavController().navigate(
                                    R.id.action_loginFragment_to_selectUserTypeFragment, bundleOf()
                                )
                            }
                        }
                    }
                } else {
                    activity?.toast(it.message)
                }
            }, onError = {
                progress.unBlockUI(activity!!, rlProgress)
                activity?.toast("Invalid login details")
            })
    }

    private fun onValidation(): Boolean {
        tilMobileNumber.error = ""
        tilPassword.error = ""
        if (!isValidEmailId(etMobileNumber.text.toString().trim())) {
            tilMobileNumber.error = "Enter valid email address"
            return false
        } else if (etPassword.text.toString().trim().length <= 3) {
            tilPassword.error = "Password must be minimum 4 character"
            return false
        }
        return true
    }

    private fun isValidEmailId(email: String): Boolean {
        return Pattern.compile(
            "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                    + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                    + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                    + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
        ).matcher(email).matches()
    }
}