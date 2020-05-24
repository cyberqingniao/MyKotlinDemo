package com.yjp.mydemo.ui.bindPhone

import android.app.Application
import androidx.databinding.ObservableField
import com.yjp.easytools.base.BaseViewModel
import com.yjp.easytools.databing.command.BindingAction
import com.yjp.easytools.databing.command.BindingCommand
import com.yjp.mydemo.ui.main.MainActivity

/**
 *
 * @author yjp
 * @date 2020-05-11 23:48:27
 */
class BindPhoneViewModel(application: Application) : BaseViewModel(application) {
    val phone = ObservableField<String>("")
    val code = ObservableField<String>("")
    val codeTxt = ObservableField<String>("验证码")

    val getCodeOncClickCommand = BindingCommand<Any>(object : BindingAction {
        override fun call() {

        }
    })
    val reportOnClickCommand = BindingCommand<Any>(object : BindingAction {
        override fun call() {
            startActivity(MainActivity::class.java)
        }
    })
}