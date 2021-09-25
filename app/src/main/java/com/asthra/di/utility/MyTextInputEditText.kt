package com.asthra.di.utility

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import com.asthra.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class MyTextInputEditText : TextInputEditText {

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context) : super(context)

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        getTextInputLayout()?.setEndIconActivated(focused)
        getTextInputLayout()?.boxBackgroundColor = resources.getColor(R.color.colorWhite)
    }

    //copied from TextInputEditText (why this is private?)
    private fun getTextInputLayout(): TextInputLayout? {
        var parent = parent
        while (parent is View) {
            if (parent is TextInputLayout) {
                return parent
            }
            parent = parent.getParent()
        }
        return null
    }
}