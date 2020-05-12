package com.yjp.easytools.utils

import kotlin.math.abs

/**
 * $
 * @author yjp
 * @date 2020/3/31 18:20
 */
object LonLatUtil {
    /**
     *
     */
    fun toDecimal(latlng: Double, jf: Float): Double {
        //替换′和″错误的情况
        var str = toLonLatStr(latlng)
        str = str.replace("['’‘]".toRegex(), "′").replace("[“”]".toRegex(), "″").replace('"', '″')
        var du = 0f
        var fen = 0f
        var miao = 0f
        if (str.contains("°")) {
            du = str.substring(0, str.indexOf("°")).toFloat()
            if (str.contains("′")) {
                fen = str.substring(
                    str.indexOf("°") + 1,
                    str.indexOf("′")
                ).toFloat() - jf
                if (str.contains("″")) {
                    miao = str.substring(
                        str.indexOf("′") + 1,
                        str.indexOf("″")
                    ).toFloat()
                }
            }
        }
        val lonlat = if (du < 0) {
            -(abs(du) + (fen + miao / 60) / 60)
        } else {
            du + (fen + miao / 60) / 60
        }
        return lonlat.toDouble()
    }

    /**
     * 字符串经纬度转换为小数形式
     *
     * @param str   字符串(87°23′55″)
     * @param digit 保留位数
     * @return
     */
    fun toDecimal(str: String, digit: Int?): Float {
        //替换′和″错误的情况
        var str = str
        str = str.replace("['’‘]".toRegex(), "′").replace("[“”]".toRegex(), "″").replace('"', '″')
        var du = 0f
        var fen = 0f
        var miao = 0f
        if (str.contains("°")) {
            du = str.substring(0, str.indexOf("°")).toFloat()
            if (str.contains("′")) {
                fen = str.substring(str.indexOf("°") + 1, str.indexOf("′")).toFloat()
                if (str.contains("″")) {
                    miao = str.substring(
                        str.indexOf("′") + 1,
                        str.indexOf("″")
                    ).toFloat()
                }
            }
        }
        var lonlat = if (du < 0) {
            -(abs(du) + (fen + miao / 60) / 60)
        } else {
            du + (fen + miao / 60) / 60
        }
        return if (digit == null) lonlat else java.lang.Float.valueOf(
            String.format(
                "%." + digit + "f",
                lonlat
            )
        )
    }

    /**
     * 字符串经纬度转换为小数形式
     *
     * @param str 字符串(87°23′55″)
     * @return
     */
    fun toDecimal(str: String): Float {
        return toDecimal(str, null)
    }

    /**
     * 小数经纬度转字符串格式
     *
     * @param lonlat 经纬度值
     * @param digit  秒保留位数
     * @return
     */
    fun toLonLatStr(lonlat: Double, digit: Int?): String {
        var str = ""
        var temp = lonlat
        //处理经纬度小于0的情况
        if (lonlat < 0) {
            str = "-"
            temp = -lonlat
        }
        //获取整数部分
        val du = Math.floor(temp).toInt()
        temp = (temp - du) * 60
        val fen = Math.floor(temp).toInt()
        temp = (temp - fen) * 60
        val miao = temp.toFloat()
        return "$str$du°$fen′" + (if (digit == null) miao else String.format(
            "%." + digit + "f",
            miao
        )) + "″"
    }

    /**
     * 小数经纬度转字符串格式
     *
     * @param lonlat 经纬度值
     * @return
     */
    fun toLonLatStr(lonlat: Double): String {
        return toLonLatStr(lonlat, null)
    }

    @JvmStatic
    fun main(args: Array<String>) { //103.99998,30.632695
        println(toDecimal("-99°2′0″", 2))
        println(toLonLatStr(103.99998))
    }
}