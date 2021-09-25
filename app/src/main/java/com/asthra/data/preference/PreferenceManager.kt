package com.asthra.data.preference

import android.content.Context
import android.content.SharedPreferences
import com.asthra.di.utility.Pref.SHARED_PREFERENCES

class PreferenceManager(private val context: Context) : IPreferenceManager {

    val preferenceManager: SharedPreferences =
        context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)

    override fun saveString(key: String?, value: String?) =
        preferenceManager.edit().putString(key, value).apply()


    override fun getString(key: String?): String = preferenceManager.getString(key, "").toString()

    override fun saveInt(key: String?, value: Int) =
        preferenceManager.edit().putInt(key, value).apply()

    override fun getInt(key: String?): Int = preferenceManager.getInt(key,0).toInt()


}