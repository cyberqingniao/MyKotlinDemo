package com.yjp.mydemo.ui.message

import android.app.Application
import androidx.databinding.ObservableArrayList
import com.yjp.easytools.base.BaseViewModel
import com.yjp.easytools.databing.command.BindingAction
import com.yjp.easytools.databing.command.BindingCommand
import com.yjp.mydemo.BR
import com.yjp.mydemo.R
import me.tatarka.bindingcollectionadapter2.ItemBinding

/**
 *
 * @author yjp
 * @date 2020-05-11 23:41:52
 */
class MessageViewModel(application: Application) : BaseViewModel(application) {

    val itemBinding =
        ItemBinding.of<MessageItemViewModel>(BR.msgItemViewModel, R.layout.item_message)
    val items = ObservableArrayList<MessageItemViewModel>()

    val menuOnClickCommand = BindingCommand<Any>(object : BindingAction {
        override fun call() {

        }
    })
}