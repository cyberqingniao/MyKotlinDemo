package com.yjp.easytools.databing.viewadapter.radiogroup

import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.databinding.BindingAdapter
import com.yjp.easytools.databing.command.BindingCommand

/**
 * $
 * @author yjp
 * @date 2020/3/26 15:49
 */
object ViewAdapter {
    @BindingAdapter(value = ["onCheckedChangedCommand"], requireAll = false)
    fun onCheckedChangedCommand(radioGroup: RadioGroup, bindingCommand: BindingCommand<String>) {
        radioGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener() { group, checkedId ->
            {
                var radioButton = group.findViewById<RadioButton>(checkedId)
                if (radioButton != null && radioButton.text != null) {
                    bindingCommand.execute(radioButton.text.toString())
                }
            }
        })
    }
}