package com.yjp.easytools.databing.viewadapter.switch

import android.widget.Switch
import androidx.databinding.BindingAdapter
import com.yjp.easytools.databing.command.BindingCommand

/**
 * $
 *
 * @author yjp
 * @date 2020-03-29 15:19
 */
object ViewAdapter {
    @BindingAdapter("switchState")
    fun setSwitchState(mSwitch: Switch, isChecked: Boolean) {
        mSwitch.isChecked = isChecked
    }

    @BindingAdapter("onCheckedChangeCommand")
    fun onCheckedChangeCommand(mSwitch: Switch, changeListener: BindingCommand<Boolean>) {
        mSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            {
                changeListener.execute(isChecked)
            }
        }
    }
}