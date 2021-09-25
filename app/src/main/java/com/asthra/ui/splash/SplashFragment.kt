package com.asthra.ui.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.asthra.R
import com.asthra.data.api.request.SplashRequest
import com.asthra.data.preference.IPreferenceManager
import com.asthra.data.viewmodel.SplashViewModel
import com.asthra.databinding.FragmentSplashBinding
import com.asthra.di.isNetworkAvailable
import com.asthra.di.toast
import com.asthra.di.utility.Pref.USER_ID
import com.asthra.ui.BaseFragment
import com.asthra.ui.dialog.CustomDialogList
import com.asthra.ui.dialog.OnItemSelect
import com.asthra.ui.dialog.model.DialogModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashFragment : BaseFragment() {

    private lateinit var splashBinding: FragmentSplashBinding
    private val viewModelScope by viewModel<SplashViewModel>()
    private lateinit var preferenceManager: IPreferenceManager

    override fun onStart() {
        super.onStart()
        activity?.inToolbar?.visibility = View.GONE
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        splashBinding = FragmentSplashBinding.inflate(inflater, container, false)
        splashBinding.apply {
            lifecycleOwner = this@SplashFragment
            executePendingBindings()
        }

        // splashBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_splash, container, false);

        return splashBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferenceManager = IPreferenceManager.getPrefInstance(context!!)

        when (isNetworkAvailable(context)) {
            true -> {
                Handler(Looper.getMainLooper()).postDelayed({
                    if (preferenceManager.getInt(USER_ID) == 1) {
                        when (findNavController().currentDestination?.id) {
                            R.id.splashFragment -> {
                                findNavController().navigate(
                                    R.id.action_splashFragment_to_employeeFragment, bundleOf()
                                )
                            }
                        }
                    } else if (preferenceManager.getInt(USER_ID) == 2) {
                        when (findNavController().currentDestination?.id) {
                            R.id.splashFragment -> {
                                findNavController().navigate(
                                    R.id.action_splashFragment_to_selectUserTypeFragment, bundleOf()
                                )
                            }
                        }
                    } else {
                        when (findNavController().currentDestination?.id) {
                            R.id.splashFragment -> {
                                findNavController().navigate(
                                    R.id.action_splashFragment_to_loginFragment, bundleOf()
                                )
                            }
                        }
                    }


                    /*viewModelScope.onCallSplashApi(onSuccess = {

                    }, onError = {

                    })*/
                    // callApi()
                    /* when (findNavController().currentDestination?.id) {
                         R.id.splashFragment -> {
                             findNavController().navigate(
                                 R.id.action_splashFragment_to_loginFragment, bundleOf(*//**//*)
                            )
                        }
                    }*/
                }, 3000)
            }
            else -> {
                activity?.toast("Check your internet connection")
            }
        }
    }

    private fun callApi() {
        viewModelScope.onCallSplashApi(SplashRequest("ravi"),
            onSuccess = {
                activity?.toast("success")
                when (findNavController().currentDestination?.id) {
                    R.id.splashFragment -> {
                        findNavController().navigate(
                            R.id.action_splashFragment_to_loginFragment, bundleOf()
                        )
                    }
                }
            }, onError = {
                activity?.toast("Failed")
            })
        /*val dialogModel = arrayListOf<DialogModel>()
        val dialogFragment = CustomDialogList(
            dialogModel,
            "",
            listener = object : OnItemSelect {
                override fun onSelectItemClick(dialogModel: DialogModel) {

                }
            })
        dialogFragment.show(activity!!.supportFragmentManager, dialogFragment.tag)*/
    }
}
