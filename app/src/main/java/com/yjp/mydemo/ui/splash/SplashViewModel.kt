package com.yjp.mydemo.ui.splash

import android.app.Application
import androidx.databinding.ObservableField
import com.yjp.easytools.base.BaseViewModel
import com.yjp.easytools.utils.RxTimerUtils
import com.yjp.mydemo.ui.login.LoginActivity

/**
 *
 * @author yjp
 * @date 2020-05-11 23:41:52
 */
class SplashViewModel(application: Application) : BaseViewModel(application) {

    val timer = ObservableField<String>("")

    private val rxTimer = RxTimerUtils()
    private var time = 3

    fun openTimer() {
        rxTimer.interval(1000, object : RxTimerUtils.IRxNext {
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