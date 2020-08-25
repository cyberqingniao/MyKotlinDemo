package com.yjp.easytools.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import androidx.annotation.RequiresPermission
import java.io.*
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
    val VERSION_CODE: Long
        get() {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                getPackageInfo()?.longVersionCode!!
            } else {
                getPackageInfo()?.versionCode!!.toLong()
            }
        }

    /**
     * 获取手机IMEI(需要“android.permission.READ_PHONE_STATE”权限)
     *
     * @return 手机IMEI
     */
    @RequiresPermission(allOf = [Manifest.permission.READ_PHONE_STATE, Manifest.permission.CHANGE_WIFI_STATE])
    fun getDeviceId(): String {
        var deviceId = getIMEI()
        if (Utils.isEmpty(deviceId)) {
            deviceId = getIMSI()
        }
        if (Utils.isEmpty(deviceId)) {
            deviceId = getWifiManagerMacAddress()
        }
        if (Utils.isEmpty(deviceId)) {
            deviceId = getAndroidId()
        }
        return deviceId!!
    }

    /**
     * 通过Linux命令获取Mac地址
     * 如果设备没有启动WiFi则返回空
     */
    fun getLinuxMacAddress(): String? {
        var macAddress: String? = null
        var str = ""
        try {
            val pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address")
            val isr = InputStreamReader(pp.inputStream)
            val input = LineNumberReader(isr)
            while (input.readLine()?.let { str = it } != null) {
                macAddress = str.trim()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return macAddress
    }

    /**
     * 通过WifiManager获取Mac地址
     */
    @SuppressLint("HardwareIds")
    @RequiresPermission(Manifest.permission.CHANGE_WIFI_STATE)
    fun getWifiManagerMacAddress(): String? {
        val wifiManager =
            Utils.context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager?
        wifiManager?.let {
            if (!it.isWifiEnabled) {
                it.isWifiEnabled = true
                it.isWifiEnabled = false
            }
        }
        val info = wifiManager?.connectionInfo
        return info?.macAddress
    }

    @SuppressLint("HardwareIds")
    @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
    fun getIMSI(): String? {
        val mTelephonyManager =
            Utils.context.applicationContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager?
        mTelephonyManager?.let {
            return it.deviceId
        }
        return null
    }

    @SuppressLint("HardwareIds")
    @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
    fun getIMEI(): String? {
        val mTelephonyManager =
            Utils.context.applicationContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager?
        mTelephonyManager?.let {
            return it.subscriberId
        }
        return null
    }

    /**
     * 获取AndroidId
     */
    fun getAndroidId(): String {
        var androidId = SPUtils.getString("ANDROID_ID", "")
        if (Utils.isEmpty(androidId)) {
            androidId = Settings.Secure.ANDROID_ID
            SPUtils.put("ANDROID_ID", androidId)
        }
        return androidId
    }

    /**
     * 获取包名信息
     */
    fun getPackageInfo(): PackageInfo? {
        var pi: PackageInfo? = null
        try {
            val pm = Utils.context.packageManager
            pi = pm.getPackageInfo(Utils.context.packageName, PackageManager.GET_CONFIGURATIONS)
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

    fun resetAPP(context: Context) {
        val intent =
            context.packageManager.getLaunchIntentForPackage(context.packageName)
        intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        context.startActivity(intent)
    }
}