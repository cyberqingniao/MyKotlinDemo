package com.yjp.easytools.base

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.yjp.easytools.utils.ActivityManager
import com.yjp.easytools.utils.Utils

/**
 * $
 * @author yjp
 * @date 2020/4/1 16:42
 */
class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        setApplication(this)
    }

    companion object {
        fun setApplication(application: Application) {
            //初始化工具
            Utils.init(application)
            //注册监听每个activity的生命周期,便于堆栈式管理
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
}