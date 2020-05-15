package com.yjp.easytools.utils

import android.app.Activity
import androidx.fragment.app.Fragment
import java.util.*
import kotlin.system.exitProcess

/**
 * Activity管理工具$
 * @author yjp
 * @date 2020/4/1 10:24
 */
class ActivityManager private constructor() {
    companion object {
        var activityStack: Stack<Activity>? = null
            private set
        var fragmentStack: Stack<Fragment>? = null
            private set
        val instance: ActivityManager by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { ActivityManager() }

    }

    /**
     * 是否有activity
     */
    var isActivity: Boolean = activityStack?.isEmpty() ?: false

    /**
     * 添加Activity到堆栈
     */
    fun addActivity(activity: Activity) {
        if (activityStack == null) {
            activityStack = Stack()
        }
        activityStack?.add(activity)
    }

    /**
     * 移除指定的Activity
     */
    fun removeActivity(activity: Activity) {
        activityStack?.remove(activity)
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    fun currentActivity(): Activity {
        return activityStack!!.lastElement()
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    fun finishActivity() {
        finishActivity(activityStack?.lastElement()!!)
    }

    /**
     * 结束指定类名的Activity
     */
    fun finishActivity(clazz: Class<*>) {
        for (a in activityStack!!) {
            if (a.javaClass == clazz) {
                finishActivity(a)
                break
            }
        }
    }

    /**
     * 结束所有Activity
     */
    fun finishAllActivity() {
        for (a in activityStack!!) {
            finishActivity(a)
        }
        activityStack?.clear()
    }

    /**
     * 结束指定的Activity
     */
    fun finishActivity(activity: Activity) {
        if (!activity.isFinishing) {
            activity.finish()
        }
    }

    /**
     * 获取指定的Activity
     *
     * @author kymjs
     */
    fun getActivity(clazz: Class<*>): Activity? {
        if (activityStack != null) {
            for (a in activityStack!!) {
                if (a.javaClass == clazz) {
                    return a
                }
            }
        }
        return null
    }

    /**
     * 添加Fragment到堆栈
     */
    fun addFragment(fragment: Fragment?) {
        if (fragmentStack == null) {
            fragmentStack = Stack<Fragment>()
        }
        fragmentStack!!.add(fragment)
    }

    /**
     * 移除指定的Fragment
     */
    fun removeFragment(fragment: Fragment?) {
        if (fragment != null) {
            fragmentStack!!.remove(fragment)
        }
    }


    /**
     * 是否有Fragment
     */
    fun isFragment(): Boolean {
        return if (fragmentStack != null) {
            fragmentStack!!.isEmpty()
        } else false
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    fun currentFragment(): Fragment? {
        return if (fragmentStack != null) {
            fragmentStack!!.lastElement()
        } else null
    }


    /**
     * 退出应用程序
     */
    fun AppExit() {
        try {
            finishAllActivity()
            // 杀死该应用进程
//          android.os.Process.killProcess(android.os.Process.myPid());
//            调用 System.exit(n) 实际上等效于调用：
//            Runtime.getRuntime().exit(n)
//            finish()是Activity的类方法，仅仅针对Activity，当调用finish()时，只是将活动推向后台，并没有立即释放内存，活动的资源并没有被清理；当调用System.exit(0)时，退出当前Activity并释放资源（内存），但是该方法不可以结束整个App如有多个Activty或者有其他组件service等不会结束。
//            其实android的机制决定了用户无法完全退出应用，当你的application最长时间没有被用过的时候，android自身会决定将application关闭了。
            exitProcess(0)
        } catch (e: Exception) {
            activityStack?.clear()
            e.printStackTrace()
        }
    }
}