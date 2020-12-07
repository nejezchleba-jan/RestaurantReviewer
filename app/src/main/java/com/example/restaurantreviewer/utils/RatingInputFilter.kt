package com.example.restaurantreviewer.utils

import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.widget.EditText


class RatingInputFilter {

    fun setTextWatcher(view: EditText) {
        view.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                val input = s.toString()
                var value = 0
                try {
                    if (input.isEmpty() || input.toInt() < 0) {
                        value = 0
                        view.setText(value.toString())
                        view.setSelection(value.toString().length)
                    }
                    else if (input.length > 3 || input.toInt() > 100) {
                        value = 100
                        view.setText(value.toString())
                        view.setSelection(value.toString().length)
                    }

                } catch (ext: Exception) {
                    view.setText(value.toString())
                    view.setSelection(value.toString().length)
                }
            }
        })
    }
}