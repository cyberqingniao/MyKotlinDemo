package com.yjp.easytools.utils

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.annotation.StringRes
import androidx.core.content.FileProvider
import okio.internal.commonAsUtf8ToByteArray
import org.jetbrains.annotations.Contract
import java.io.File
import java.io.UnsupportedEncodingException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import kotlin.experimental.and

/**
 * 通用工具包$
 * @author yjp
 * @date 2020/3/27 19:39
 */
object Utils {

    lateinit var context: Context
        private set

    fun init(context: Context) {
        this.context = context.applicationContext
    }

    /**
     * 16位MD5加密
     */
    fun md5Decode32(content: String): String {
        val hash: ByteArray
        try {
            hash = MessageDigest.getInstance("MD5").digest(content.commonAsUtf8ToByteArray())
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException("NoSuchAlgorithmException", e)
        } catch (e: UnsupportedEncodingException) {
            throw RuntimeException("UnsupportedEncodingException", e)
        }
        val hex = StringBuilder(hash.size * 2)
        for (b in hash) {
            if (b and 0xFF.toByte() < 0x10) {
                hex.append("0")
            }
            hex.append(Integer.toHexString((b and 0xFF.toByte()).toInt()))
        }
        return hex.toString()
    }

    /**
     * dp转px
     */
    fun dp2px(value: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (value * (scale + 0.5f)).toInt()
    }

    /**
     * px转dp
     */
    fun px2dp(value: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (value / scale + 0.5).toInt()
    }

    /**
     * px转sp
     */
    fun px2sp(value: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (value / scale + 0.5).toInt()
    }

    /**
     * sp转px
     */
    fun sp2px(value: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (value * scale + 0.5).toInt()
    }

    /**
     * 判断对象是否为空
     */
    @Contract(value = "null->true;!null->false", pure = true)
    fun isEmpty(obj: Any?): Boolean {
        return obj == null
    }

    /**
     * 判断对象是否为空
     */
    @Contract(value = "null->true;!null->false", pure = true)
    fun isEmpty(map: Map<*, *>?): Boolean {
        return map == null || map.isEmpty()
    }

    /**
     * 判断对象是否为空
     */
    @Contract(value = "null->true;!null->false", pure = true)
    fun isEmpty(list: List<*>?): Boolean {
        return list == null || list.isEmpty()
    }

    /**
     * 判断字符串是否为空
     */
    fun isEmpty(s: String?): Boolean {
        return StringUtils.isEmpty(s)
    }

    /**
     * 根据资源ID获取字符串
     */
    fun getString(@StringRes resId: Int): String {
        return context.getString(resId)
    }

    /**
     * 读取字符串
     * 如果为空则返回空字符串
     * 如果不是String类型则转成String类型返回
     */
    fun getString(obj: Any?): String {
        if (obj == null) {
            return ""
        }
        return StringUtils.delInvisibleChar(
            if (obj is String) {
                obj
            } else {
                obj.toString()
            }
        )
    }

    /**
     * 获取类对象属性名
     *
     * @param obj : 实体类
     * @return
     */
    fun getFiledName(obj: Any): Array<String?> {
        val fields = obj.javaClass.declaredFields
        val fieldNames = arrayOfNulls<String>(fields.size)
        for (i in fieldNames.indices) {
            fieldNames[i] = fields[i].name
        }
        return fieldNames
    }

    /**
     * 根据属性名获取该属性的属性值
     * get方法获取
     *
     * @param fieldName : 属性名
     * @param obj         : 实体类
     * @return Object 属性值
     */
    fun getFieldValueByName(fieldName: String, obj: Any): Any? {
        try {
            val firstLetter = fieldName.substring(0, 1).toUpperCase(Locale.ROOT)
            val getter = "get" + firstLetter + fieldName.substring(1)
            val method = obj.javaClass.getMethod(getter)
            return method.invoke(obj)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 判断服务是否在运行
     *
     * @return
     */
    fun isServiceRunning(serviceName: String): Boolean {
        var isRun = false
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?
        if (am != null) {
            val list = am.getRunningServices(Int.MAX_VALUE)
            if (!isEmpty(list)) {
                for (info in list) {
                    if (info.service.className == serviceName) {
                        isRun = true
                        break
                    }
                }
            }
        }
        return isRun
    }

    /**
     * 判断程序是否处于后台
     *
     * @return
     */
    fun isBackground(): Boolean {
        val isBack = false;
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?
        if (am != null) {
            val aps = am.runningAppProcesses
            if (aps != null) {
                for (ap in aps) {
                    if (ap.processName == context.packageName) {
                        return ap.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                    }
                }
            }
        }
        return isBack
    }

    /**
     * 获取屏幕高度
     *
     * @return
     */
    fun getScreenHeight(): Int {
        val dm = DisplayMetrics()
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        wm.defaultDisplay.getMetrics(dm)
        return dm.heightPixels
    }

    /**
     * 获取屏幕宽度
     *
     * @return
     */
    fun getScreenWidth(): Int {
        val dm = DisplayMetrics()
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        wm.defaultDisplay.getMetrics(dm)
        return dm.widthPixels
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    fun getStatusBarHeight(): Int {
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            context.resources.getDimensionPixelSize(resourceId)
        } else {
            0
        }
    }

    /**
     * 获取导航栏高度
     *
     * @param context
     * @return
     */
    fun getNavigationBarHeight(): Int {
        val resourceId =
            context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            context.resources.getDimensionPixelSize(resourceId)
        } else {
            0
        }
    }

    /**
     * 获取Drawable
     *
     * @param resId : 资源Id
     * @return Drawable
     */
    fun getDrawable(resId: Int): Drawable {
        val drawable = context.resources.getDrawable(resId)
        drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
        return drawable
    }

    /**
     * 根据传入的文件名与资源名，动态获取资源
     *
     * @param fileName
     * @param name
     * @return
     */
    fun getResId(filePath: String, fileName: String): Int {
        return context.resources.getIdentifier(fileName, filePath, context.packageName)
    }

    /**
     * 唤醒屏幕并解锁
     *
     * @param context
     */
    @SuppressLint("InvalidWakeLockTag")
    fun wakeUpAndUnlock() {
        try {
            val km = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            val kl = km.newKeyguardLock("unLock")
            kl.disableKeyguard()
            val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
            val wl = pm.newWakeLock(
                PowerManager.ACQUIRE_CAUSES_WAKEUP or PowerManager.SCREEN_DIM_WAKE_LOCK,
                "bright"
            )
            wl.acquire(10 * 60 * 1000L)
            wl.release()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 判断是否安装了某应用
     */
    fun isInstallApp(pkgName: String): Boolean {
        try {
            val info: List<PackageInfo> = context.packageManager.getInstalledPackages(0)
            if (info.isEmpty()) {
                return false
            } else {
                for (i in info) {
                    if (pkgName == i.packageName) {
                        return true
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * 安装APK
     */
    fun installAPK(file: File) {
        val intent = Intent(Intent.ACTION_VIEW)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            val uri =
                FileProvider.getUriForFile(context, context.packageName + ".fileProvider", file)
            intent.setDataAndType(uri, "application/vnd.android.package-archive")
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }

    /**
     * 二进制数组转十六进制字符串
     *
     * @param bytes byte array to be converted
     * @return string containing hex values
     */
    fun byteArrayToHexString(bytes: ByteArray): String {
        val sb = StringBuilder(bytes.size * 2)
        for (element in bytes) {
            val v: Byte = element and 0xff.toByte()
            if (v < 16) {
                sb.append('0')
            }
            sb.append(Integer.toHexString(v.toInt()))
        }
        return sb.toString().toUpperCase(Locale.US)
    }

    /**
     * 十六进制字符串转二进制数组
     *
     * @param hexString string of hex-encoded values
     * @return decoded byte array
     */
    fun hexStringToByteArray(hexString: String): ByteArray {
        val len = hexString.length
        val data = ByteArray(len / 2)
        var i = 0
        while (i < len) {
            data[i / 2] = ((Character.digit(
                hexString[i],
                16
            ) shl 4) + Character.digit(hexString[i + 1], 16)).toByte()
            i += 2
        }
        return data
    }

}