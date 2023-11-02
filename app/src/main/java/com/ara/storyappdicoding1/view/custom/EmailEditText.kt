package com.ara.storyappdicoding1.view.custom

import android.content.Context
import android.util.AttributeSet
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import com.ara.storyappdicoding1.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class EmailEditText : TextInputLayout, View.OnTouchListener {

    private lateinit var editText: TextInputEditText

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onFinishInflate() {
        super.onFinishInflate()
        val parentLayout = getChildAt(0) as ViewGroup

        editText = parentLayout.getChildAt(0) as TextInputEditText

        editText.doOnTextChanged { text, _, _, _ ->
            if (text != null) {
                if (!Patterns.EMAIL_ADDRESS.matcher(text.trim())
                        .matches()) {
                    this.error = context.getString(R.string.email_invalid)
                } else {
                    this.error = null
                }
            }
        }
    }

    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        return false
    }
}
