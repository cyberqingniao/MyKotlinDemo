package com.yjp.mydemo.ui.setService

import android.app.Application
import androidx.databinding.ObservableField
import com.yjp.easytools.base.BaseViewModel
import com.yjp.easytools.databing.command.BindingAction
import com.yjp.easytools.databing.command.BindingCommand
import com.yjp.easytools.utils.SPUtils
import com.yjp.easytools.utils.Utils
import com.yjp.mydemo.constant.ConstantUtil
import com.yjp.mydemo.constant.SPKey
import com.yjp.mydemo.http.HttpHelp

/**
 *
 * @author yjp
 * @date 2020-05-11 23:41:52
 */
class SetServiceViewModel(application: Application) : BaseViewModel(application) {
    //当前IP与端口
    val mCurrentIPAndPort = ObservableField<String>("")

    //IP
    val ip = ObservableField<String>("")

    //端口
    val port = ObservableField<String>("")

    init {
        ip.set(SPUtils.getString(SPKey.CURRENT_IP, ConstantUtil.IP))
        port.set(SPUtils.getString(SPKey.CURRENT_PORT, ConstantUtil.PORT))
        mCurrentIPAndPort.set("${ip.get()}:${port.get()}")
    }

    val reportOnClickCommand = BindingCommand<Any>(object : BindingAction {
        override fun call() {
            if (!Utils.isEmpty(ip.get())) {
                SPUtils.put(SPKey.CURRENT_IP, ip.get()!!)
            }
            if (!Utils.isEmpty(port.get())) {
                SPUtils.put(SPKey.CURRENT_PORT, port.get()!!)
            }
            HttpHelp.reset()
            finish()
        }

    })
}