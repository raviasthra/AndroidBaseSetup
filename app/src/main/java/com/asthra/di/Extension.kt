package com.asthra.di

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Typeface
import android.media.MediaScannerConnection
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.MediaStore
import android.text.TextPaint
import android.text.style.TypefaceSpan
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.FontRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.asthra.R
import com.asthra.di.utility.ImageConstants.CAMERA
import com.asthra.di.utility.ImageConstants.GALLERY
import com.asthra.di.utility.ImageConstants.IMAGE_DIRECTORY
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

fun Context.getCompatTypefaceSpan(@FontRes fontRes: Int): TypefaceSpan =
    getCompatFont(fontRes).let {
        object : TypefaceSpan(String()) {

            override fun updateDrawState(ds: TextPaint) {
                applyCustomTypeFace(ds, it)
            }

            override fun updateMeasureState(paint: TextPaint) {
                applyCustomTypeFace(paint, it)
            }

            fun applyCustomTypeFace(paint: Paint, tf: Typeface) {
                val fake = paint.typeface?.style ?: 0 and tf.style.inv()
                if (fake and Typeface.BOLD != 0) {
                    paint.isFakeBoldText = true
                }
                if (fake and Typeface.NORMAL != 0) {
                    paint.textSkewX = -0.25f
                }
                paint.typeface = tf
            }

        }
    }

fun Context.getCompatFont(@FontRes fontRes: Int): Typeface =
    ResourcesCompat.getFont(this, fontRes) ?: Typeface.DEFAULT

fun View.getCompatSize(@DimenRes dimenRes: Int): Int =
    resources.getDimension(dimenRes).toInt()

fun View.getCompatColor(@ColorRes colorRes: Int): Int =
    ContextCompat.getColor(this.context, colorRes)

fun Context.toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

fun Context.toast(msg: Int) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

fun ProgressBar.blockUI(activity: Activity, relativeLay: RelativeLayout) {
    //this.visibility = View.VISIBLE
    relativeLay.visibility = View.VISIBLE
    activity.window.setFlags(
        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
    )
}

fun ProgressBar.unBlockUI(activity: Activity, relativeLay: RelativeLayout) {
    relativeLay.visibility = View.GONE
    activity.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
}

fun Context.setCircularView(imageView: ImageView, pic: Int?) {
    Glide
        .with(this)
        .load(pic)
        .circleCrop()
        .into(imageView)
}

fun Context.setCircularView(imageView: ImageView, pic: String?) {
    Glide
        .with(this)
        .load(pic)
        .circleCrop()
        .placeholder(R.drawable.icon_user)
        .into(imageView)
}

fun Activity.initToolbar(tbar: Toolbar, name: String, navController: NavController) {
    (this as AppCompatActivity).setSupportActionBar(tbar)
    this.supportActionBar?.apply {
        title = name
        setDisplayHomeAsUpEnabled(true)
    }
    tbar.setNavigationOnClickListener {
        navController.navigateUp()
    }
}

fun BottomNavigationView.show() {
    this.visibility = View.VISIBLE
}

fun BottomNavigationView.hide() {
    this.visibility = View.GONE
}

fun Fragment.choosePhotoFromGallery() {
    startActivityForResult(
        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI),
        GALLERY
    )
}

fun Fragment.takePhotoFromCamera() {
    startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE), CAMERA)
}

fun Fragment.showDialogToPick() {
    val pickDialog = AlertDialog.Builder(this.context)
    pickDialog.setTitle("Choose an action")
    val pictureSelection = arrayOf("Select image from gallery", "Take photo on camera")
    pickDialog.setItems(pictureSelection) { dialog, which ->
        when (which) {
            0 -> choosePhotoFromGallery()
            1 -> takePhotoFromCamera()
        }
    }
    pickDialog.show()
}

fun Context.savePic(bitmap: Bitmap, msg: String): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream)
    //  val directory = File(Environment.getExternalStorageState().toString() + IMAGE_DIRECTORY)  // getExternalStorageDirectory()
    val directory = File(this.filesDir, IMAGE_DIRECTORY)
    if (!directory.exists()) directory.mkdirs()
    try {
        val file = File(directory, ((Calendar.getInstance().timeInMillis).toString() + ".jpg"))
        file.createNewFile()
        val fileOutputStream = FileOutputStream(file)
        fileOutputStream.write(byteArrayOutputStream.toByteArray())
        MediaScannerConnection.scanFile(this, arrayOf(file.path), arrayOf("image/jpeg"), null)
        fileOutputStream.close()
        this.toast("$msg  picture has been selected")
        return file.absolutePath
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return ""
}

fun EditText.showDate() {
    val cal = Calendar.getInstance()
    val dateListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
        this.setText("$year-${twoDigitConversion(month + 1)}-${twoDigitConversion(dayOfMonth)}")
    }
    DatePickerDialog(
        context,
        dateListener,
        cal.get(Calendar.YEAR),
        cal.get(Calendar.MONTH),
        cal.get(Calendar.DAY_OF_MONTH)
    ).apply {
        datePicker.minDate = cal.timeInMillis
        show()
    }
}

fun TextView.showDate(texts: String) {
    val cal = Calendar.getInstance()
    val dateListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
        (this as TextView).text =
            "$text - $year-${twoDigitConversion(month + 1)}-${twoDigitConversion(dayOfMonth)}"
        this.tag = "$year-${twoDigitConversion(month + 1)}-${twoDigitConversion(dayOfMonth)}"
    }
    DatePickerDialog(
        context,
        dateListener,
        cal.get(Calendar.YEAR),
        cal.get(Calendar.MONTH),
        cal.get(Calendar.DAY_OF_MONTH)
    ).apply {
        datePicker.minDate = cal.timeInMillis
        show()
    }
}

fun TextView.showDate(text: String, date: String?, fromToDate: Boolean) {
    val cal = Calendar.getInstance()
    val dateListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
        (this as TextView).text =
            "$text - $year-${twoDigitConversion(month + 1)}-${twoDigitConversion(dayOfMonth)}"
        this.tag = "$year-${twoDigitConversion(month + 1)}-${twoDigitConversion(dayOfMonth)}"
    }
    DatePickerDialog(
        context,
        dateListener,
        cal.get(Calendar.YEAR),
        cal.get(Calendar.MONTH),
        cal.get(Calendar.DAY_OF_MONTH)
    ).apply {
        if (fromToDate) {
            datePicker.convertToMilliMin(date)
        } else {
            datePicker.minDate = System.currentTimeMillis()
            datePicker.convertToMilliMax(date)
        }
        show()
    }
}

fun DatePicker.convertToMilliMin(date: String?) {
    try {
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val newDate = sdf.parse(date.toString())
        if (newDate != null)
            this.minDate = newDate.time
        else
            this.minDate = System.currentTimeMillis()
    } catch (e: Exception) {
        e.printStackTrace()
        this.minDate = System.currentTimeMillis()
    }
}

fun DatePicker.convertToMilliMax(date: String?) {
    try {
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val newDate = sdf.parse(date.toString())
        if (newDate != null)
            this.maxDate = newDate.time
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun TextView.showTime(text: String) {
    val cal = Calendar.getInstance()
    val timeListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
        (this as TextView).text =
            "$text - ${twoDigitConversion(hourOfDay)}:${twoDigitConversion(minute)}"
        this.tag = "${twoDigitConversion(hourOfDay)}:${twoDigitConversion(minute)}"
    }
    TimePickerDialog(
        context,
        timeListener,
        cal.get(Calendar.HOUR_OF_DAY),
        cal.get(Calendar.MINUTE),
        true
    ).show()
}

fun String.getTimeInMilli(): Long {
    return try {
        val sdf = SimpleDateFormat("HH:MM", Locale.getDefault())
        sdf.parse(this).time
    } catch (e: Exception) {
        e.printStackTrace()
        0L
    }
}

fun twoDigitConversion(num: Int): String {
    return if (num < 10) "0$num" else "$num"
}

/*
fun LatLng.toLocation() = Location("").apply {
    latitude = latitude
    longitude = longitude
}

fun LatLng.distanceTo(latLng: LatLng) = toLocation().distanceTo(latLng.toLocation())
*/

fun verifyAvailableNetwork(activity: AppCompatActivity): Boolean {
    val connectivityManager =
        activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnected
}

fun isOnline(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (connectivityManager != null) {
        val capabilities =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            } else {
                TODO("VERSION.SDK_INT < M")
            }
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
    }
    return false
}

fun isNetworkAvailable(context: Context?): Boolean {
    if (context == null) return false
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> {
                    return true
                }
            }
        }
    } else {
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
            return true
        }
    }
    return false
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}