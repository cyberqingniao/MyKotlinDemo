package com.yjp.easytools.utils

import android.annotation.SuppressLint
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.telephony.TelephonyManager
import androidx.appcompat.app.AppCompatActivity
import java.io.DataOutputStream
import java.io.File
import java.io.IOException
import java.util.*

/**
 * 系统操作工具类$
 * @author yjp
 * @date 2020/3/31 9:50
 */
@SuppressLint("ConstantLocale")
object SystemUtils {
    /**
     * 获取当前手机系统语言。
     *
     * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
     */
    val LANGUAGE by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { Locale.getDefault().language }
    /**
     * 获取当前系统上的语言列表(Locale列表)
     *
     * @return 语言列表
     */
    val LANGUAGE_LIST by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { Locale.getAvailableLocales() }
    /**
     * 获取当前手机系统版本号
     *
     * @return 系统版本号
     */
    val SYSTEM_VERSION by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { android.os.Build.VERSION.RELEASE }
    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    val PHONE_MODEL by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { android.os.Build.MODEL }
    /**
     * 获取手机厂商
     *
     * @return 手机厂商
     */
    val PHONE_BRAND by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { android.os.Build.BRAND }

    /**
     * 版本名
     */
    val VERSION_NAME by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { getPackageInfo()?.versionName }
    /**
     * 版本号
     */
    val VERSION_CODE by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { getPackageInfo()?.versionCode }

    /**
     * 获取手机IMEI(需要“android.permission.READ_PHONE_STATE”权限)
     *
     * @return 手机IMEI
     */
    @SuppressLint("ServiceCast", "MissingPermission", "HardwareIds")
    fun getPhoneIMEI(): String? {
        val tm = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Utils.context!!.getSystemService(AppCompatActivity.TELECOM_SERVICE) as TelephonyManager?
        } else {
            null
        }
        return tm?.deviceId
    }

    /**
     * 获取包名信息
     */
    fun getPackageInfo(): PackageInfo? {
        var pi: PackageInfo? = null
        try {
            val pm = Utils.context!!.packageManager
            pi = pm.getPackageInfo(Utils.context!!.packageName, PackageManager.GET_CONFIGURATIONS)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return pi
    }

    /**
     * 重启系统
     * */
    fun reBootDevice() {
        createSuProcess("reboot")!!.waitFor()
    }

    /**
     * 重启设备只需要 createSuProcess("reboot").waitFor();
     */
    @Throws(IOException::class)
    fun createSuProcess(cmd: String): Process? {
        var os: DataOutputStream? = null
        val process: Process? = createSuProcess()
        try {
            os = DataOutputStream(process!!.outputStream)
            os.writeBytes(cmd + "\n")
            os.writeBytes("exit $?\n")
        } finally {
            if (os != null) {
                try {
                    os.close()
                } catch (e: IOException) {
                }
            }
        }
        return process
    }

    @Throws(IOException::class)
    fun createSuProcess(): Process? {
        val rootUser = File("/system/xbin/ru")
        return if (rootUser.exists()) {
            Runtime.getRuntime().exec(rootUser.absolutePath)
        } else {
            Runtime.getRuntime().exec("su")
        }
    }
}