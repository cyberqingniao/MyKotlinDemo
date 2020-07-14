package com.yjp.easytools.utils

import android.annotation.SuppressLint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * 日期时间操作工具$
 *
 * @author yjp
 * @date 2020-03-29 15:24
 */
@SuppressLint("SimpleDateFormat")
object DateUtils {
    /**
     * 中文星期
     */
    val WEEK_CN = arrayOf("星期天", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六")

    /**
     * 英文星期
     */
    val WEEK_EN =
        arrayOf("SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY")

    /**
     * 当前年
     */
    val YEAR by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        Calendar.getInstance().get(Calendar.YEAR)
    }

    /**
     * 当前月
     */
    val MONTH by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        Calendar.getInstance().get(Calendar.MONTH)
    }

    /**
     * 当前天
     */
    val DAY by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    }

    /**
     * 当前小时
     */
    val HOUR by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    }

    /**
     * 当前分
     */
    val MIN by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        Calendar.getInstance().get(Calendar.MINUTE)
    }

    /**
     * 当前秒
     */
    val SEC by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        Calendar.getInstance().get(Calendar.SECOND)
    }

    /**
     * 星期
     */
    val WEEK by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        WEEK_CN[WEEK_INDEX]
    }

    /**
     * 星期下标
     */
    val WEEK_INDEX by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
    }

    /**
     * 日期转字符串
     */
    fun data2Str(date: Any): String {
        return data2Str(date, FORMAT.DATETIME_HORIZONTAL)
    }

    /**
     * 日期转字符串
     */
    fun data2Str(date: Any, format: String): String {
        val sdf = SimpleDateFormat(format)
        return when (date) {
            is Date -> {
                sdf.format(date)
            }
            is Long -> {
                sdf.format(Date(date))
            }
            is Calendar -> {
                sdf.format(date.time)
            }
            else -> {
                throw RuntimeException("传入日期有误，可以是Date、long、Calendar三种类型")
            }
        }
    }

    /**
     * 字符串转日期
     */
    fun str2Date(date: String): Date? {
        return str2Date(date, FORMAT.DATETIME_HORIZONTAL)
    }

    /**
     * 字符串转日期
     */
    fun str2Date(date: String, format: String): Date? {
        try {
            val sdf = SimpleDateFormat(format)
            return sdf.parse(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 转化时间输入时间与当前时间的间隔
     */
    fun converTime(timestamp: Long): String {
        var currentSeconds = System.currentTimeMillis() / 1000
        var timeGap = currentSeconds - timestamp;
        return when {
            timeGap > 24 * 60 * 60 -> {
                (timeGap / (24 * 60 * 60)).toString() + "天前"
            }
            timeGap > 60 * 60 -> {
                (timeGap / (60 * 60)).toString() + "小时前"
            }
            timeGap > 60 -> {
                (timeGap / 60).toString() + "分钟前"
            }
            else -> {
                "刚刚"
            }
        }
    }

    /**
     * 判断原日期是否在目标日期之前
     */
    fun isBefore(src: Date, dst: Date): Boolean {
        return src.before(dst)
    }

    /**
     * 判断原日期是否在目标日期之后
     */
    fun isAfter(src: Date, dst: Date): Boolean {
        return src.after(dst)
    }

    /**
     * 判断两日期是否相同
     */
    fun isEqual(src: Date, dst: Date): Boolean {
        return src.compareTo(dst) == 0
    }

    /**
     * 判断某个日期是否在某个日期范围
     */
    fun between(beginDate: Date, endDate: Date, src: Date): Boolean {
        return beginDate.before(src) && endDate.after(src)
    }

    /**
     * 比较时间大小
     * 1:小于 -2:大于 0:等于
     */
    fun compareDate(begin: String, end: String): Int {
        val df = SimpleDateFormat(FORMAT.DATETIME_YMDHM)
        try {
            val beginDate = df.parse(begin)
            val endDate = df.parse(end)
            return when {
                beginDate!!.time < endDate!!.time -> {
                    1
                }
                beginDate.time > endDate.time -> {
                    -1
                }
                else -> {
                    0
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return 0
    }

    /**
     * 获取天数差
     */
    fun dayDiff(begin: Date, end: Date): Long {
        return when {
            end.time < begin.time -> {
                -1
            }
            end.time == begin.time -> {
                1
            }
            else -> {
                ((end.time - begin.time) / (24 * 60 * 60 * 1000)) + 1
            }
        }
    }

    /**
     * 获取月的最后一天
     */
    fun lastDayOfMonth(date: Date): Date {
        val cal = Calendar.getInstance()
        cal.time = date
        //月置零
        cal.set(Calendar.DAY_OF_MONTH, 0)
        //小时置零
        cal.set(Calendar.HOUR_OF_DAY, 0)
        //分置零
        cal.set(Calendar.MINUTE, 0)
        //秒置零
        cal.set(Calendar.SECOND, 0)
        //毫秒置零
        cal.set(Calendar.MILLISECOND, 0)
        //月份+1
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1)
        //毫秒-1
        cal.set(Calendar.MILLISECOND, -1)
        return cal.time
    }

    /**
     * 获取月的第一天
     */
    fun firstDayOfMonth(date: Date): Date {
        val cal = Calendar.getInstance()
        cal.time = date
        //月置零
        cal.set(Calendar.DAY_OF_MONTH, 1)
        //小时置零
        cal.set(Calendar.HOUR_OF_DAY, 0)
        //分置零
        cal.set(Calendar.MINUTE, 0)
        //秒置零
        cal.set(Calendar.SECOND, 0)
        //毫秒置零
        cal.set(Calendar.MILLISECOND, 0)
        return cal.time
    }

    /**
     * 获取传入的日期月份最大天数
     */
    fun getMonthMaxDay(date: Date): Int {
        val c = Calendar.getInstance()
        c.time = date
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH))
        return c.get(Calendar.DAY_OF_MONTH)
    }

    /**
     * 判断是否是平年
     * */
    fun isPingNian(year: Int): Boolean {
        return year % 4 == 0 && year % 100 != 0 || year % 400 == 0
    }

    /**
     * 定义日期的格式
     * */
    object FORMAT {
        const val DATETIME_TXT = "yyyy年MM月dd日 HH:mm:ss"
        const val DATETIME_YMDHM = "yyyy-MM-dd HH:mm"
        const val YEAR_MONTH = "yyyy-MM"

        /**
         * 6位日期格式
         */
        const val DATE_6CHAR = "yyMMdd"

        /**
         * 8位日期格式
         */
        const val DATE_8CHAR = "yyyyMMdd"

        /**
         * 点号日期格式
         */
        const val DATE_DOT = "yyyy.MM.dd"

        /**
         * 反斜杠日期格式
         */
        const val DATE_SLASH = "yyyy/MM/dd"

        /**
         * 横杠日期格式
         */
        const val DATE_HORIZONTAL = "yyyy-MM-dd"

        /**
         * 日期时间(日期点号分割)
         */
        const val DATATIME_DOT = "yyyy.MM.dd HH:mm:ss"

        /**
         * 日期时间（文字分割）
         */
        const val DATATIME_TXT = "yyyy年MM月dd日 HH:mm"

        const val DATETXT = "yyyy年MM月dd日"

        const val DATE_TXT = "yyyy年MM月dd日 HH:mm:ss"

        /**
         * 日期时间(日期反斜杠)
         */
        const val DATETIME_SLASH = "yyyy/MM/dd HH:mm:ss"

        /**
         * 日期时间(日期横杠)
         */
        const val DATETIME_HORIZONTAL = "yyyy-MM-dd HH:mm:ss"
    }
}