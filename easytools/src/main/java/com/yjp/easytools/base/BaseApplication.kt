package com.yjp.easytools.base

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.yjp.easytools.utils.ActivityManager
import com.yjp.easytools.utils.FileUtil
import com.yjp.easytools.utils.Utils

/**
 * $
 * @author yjp
 * @date 2020/4/1 16:42
 */
open class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        //初始化工具
        Utils.init(this)
        //初始化文件操作工具
        FileUtil.init()
        //生命周期管理
        setLifecycleCallback(this)
    }

    /**
     * 注册监听每个activity的生命周期,便于堆栈式管理
     */
    private fun setLifecycleCallback(application: Application) {
        application.registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity) {
            }

            override fun onActivityStarted(activity: Activity) {
            }

            override fun onActivityDestroyed(activity: Activity) {
                ActivityManager.instance.removeActivity(activity)
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            }

            override fun onActivityStopped(activity: Activity) {
            }

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                ActivityManager.instance.addActivity(activity)
            }

            override fun onActivityResumed(activity: Activity) {
            }

        })
    }
}