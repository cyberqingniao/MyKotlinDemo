package com.yjp.easytools.databing.viewadapter.radiobutton

import android.widget.CompoundButton
import android.widget.RadioButton
import androidx.databinding.BindingAdapter
import com.yjp.easytools.databing.command.BindingCommand

/**
 * $
 * @author yjp
 * @date 2020/5/12 14:19
 */
class ViewAdapter {
    @BindingAdapter(value = ["onCheckChangedCommand"], requireAll = false)
    fun setCheckedChanged(radioButton: RadioButton, bindingCommand: BindingCommand<Boolean>) {
        radioButton.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener() { buttonView: CompoundButton?, isChecked: Boolean ->
            run {
                bindingCommand.execute(isChecked)
            }
        })
    }
}