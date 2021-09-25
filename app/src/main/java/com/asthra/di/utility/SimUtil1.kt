package com.asthra.di.utility

import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

class SimUtil1 {

    fun sendSMS(
        ctx: Context,
        simID: Int,
        toNum: String?,
        centerNum: String?,
        smsText: String?,
        sentIntent: PendingIntent?,
        deliveryIntent: PendingIntent?
    ): Boolean {
        val name: String
        try {
            name = if (simID == 0) {
                "isms"
                // for model : "Philips T939" name = "isms0"
            } else if (simID == 1) {
                "isms2"
            } else {
                throw Exception("can not get service which for sim '$simID', only 0,1 accepted as values")
            }
            var method = Class.forName("android.os.ServiceManager").getDeclaredMethod(
                "getService",
                String::class.java
            )
            method.isAccessible = true
            val param = method.invoke(null, name)
            method = Class.forName("com.android.internal.telephony.ISms\$Stub").getDeclaredMethod(
                "asInterface",
                IBinder::class.java
            )
            method.isAccessible = true
            val stubObj = method.invoke(null, param)
            if (Build.VERSION.SDK_INT < 18) {
                method = stubObj.javaClass.getMethod(
                    "sendText",
                    String::class.java,
                    String::class.java,
                    String::class.java,
                    PendingIntent::class.java,
                    PendingIntent::class.java
                )
                method.invoke(stubObj, toNum, centerNum, smsText, sentIntent, deliveryIntent)
            } else {
                method = stubObj.javaClass.getMethod(
                    "sendText",
                    String::class.java,
                    String::class.java,
                    String::class.java,
                    String::class.java,
                    PendingIntent::class.java,
                    PendingIntent::class.java
                )
                method.invoke(
                    stubObj,
                    ctx.packageName,
                    toNum,
                    centerNum,
                    smsText,
                    sentIntent,
                    deliveryIntent
                )
            }
            return true
        } catch (e: ClassNotFoundException) {
            Log.e("apipas", "ClassNotFoundException:" + e.message)
        } catch (e: NoSuchMethodException) {
            Log.e("apipas", "NoSuchMethodException:" + e.message)
        } catch (e: InvocationTargetException) {
            Log.e("apipas", "InvocationTargetException:" + e.message)
        } catch (e: IllegalAccessException) {
            Log.e("apipas", "IllegalAccessException:" + e.message)
        } catch (e: Exception) {
            Log.e("apipas", "Exception:" + e.message)
        }
        return false
    }


    fun sendMultipartTextSMS(
        ctx: Context,
        simID: Int,
        toNum: String?,
        centerNum: String?,
        smsTextlist: ArrayList<String?>?,
        sentIntentList: ArrayList<PendingIntent?>?,
        deliveryIntentList: ArrayList<PendingIntent?>?
    ): Boolean {
        val name: String
        try {
            name = if (simID == 0) {
                "isms"
                // for model : "Philips T939" name = "isms0"
            } else if (simID == 1) {
                "isms2"
            } else {
                throw Exception("can not get service which for sim '$simID', only 0,1 accepted as values")
            }
            var method = Class.forName("android.os.ServiceManager").getDeclaredMethod(
                "getService",
                String::class.java
            )
            method.isAccessible = true
            val param = method.invoke(null, name)
            method = Class.forName("com.android.internal.telephony.ISms\$Stub").getDeclaredMethod(
                "asInterface",
                IBinder::class.java
            )
            method.isAccessible = true
            val stubObj = method.invoke(null, param)
            if (Build.VERSION.SDK_INT < 18) {
                method = stubObj.javaClass.getMethod(
                    "sendMultipartText",
                    String::class.java,
                    String::class.java,
                    MutableList::class.java,
                    MutableList::class.java,
                    MutableList::class.java
                )
                method.invoke(
                    stubObj,
                    toNum,
                    centerNum,
                    smsTextlist,
                    sentIntentList,
                    deliveryIntentList
                )
            } else {
                method = stubObj.javaClass.getMethod(
                    "sendMultipartText",
                    String::class.java,
                    String::class.java,
                    String::class.java,
                    MutableList::class.java,
                    MutableList::class.java,
                    MutableList::class.java
                )
                method.invoke(
                    stubObj,
                    ctx.packageName,
                    toNum,
                    centerNum,
                    smsTextlist,
                    sentIntentList,
                    deliveryIntentList
                )
            }
            return true
        } catch (e: ClassNotFoundException) {
            Log.e("apipas", "ClassNotFoundException:" + e.message)
        } catch (e: NoSuchMethodException) {
            Log.e("apipas", "NoSuchMethodException:" + e.message)
        } catch (e: InvocationTargetException) {
            Log.e("apipas", "InvocationTargetException:" + e.message)
        } catch (e: IllegalAccessException) {
            Log.e("apipas", "IllegalAccessException:" + e.message)
        } catch (e: Exception) {
            Log.e("apipas", "Exception:" + e.message)
        }
        return false
    }

}