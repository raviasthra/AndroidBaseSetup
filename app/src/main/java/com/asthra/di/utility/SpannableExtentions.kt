package com.asthra.di.utility

import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import androidx.core.text.inSpans
import com.asthra.R
import com.asthra.di.getCompatColor
import com.asthra.di.getCompatSize
import com.asthra.di.getCompatTypefaceSpan

fun TextView.applySpanPo(stringPrefix: String, stringSuffix: String, color: Int) {
    text = buildSpannedString {
        inSpans {
            ForegroundColorSpan(getCompatColor(R.color.colorBlack))
        }
        inSpans {
            append(stringPrefix)
        }.inSpans(
            ForegroundColorSpan(getCompatColor(color))
        ) {
            bold { append(stringSuffix) }
        }
    }
}

fun TextView.applySpanPo(
    stringPrefix: String,
    string: String,
    stringSuffix: String,
    font: Int
) {
    text = buildSpannedString {
        inSpans(
            ForegroundColorSpan(getCompatColor(R.color.colorBlack)),
            context.getCompatTypefaceSpan(font)
        ) {
            append(stringPrefix)
        }.inSpans(
            append(string),
            /*context.getCompatTypefaceSpan(R.font.poppins_regular),*/
            AbsoluteSizeSpan(getCompatSize(R.dimen.size_12_dp))
        ) {

        }.inSpans(
            ForegroundColorSpan(getCompatColor(R.color.colorBlack)),
            context.getCompatTypefaceSpan(font)
        ) {
            bold { append(stringSuffix) }
        }
    }
}

fun TextView.applyBoldSpan(stringPrefix: String, stringSuffix: String, font: Int) {
    text = buildSpannedString {
        inSpans(
            ForegroundColorSpan(getCompatColor(R.color.colorBlack)),
            context.getCompatTypefaceSpan(font)
        ) {
            append(stringPrefix)
        }.inSpans {
            append(stringSuffix)
        }
    }
}