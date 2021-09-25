package com.asthra.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.asthra.AsthraApplication
import com.asthra.R
import com.asthra.data.preference.IPreferenceManager
import com.asthra.di.isNetworkAvailable
import com.asthra.di.toast
import com.asthra.di.utility.Pref
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var asthraApplication: AsthraApplication
    private lateinit var preferenceManager: IPreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        asthraApplication = applicationContext as AsthraApplication
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.credentialNavHost) as NavHostFragment
        val navController = navHostFragment.navController
        NavigationUI.setupWithNavController(bottomNavHome, navController)   //  Navigation bar
        setNavElementVisibility(navController)
        setSupportActionBar(tbar)

        navigationView.setupWithNavController(navController)
        navigationView.setNavigationItemSelectedListener(this)

        preferenceManager = IPreferenceManager.getPrefInstance(this)

        menuImg.setOnClickListener {
            drawerLayout.openDrawer(Gravity.LEFT)
        }

        backImg.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setNavElementVisibility(navController: NavController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
//                R.id.homeFragment -> bottomNavHome.show()
//                else -> bottomNavHome.hide()
            }
        }
    }

    fun setToolbarTitle(title: String) {
        toobarTitle.text = title
    }

    override fun onBackPressed() {
        when (findNavController(R.id.credentialNavHost).currentDestination?.id) {
            R.id.loginFragment -> finish()
            R.id.selectUserTypeFragment -> {
                val builder = AlertDialog.Builder(this, R.style.AlertDialogStyle)
                builder.setTitle("Do you want to exit?")
                builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                    dialog.dismiss()
                    finish()
                }

                builder.setNegativeButton(android.R.string.no) { dialog, which ->
                    dialog.dismiss()
                }
                builder.show()
            }
            R.id.employeeFragment -> {
                val builder = AlertDialog.Builder(this, R.style.AlertDialogStyle)
                builder.setTitle("Do you want to exit?")
                builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                    dialog.dismiss()
                    finish()
                }

                builder.setNegativeButton(android.R.string.no) { dialog, which ->
                    dialog.dismiss()
                }
                builder.show()

            }
            else -> findNavController(R.id.credentialNavHost).navigateUp()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (isNetworkAvailable(this)) {
            when (item.itemId) {
                R.id.dashboard -> {
                    findNavController(R.id.credentialNavHost).navigate(R.id.dashboardFragment)
                }
                R.id.fuel -> {
                    findNavController(R.id.credentialNavHost).navigate(R.id.fuelFragment)
                }
                R.id.settings -> {
                    findNavController(R.id.credentialNavHost).navigate(R.id.settingFragment)
                }
                R.id.redeemList -> {
                    findNavController(R.id.credentialNavHost).navigate(R.id.redeemHistory)
                }
                R.id.logout -> {
                    val builder = AlertDialog.Builder(this, R.style.AlertDialogStyle)
                    builder.setTitle("Logout")
                    builder.setMessage("Are you sure want to logout?")
                    builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                        preferenceManager.saveInt(Pref.USER_ID, 0)
                        preferenceManager.saveString(Pref.USER_EMAIL, "")
                        preferenceManager.saveString(Pref.USER_NAME, "")
                        dialog.dismiss()
                        findNavController(R.id.credentialNavHost).navigate(R.id.loginFragment)
                    }

                    builder.setNegativeButton(android.R.string.no) { dialog, which ->
                        dialog.dismiss()
                    }
                    builder.show()

                }

                else -> {
                    toast("Coming soon")
                }
            }
        } else {
            toast(getString(R.string.txt_please_check_internet))
        }
        item.isCheckable = true
        drawerLayout.closeDrawers()
        return true
    }
}