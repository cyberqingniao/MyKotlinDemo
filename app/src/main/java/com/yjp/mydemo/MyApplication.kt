package com.yjp.mydemo

import com.yjp.easytools.base.BaseApplication

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
    }
}