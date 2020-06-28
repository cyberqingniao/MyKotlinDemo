package com.yjp.easytools.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.telephony.TelephonyManager
import android.util.Log
import java.net.NetworkInterface
import java.net.SocketException
import java.util.*

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
            Utils.context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            manager.isDefaultNetworkActive
        } else {
            val nwi = manager.activeNetworkInfo
            !(nwi == null || nwi.isAvailable)
        }
    }

    /**
     * ping 百度 判断当前连接的网络是否有外网访问
     */
    fun ping(): Boolean {
        val address = "www.baidu.com"
        val p = Runtime.getRuntime().exec("ping -c 3 -w 100 $address")
        return p.waitFor() == 0
    }


    /**
     * 获取网络信息
     */
    private fun getActiveNetworkInfo(): NetworkInfo? {
        val manager =
            Utils.context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return manager.activeNetworkInfo
    }

    /**
     * 获取网络状态
     */
    fun getNetworkType(): NetworkType {
        var netType = NetworkType.NETWORK_NO
        val info = getActiveNetworkInfo()
        if (info != null && info.isAvailable) {
            netType = when (info.type) {
                ConnectivityManager.TYPE_WIFI -> {
                    NetworkType.NETWORK_WIFI
                }
                ConnectivityManager.TYPE_MOBILE -> {
                    when (info.subtype) {
                        TelephonyManager.NETWORK_TYPE_GSM,
                        TelephonyManager.NETWORK_TYPE_GPRS,
                        TelephonyManager.NETWORK_TYPE_CDMA,
                        TelephonyManager.NETWORK_TYPE_EDGE,
                        TelephonyManager.NETWORK_TYPE_1xRTT,
                        TelephonyManager.NETWORK_TYPE_IDEN
                        -> {
                            NetworkType.NETWORK_2G
                        }
                        TelephonyManager.NETWORK_TYPE_TD_SCDMA,
                        TelephonyManager.NETWORK_TYPE_EVDO_A,
                        TelephonyManager.NETWORK_TYPE_UMTS,
                        TelephonyManager.NETWORK_TYPE_EVDO_0,
                        TelephonyManager.NETWORK_TYPE_HSDPA,
                        TelephonyManager.NETWORK_TYPE_HSUPA,
                        TelephonyManager.NETWORK_TYPE_HSPA,
                        TelephonyManager.NETWORK_TYPE_EVDO_B,
                        TelephonyManager.NETWORK_TYPE_EHRPD,
                        TelephonyManager.NETWORK_TYPE_HSPAP
                        -> {
                            NetworkType.NETWORK_3G
                        }
                        TelephonyManager.NETWORK_TYPE_LTE,
                        TelephonyManager.NETWORK_TYPE_IWLAN
                        -> {
                            NetworkType.NETWORK_4G
                        }
                        TelephonyManager.NETWORK_TYPE_NR -> {
                            NetworkType.NETWORK_5G
                        }
                        else -> {
                            val subtypeName = info.subtypeName
                            if (subtypeName.equals("TD-SCDMA", true) ||
                                subtypeName.equals("WCDMA", true) ||
                                subtypeName.equals("CDMA2000", true)
                            ) {
                                NetworkType.NETWORK_3G
                            } else {
                                NetworkType.NETWORK_UNKNOWN
                            }
                        }
                    }
                }
                else -> {
                    NetworkType.NETWORK_UNKNOWN
                }
            }
        }
        return netType
    }

    /**
     * 获取IP地址
     */
    fun getIP(): String {
        return when (getNetworkType()) {
            NetworkType.NETWORK_5G,
            NetworkType.NETWORK_4G,
            NetworkType.NETWORK_3G,
            NetworkType.NETWORK_2G
            -> {
                getLocalIPAddress() ?: ""
            }
            NetworkType.NETWORK_WIFI -> {
                val wifiManager =
                    Utils.context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
                val wifiInfo = wifiManager.connectionInfo
                val ipAddress = wifiInfo.ipAddress
                int2ip(ipAddress)
            }
            else -> {
                ""
            }
        }
    }

    /**
     * 获取本地IP地址
     */
    private fun getLocalIPAddress(): String? {
        try {
            val nilist = Collections.list(NetworkInterface.getNetworkInterfaces())
            for (ni in nilist) {
                val ialist = Collections.list(ni.inetAddresses)
                for (address in ialist) {
                    if (!address.isLoopbackAddress) {
                        return address.hostAddress
                    }
                }
            }
        } catch (e: SocketException) {
            Log.e("NetworkUtils", e.toString())
        }
        return null
    }

    /**
     * 将ip的整数形式转换成ip形式
     *
     * @param ipInt
     * @return
     */
    private fun int2ip(ipInt: Int): String {
        return (ipInt and 0xFF).toString() + "." +
                (ipInt shr 8 and 0xFF) + "." +
                (ipInt shr 16 and 0xFF) + "." +
                (ipInt shr 24 and 0xFF)
    }
}

/**
 * 网络连接状态
 * @author yjp
 * @date 2020/6/5
 */
enum class NetworkType(private var desc: String) {

    NETWORK_WIFI("WiFi"),
    NETWORK_5G("5G"),
    NETWORK_4G("4G"),
    NETWORK_2G("2G"),
    NETWORK_3G("3G"),
    NETWORK_UNKNOWN("Unknown"),
    NETWORK_NO("No network");

    override fun toString(): String {
        return desc
    }
}