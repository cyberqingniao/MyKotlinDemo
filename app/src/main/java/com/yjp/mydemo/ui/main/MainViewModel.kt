package com.yjp.mydemo.ui.main

import android.app.Application
import com.yjp.easytools.base.BaseViewModel
import com.yjp.easytools.bus.event.SingleLiveEvent
import com.yjp.easytools.databing.command.BindingAction
import com.yjp.easytools.databing.command.BindingCommand
import com.yjp.easytools.databing.command.BindingConsumer
import com.yjp.easytools.utils.Utils
import com.yjp.mydemo.R

/**
 *
 * @author yjp
 * @date 2020-05-11 23:41:52
 */
class MainViewModel(application: Application) : BaseViewModel(application) {

    val ui = SingleLiveEvent<Int>()

    val pageChangeCommand = BindingCommand<String>(object : BindingConsumer<String?> {

        override fun call(t: String?) {
            if (Utils.isEmpty(t)) {
                return
            }
            ui.value = when (t) {
                Utils.getString(R.string.page_home) -> {
                    0
                }
                Utils.getString(R.string.page_type) -> {
                    1
                }
                Utils.getString(R.string.page_message) -> {
                    2
                }
                Utils.getString(R.string.page_my) -> {
                    3
                }
                else -> 0
            }
        }
    })
}