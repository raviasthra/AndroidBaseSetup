package com.asthra.ui.userType

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.findNavController
import com.asthra.R
import com.asthra.data.preference.IPreferenceManager
import com.asthra.data.viewmodel.SplashViewModel
import com.asthra.databinding.FragmentSplashBinding
import com.asthra.databinding.FragmentUserTypeBinding
import com.asthra.ui.BaseFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_user_type.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SelectUserTypeFragment : BaseFragment() {

    private lateinit var userTypeBinding: FragmentUserTypeBinding
    private val viewModelScope by viewModel<SplashViewModel>()
    private lateinit var preferenceManager: IPreferenceManager

    override fun onStart() {
        super.onStart()
        activity?.inToolbar?.visibility = View.GONE
        activity?.drawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        userTypeBinding = FragmentUserTypeBinding.inflate(inflater, container, false)

        userTypeBinding.apply {
            lifecycleOwner = this@SelectUserTypeFragment
            executePendingBindings()
        }

        return userTypeBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        tvEmployee.setOnClickListener {
            when (findNavController().currentDestination?.id) {
                R.id.selectUserTypeFragment -> {
                    findNavController().navigate(
                        R.id.action_selectUserTypeFragment_to_employeeFragment, bundleOf(/**/)
                    )
                }
            }
        }

        tvAdmin.setOnClickListener {
            when (findNavController().currentDestination?.id) {
                R.id.selectUserTypeFragment -> {
                    findNavController().navigate(
                        R.id.action_selectUserTypeFragment_to_dashboardFragment, bundleOf(/**/)
                    )
                }
            }
        }
    }
}