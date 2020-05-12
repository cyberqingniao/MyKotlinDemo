package com.yjp.easytools.databing.viewadapter.edittex

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.databinding.BindingAdapter
import com.yjp.easytools.databing.command.BindingCommand

/**
 * $
 * @author yjp
 * @date 2020/3/26 15:26
 */
object ViewAdapter {

    @BindingAdapter(value = ["requestFocus"], requireAll = false)
    fun requestFocusCommand(editText: EditText, needRequestFocus: Boolean) {
        if (needRequestFocus) {
            editText.setSelection(editText.text.length)
            editText.requestFocus()
            var imm =
                editText.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
        }
        editText.isFocusableInTouchMode = needRequestFocus
    }

    @BindingAdapter(value = ["textChanged"], requireAll = false)
    fun addTextChangedListener(editText: EditText, textChanged: BindingCommand<String>?) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
            }

            override fun onTextChanged(
                text: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
                textChanged?.execute(text.toString())
            }

            override fun afterTextChanged(editable: Editable) {
            }
        })
    }

    @BindingAdapter(value = ["afterTextChangedCommand"], requireAll = false)
    fun afterTextChangedListener(editText: EditText, textChanged: BindingCommand<String>?) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
            }

            override fun onTextChanged(
                text: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
            }

            override fun afterTextChanged(editable: Editable) {
                textChanged?.execute(editable.toString())
            }
        })
    }
}