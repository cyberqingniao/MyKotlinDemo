package com.yjp.easytools.databing.viewadapter.checkbox

import android.widget.CheckBox
import android.widget.CompoundButton
import androidx.databinding.BindingAdapter
import com.yjp.easytools.databing.command.BindingCommand

/**
 * $
 * @author yjp
 * @date 2020/3/26 15:16
 */
object ViewAdapter {
    @BindingAdapter(value = ["onCheckChangedCommand"], requireAll = false)
    fun setCheckedChanged(checkBox: CheckBox, bindingCommand: BindingCommand<Boolean>) {
        checkBox.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener() { buttonView: CompoundButton?, isChecked: Boolean ->
            {
                bindingCommand.execute(isChecked)
            }
        })
    }
}