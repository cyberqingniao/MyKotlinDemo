package com.yjp.mydemo.ui.forgetPassword

import android.app.Application
import androidx.databinding.ObservableField
import com.yjp.easytools.base.BaseViewModel
import com.yjp.easytools.databing.command.BindingAction
import com.yjp.easytools.databing.command.BindingCommand
import com.yjp.easytools.utils.RxTimerUtils

/**
 *
 * @author yjp
 * @date 2020-05-11 23:42:09
 */
class ForgetPasswordViewModel(application: Application) : BaseViewModel(application) {
    val phone = ObservableField<String>("")
    val code = ObservableField<String>("")
    val password = ObservableField<String>("")
    val password1 = ObservableField<String>("")
    val codeTxt = ObservableField<String>("验证码")
    val rxTimerUtil = RxTimerUtils()
    var oldTime = 60L

    val getCodeOncClickCommand = BindingCommand<Any>(object : BindingAction {
        override fun call() {
            if (rxTimerUtil.isDispose()) {
                codeTxt.set("${oldTime}s")
                rxTimerUtil.interval(1000, object : RxTimerUtils.IRxNext {
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
    val reportOnClickCommand = BindingCommand<Any>(object : BindingAction {
        override fun call() {
            finish()
        }

    })
}