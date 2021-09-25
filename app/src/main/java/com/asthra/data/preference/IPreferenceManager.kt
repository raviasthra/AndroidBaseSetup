package com.asthra.data.preference

import android.content.Context

interface IPreferenceManager {

    fun saveString(key: String?, value: String?)
    fun getString(key: String?): String
    fun saveInt(key: String?, value: Int)
    fun getInt(key: String?): Int

    companion object Factory {
        fun getPrefInstance(context: Context): IPreferenceManager = PreferenceManager(context)
    }
}