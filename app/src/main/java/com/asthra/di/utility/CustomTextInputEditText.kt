package com.asthra.di.utility

import com.google.android.material.textfield.TextInputEditText
import android.content.Context
import android.util.AttributeSet
import androidx.annotation.Nullable
import com.asthra.R
import com.google.android.material.textfield.TextInputLayout

class CustomTextInputEditText(context: Context) : TextInputLayout(context) {

    private var editText: TextInputEditText? = null

    fun CustomTextInputEditText(context: Context) {

    }

    fun CustomTextInputEditText(context: Context, @Nullable attrs: AttributeSet?) {
        //this(context, attrs, R.style.Widget_MaterialComponents_TextInputLayout_OutlinedBox)
    }

    fun CustomTextInputEditText(
        context: Context,
        @Nullable attrs: AttributeSet,
        defStyleAttr: Int
    ) {
        // super(context, attrs, defStyleAttr)
        init(context, attrs, defStyleAttr)
    }

    private fun init(context: Context, attrs: AttributeSet, defStyleAttr: Int) {
        //removeAllViews();
        setWillNotDraw(false)
        editText = TextInputEditText(getContext())
        createEditBox(editText!!)
    }

    private fun createEditBox(editText: TextInputEditText) {
        val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        editText.setPadding(0, 10, 0, 0)
        editText.layoutParams = layoutParams
        addView(editText)
    }
}