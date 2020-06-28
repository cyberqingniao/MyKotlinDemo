package com.yjp.mydemo.ui.splash

import android.app.Application
import androidx.databinding.ObservableField
import com.yjp.easytools.base.BaseViewModel
import com.yjp.easytools.bus.event.SingleLiveEvent
import com.yjp.easytools.databing.command.BindingAction
import com.yjp.easytools.databing.command.BindingCommand
import com.yjp.easytools.utils.RxTimerUtil
import com.yjp.mydemo.ui.login.LoginActivity

/**
 *
 * @author yjp
 * @date 2020-05-11 23:41:52
 */
class SplashViewModel(application: Application) : BaseViewModel(application) {

    val timer = ObservableField<String>("")

    private val rxTimer = RxTimerUtil()
    private var time = 3

    fun openTimer() {
        rxTimer.interval(1000, object : RxTimerUtil.IRxNext {
            override fun next(number: Long) {
                time--
                if (time <= 0) {
                    rxTimer.cancel()
                    startActivity(LoginActivity::class.java)
                    finish()
                }
            }

        })
    }

    override fun onStop() {
        super.onStop()
        rxTimer.cancel()
    }
}