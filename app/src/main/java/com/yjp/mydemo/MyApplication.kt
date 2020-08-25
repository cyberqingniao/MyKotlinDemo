package com.yjp.mydemo

import android.annotation.SuppressLint
import android.util.Log
import android.view.Gravity
import com.hst.xzqx.QxInterface
import com.hst.xzqx.play.QxSdkManager
import com.yjp.easytools.base.BaseApplication
import com.yjp.easytools.utils.ToastUtils
import java.security.SecureRandom
import javax.net.ssl.*

/**
 * $
 *
 * @author yjp
 * @date 2020-05-08 23:39
 */
class MyApplication : BaseApplication() {

    companion object {
        public lateinit var context: MyApplication
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        ToastUtils.setGravity(Gravity.CENTER, 0, 0)
        QxSdkManager.getInstance().init(true, object : QxInterface {
            override fun onError(p0: String?) {
                Log.e("yjp", "初始化GxSdkManager$p0")
            }

            override fun initSuccess() {
                Log.i("yjp", "初始化GxSdkManager成功")
            }

        })
    }
}