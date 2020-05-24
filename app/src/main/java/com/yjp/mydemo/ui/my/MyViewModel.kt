package com.yjp.mydemo.ui.my

import android.app.Application
import com.yjp.easytools.base.BaseViewModel
import com.yjp.easytools.databing.command.BindingAction
import com.yjp.easytools.databing.command.BindingCommand
import com.yjp.mydemo.ui.lottery.LotteryActivity

/**
 *
 * @author yjp
 * @date 2020-05-11 23:41:52
 */
class MyViewModel(application: Application) : BaseViewModel(application) {

    val lotteryOnClickCommand = BindingCommand<Any>(object : BindingAction {
        override fun call() {
            startActivity(LotteryActivity::class.java)
        }

    })
}