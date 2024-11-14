package com.dicoding.dicodingstory.view.custom

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText

class PasswordEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // nothing
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                validatePassword(s)
            }
            override fun afterTextChanged(s: Editable?) {
                validatePassword(s)
            }
        })
    }

    private fun validatePassword(s: CharSequence?) {
        if (s.toString().length < 8) {
            setError("Password tidak boleh kurang dari 8 karakter", null)
        } else {
            error = null
        }
    }
}