package com.yjp.easytools.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.telephony.TelephonyManager
import android.text.format.Formatter
import java.io.IOException
import java.net.HttpURLConnection
import java.net.NetworkInterface
import java.net.URL

/**
 * 网络操作工具类$
 * @author yjp
 * @date 2020/3/31 10:52
 */
object NetworkUtils {
    /**
     * 判断是否有网
     */
    fun isNetworkAvailable(): Boolean {
        val manager =
            Utils.context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = manager.activeNetworkInfo
        if (null == info || !info.isAvailable) {
            return false
        }
        return true
    }

    /**
     * 获取本地IP
     *
     * @return
     */
    fun getIP(): String {
        val en =
            NetworkInterface.getNetworkInterfaces()
        while (en.hasMoreElements()) {
            val intf = en.nextElement()
            val enumIpAddr = intf.inetAddresses
            while (enumIpAddr.hasMoreElements()) {
                val inetAddress = enumIpAddr.nextElement()
                if (!inetAddress.isLoopbackAddress) {
                    return inetAddress.hostAddress
                }
            }
        }
        return ""
    }

    /**
     * wifi获取 路由ip地址
     *
     * @param context
     * @return
     */
    fun getWifiIP(): String {
        val wifi_service =
            Utils.context!!.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val dhcpInfo = wifi_service.dhcpInfo
        return Formatter.formatIpAddress(dhcpInfo.gateway)
    }

    /**
     * h获取网络状态
     *
     * @return
     */
    fun getState(): Int {
        try {
            val connectivity =
                Utils.context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkinfo = connectivity.activeNetworkInfo
            if (networkinfo != null) {
                return if (networkinfo.isAvailable && networkinfo.isConnected) {
                    if (!connectionNetwork()) {
                        // No NetworkAvailable
                        2
                    } else {
                        // NetworkAvailable
                        1
                    }
                } else {
                    // Net No Ready
                    3
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        // Net Error
        return 4
    }

    /**
     * ping "www.baidu.com"
     *
     * @return
     */
    fun connectionNetwork(): Boolean {
        var result = false
        var httpUrl: HttpURLConnection? = null
        try {
            httpUrl = URL("www.baidu.com").openConnection() as HttpURLConnection
            // TIMEOUT
            val TIMEOUT = 3000
            httpUrl.connectTimeout = TIMEOUT
            httpUrl.connect()
            result = true
        } catch (ignored: IOException) {
        } finally {
            httpUrl?.disconnect()
            httpUrl = null
        }
        return result
    }

    /**
     * check is 2G
     *
     * @return boolean
     */
    fun is2G(): Boolean {
        val connectivityManager = Utils.context!!
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var activeNetInfo: NetworkInfo? = null
        activeNetInfo = connectivityManager.activeNetworkInfo
        return (activeNetInfo != null
                && (activeNetInfo.subtype == TelephonyManager.NETWORK_TYPE_EDGE || activeNetInfo.subtype == TelephonyManager.NETWORK_TYPE_GPRS || activeNetInfo
            .subtype == TelephonyManager.NETWORK_TYPE_CDMA))
    }

    /**
     * check is3G
     *
     * @return boolean
     */
    fun is3G(): Boolean {
        val connectivityManager = Utils.context!!
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var activeNetInfo: NetworkInfo? = null
        activeNetInfo = connectivityManager.activeNetworkInfo
        return (activeNetInfo != null && activeNetInfo.type == ConnectivityManager.TYPE_MOBILE)
    }

    /**
     * check is Wifi
     *
     * @return boolean
     */
    fun isWifi(): Boolean {
        val connectivityManager = Utils.context!!
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var activeNetInfo: NetworkInfo? = null
        activeNetInfo = connectivityManager.activeNetworkInfo
        return (activeNetInfo != null && activeNetInfo.type == ConnectivityManager.TYPE_WIFI)
    }

    /**
     * check is wifi on
     */
    fun isWifiEnabled(): Boolean {
        val mgrConn = Utils.context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val mgrTel = Utils.context!!.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return mgrConn.activeNetworkInfo != null && mgrConn.activeNetworkInfo.state == NetworkInfo.State.CONNECTED || mgrTel.networkType == TelephonyManager.NETWORK_TYPE_UMTS
    }

    /**
     * 将ip的整数形式转换成ip形式
     *
     * @param ipInt
     * @return
     */
    fun int2ip(ipInt: Int): String? {
        return (ipInt and 0xFF).toString() + "." +
                (ipInt shr 8 and 0xFF) + "." +
                (ipInt shr 16 and 0xFF) + "." +
                (ipInt shr 24 and 0xFF)
    }
}