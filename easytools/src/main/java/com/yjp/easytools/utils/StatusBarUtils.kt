package com.yjp.easytools.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.readystatesoftware.systembartint.SystemBarTintManager
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

/**
 * 状态栏沉浸式$
 * @author yjp
 * @date 2020/3/31 16:48
 */
object StatusBarUtils {
    //小米
    const val TYPE_MIUI = 0
    //魅族
    const val TYPE_FLYME = 1
    //6.0
    const val TYPE_M = 3

    /**
     * 修改状态栏颜色，支持4.4以上版本
     *
     * @param colorId 颜色
     */
    fun setStatusBarColor(activity: AppCompatActivity, colorId: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = activity.window
            window.statusBarColor = colorId
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(activity)
            val stm = SystemBarTintManager(activity)
            stm.isStatusBarTintEnabled = true
            stm.setStatusBarTintColor(colorId)
        }
    }

    /**
     * 设置状态栏透明
     */
    fun setTranslucentStatus(activity: AppCompatActivity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = activity.window
            val decorView = window.decorView
            //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
            val option =
                (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            decorView.systemUiVisibility = option
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val window = activity.window
            val attributes = window.attributes
            val flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            attributes.flags = attributes.flags or flagTranslucentStatus
            window.attributes = attributes
        }
    }

    /**
     * 代码实现android:fitsSystemWindows
     *
     * @param activity
     */
    fun setRootViewFitsSystemWindows(activity: AppCompatActivity, fitSystemWindows: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val winContent = activity.findViewById<ViewGroup>(android.R.id.content)
            if (winContent.childCount > 0) {
                for (i in 0 until winContent.childCount) {
                    val rootView = winContent.getChildAt(i)
                    if (rootView != null) {
                        if (rootView is LinearLayout ||
                            rootView is RelativeLayout ||
                            rootView is FrameLayout ||
                            rootView is CoordinatorLayout
                        ) {
                            rootView.fitsSystemWindows = fitSystemWindows
                            break
                        }
                    }
                }
            }
        }
    }

    /**
     * 设置状态栏深色浅色切换
     */
    fun setStatusBarDarkTheme(activity: AppCompatActivity, dark: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> setStatusBarFontIconDark(
                    activity,
                    TYPE_M,
                    dark
                )
                OSUtils.isMiui() -> setStatusBarFontIconDark(activity, TYPE_MIUI, dark)
                OSUtils.isFlyme() -> setStatusBarFontIconDark(activity, TYPE_FLYME, dark)
            }
        }
    }

    /**
     * 设置 状态栏深色浅色切换
     */
    fun setStatusBarFontIconDark(
        activity: AppCompatActivity,
        type: Int,
        dark: Boolean
    ): Boolean {
        return when (type) {
            TYPE_MIUI -> setMiuiUI(activity, dark)
            TYPE_FLYME -> setFlymeUI(activity, dark)
            TYPE_M -> setCommonUI(activity, dark)
            else -> setCommonUI(activity, dark)
        }
    }

    //设置6.0 状态栏深色浅色切换
    fun setCommonUI(activity: AppCompatActivity, dark: Boolean): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val decorView = activity.window.decorView
            var vis = decorView.systemUiVisibility
            vis = if (dark) {
                vis or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                vis and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
            if (decorView.systemUiVisibility != vis) {
                decorView.systemUiVisibility = vis
            }
            return true
        }
        return false
    }

    //设置Flyme 状态栏深色浅色切换
    fun setFlymeUI(activity: AppCompatActivity, dark: Boolean): Boolean {
        return try {
            val window = activity.window
            val lp = window.attributes
            val darkFlag =
                WindowManager.LayoutParams::class.java.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
            val meizuFlags =
                WindowManager.LayoutParams::class.java.getDeclaredField("meizuFlags")
            darkFlag.isAccessible = true
            meizuFlags.isAccessible = true
            val bit = darkFlag.getInt(null)
            var value = meizuFlags.getInt(lp)
            value = if (dark) {
                value or bit
            } else {
                value and bit.inv()
            }
            meizuFlags.setInt(lp, value)
            window.attributes = lp
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    //设置MIUI 状态栏深色浅色切换
    fun setMiuiUI(activity: AppCompatActivity, dark: Boolean): Boolean {
        return try {
            val window = activity.window
            val clazz: Class<*> = activity.window.javaClass
            @SuppressLint("PrivateApi") val layoutParams =
                Class.forName("android.view.MiuiWindowManager\$LayoutParams")
            val field =
                layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
            val darkModeFlag = field.getInt(layoutParams)
            val extraFlagField = clazz.getDeclaredMethod(
                "setExtraFlags",
                Int::class.javaPrimitiveType,
                Int::class.javaPrimitiveType
            )
            extraFlagField.isAccessible = true
            if (dark) { //状态栏亮色且黑色字体
                extraFlagField.invoke(window, darkModeFlag, darkModeFlag)
            } else {
                extraFlagField.invoke(window, 0, darkModeFlag)
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    //获取状态栏高度
    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources.getIdentifier(
            "status_bar_height", "dimen", "android"
        )
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    object OSUtils {
        const val ROM_MIUI = "MIUI"
        const val ROM_EMUI = "EMUI"
        const val ROM_FLYME = "FLYME"
        const val ROM_OPPO = "OPPO"
        const val ROM_SMARTISAN = "SMARTISAN"
        const val ROM_VIVO = "VIVO"
        const val ROM_QIKU = "QIKU"

        private const val KEY_VERSION_MIUI = "ro.miui.ui.version.name"
        private const val KEY_VERSION_EMUI = "ro.build.version.emui"
        private const val KEY_VERSION_OPPO = "ro.build.version.opporom"
        private const val KEY_VERSION_SMARTISAN = "ro.smartisan.version"
        private const val KEY_VERSION_VIVO = "ro.vivo.os.version"

        private var sName: String? = null
        private var sVersion: String? = null

        fun isEmui(): Boolean {
            return check(ROM_EMUI)
        }

        fun isMiui(): Boolean {
            return check(ROM_MIUI)
        }

        fun isVivo(): Boolean {
            return check(ROM_VIVO)
        }

        fun isOppo(): Boolean {
            return check(ROM_OPPO)
        }

        fun isFlyme(): Boolean {
            return check(ROM_FLYME)
        }

        fun is360(): Boolean {
            return check(ROM_QIKU) || check("360")
        }

        fun isSmartisan(): Boolean {
            return check(ROM_SMARTISAN)
        }

        fun getName(): String? {
            if (sName == null) {
                check("")
            }
            return sName
        }

        fun getVersion(): String? {
            if (sVersion == null) {
                check("")
            }
            return sVersion
        }

        fun check(rom: String): Boolean {
            if (sName != null) {
                return sName == rom
            }
            if (!TextUtils.isEmpty(getProp(KEY_VERSION_MIUI).also {
                    sVersion = it
                })) {
                sName = ROM_MIUI
            } else if (!TextUtils.isEmpty(getProp(KEY_VERSION_EMUI).also {
                    sVersion = it
                })) {
                sName = ROM_EMUI
            } else if (!TextUtils.isEmpty(getProp(KEY_VERSION_OPPO).also {
                    sVersion = it
                })) {
                sName = ROM_OPPO
            } else if (!TextUtils.isEmpty(getProp(KEY_VERSION_VIVO).also {
                    sVersion = it
                })) {
                sName = ROM_VIVO
            } else if (!TextUtils.isEmpty(getProp(KEY_VERSION_SMARTISAN).also {
                    sVersion = it
                })) {
                sName = ROM_SMARTISAN
            } else {
                sVersion = Build.DISPLAY
                if (sVersion!!.toUpperCase().contains(ROM_FLYME)) {
                    sName = ROM_FLYME
                } else {
                    sVersion = Build.UNKNOWN
                    sName = Build.MANUFACTURER.toUpperCase()
                }
            }
            return sName == rom
        }

        fun getProp(name: String): String? {
            var line: String? = null
            var input: BufferedReader? = null
            try {
                val p = Runtime.getRuntime().exec("getprop $name")
                input = BufferedReader(InputStreamReader(p.inputStream), 1024)
                line = input.readLine()
                input.close()
            } catch (ex: IOException) {
                return null
            } finally {
                if (input != null) {
                    try {
                        input.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
            return line
        }
    }
}