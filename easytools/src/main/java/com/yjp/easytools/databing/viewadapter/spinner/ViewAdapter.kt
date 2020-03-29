package com.yjp.easytools.databing.viewadapter.spinner

import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.BindingAdapter
import com.yjp.easytools.databing.command.BindingCommand

/**
 * $
 *
 * @author yjp
 * @date 2020-03-29 14:18
 */
object ViewAdapter {

    @BindingAdapter(
        value = ["itemDatas", "valueReply", "resource", "dropDownResource", "onItemSelectedCommand"],
        requireAll = false
    )
    fun onItemSelectedCommand(
        spinner: Spinner,
        itemDatas: List<IKeyAndValue>,
        valueReply: String,
        resource: Int,
        dropDownResource: Int,
        bindingCommand: BindingCommand<IKeyAndValue>
    ) {
        var lists = mutableListOf<String>()
        for (iKeyAndValue in itemDatas) {
            lists.add(iKeyAndValue.getKey())
        }
        var resId = if (resource == 0) {
            android.R.layout.simple_spinner_item
        } else {
            resource
        }
        var dropResId = if (dropDownResource == 0) {
            android.R.layout.simple_spinner_dropdown_item
        } else {
            dropDownResource
        }
        var adapter = ArrayAdapter<String>(spinner.context, resId, lists)
        adapter.setDropDownViewResource(dropResId)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                var iKAV = itemDatas[position]
                bindingCommand.execute(iKAV)
            }

        }
        if (!TextUtils.isEmpty(valueReply)) {
            for (i in itemDatas.indices) {
                var iKAV = itemDatas[i]
                if (valueReply == iKAV.getValue()) {
                    spinner.setSelection(i)
                    return
                }
            }
        }
    }
}