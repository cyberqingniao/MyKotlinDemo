package com.yjp.mydemo

import android.view.Gravity
import com.yjp.easytools.base.BaseApplication
import com.yjp.easytools.utils.ToastUtils

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
        ToastUtils.setGravity(Gravity.CENTER,0,0)
    }
}