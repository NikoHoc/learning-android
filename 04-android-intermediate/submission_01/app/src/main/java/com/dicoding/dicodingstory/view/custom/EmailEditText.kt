package com.dicoding.dicodingstory.view.custom

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import android.view.View
import androidx.appcompat.widget.AppCompatEditText

class EmailEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateEmail(s)
            }

            override fun afterTextChanged(s: Editable?) {
                validateEmail(s)
            }
        })
    }

    private fun validateEmail(s: CharSequence?) {
        if (s.isNullOrEmpty() || !Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
            setError("Format email tidak valid", null)
        } else if (!s.endsWith("@gmail.com")) {
            setError("Email harus diakhiri dengan @gmail.com", null)
        } else {
            error = null
        }
    }
}