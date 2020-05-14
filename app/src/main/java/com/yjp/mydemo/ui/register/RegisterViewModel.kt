package com.yjp.mydemo.ui.register

import android.app.Application
import androidx.databinding.ObservableField
import com.yjp.easytools.base.BaseViewModel
import com.yjp.easytools.databing.command.BindingAction
import com.yjp.easytools.databing.command.BindingCommand
import com.yjp.easytools.utils.RxTimerUtil
import com.yjp.mydemo.ui.main.MainActivity

/**
 *
 * @author yjp
 * @date 2020-05-11 23:46:20
 */
class RegisterViewModel(application: Application) : BaseViewModel(application) {
    val phone = ObservableField<String>("")
    val code = ObservableField<String>("")
    val password = ObservableField<String>("")
    val password1 = ObservableField<String>("")
    val codeTxt = ObservableField<String>("验证码")
    val rxTimerUtil = RxTimerUtil()
    var oldTime = 60L

    val getCodeOncClickCommand = BindingCommand<Any>(object : BindingAction {
        override fun call() {
            if (rxTimerUtil.isDispose()) {
                codeTxt.set("${oldTime}s")
                rxTimerUtil.interval(1000, object : RxTimerUtil.IRxNext {
                    override fun next(number: Long) {
                        oldTime--
                        if (oldTime > 0) {
                            codeTxt.set("${oldTime}s")
                        } else {
                            codeTxt.set("重新获取")
                        }
                    }
                })
            }
        }
    })
    val registerOnClickCommand = BindingCommand<Any>(object : BindingAction {
        override fun call() {
            startActivity(MainActivity::class.java)
        }

    })
}