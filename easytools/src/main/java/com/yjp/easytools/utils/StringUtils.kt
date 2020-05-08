package com.yjp.easytools.utils

/**
 * 字符串操作工具类$
 * @author yjp
 * @date 2020/3/31 10:10
 */
object StringUtils {
    /**
     * 根据资源ID获取字符串
     *
     * @param resId
     * @return
     */
    fun getString(resId: Int): String {
        return Utils.context!!.getString(resId)
    }

    /**
     * 判断字符串是否为空
     *
     * @param str : 需要判断的字符串
     * @return Boolean
     */
    fun isEmpty(str: String?): Boolean {
        var str = str
        str = delInvisibleChar(str)
        return str == null || str == "" || str.isEmpty()
    }

    /**
     * 剔除不可见字符串
     *
     * @param str : 需要处理的字符串
     * @return String
     */
    fun delInvisibleChar(str: String?): String {
        return str?.replace("[ \n\r\t]".toRegex(), "") ?: ""
    }

    /**
     * 判断字符串是否为null或全为空格
     *
     * @param s 待校验字符串
     * @return `true`: null或全空格<br></br> `false`: 不为null且不全空格
     */
    fun isTrimEmpty(s: String?): Boolean {
        return s == null || s.trim { it <= ' ' }.isEmpty()
    }

    /**
     * 判断字符串是否为null或全为空白字符
     *
     * @param s 待校验字符串
     * @return `true`: null或全空白字符<br></br> `false`: 不为null且不全空白字符
     */
    fun isSpace(s: String?): Boolean {
        if (s == null) return true
        var i = 0
        val len = s.length
        while (i < len) {
            if (!Character.isWhitespace(s[i])) {
                return false
            }
            ++i
        }
        return true
    }

    /**
     * 判断两字符串是否相等
     *
     * @param a 待校验字符串a
     * @param b 待校验字符串b
     * @return `true`: 相等<br></br>`false`: 不相等
     */
    fun equals(a: CharSequence?, b: CharSequence?): Boolean {
        if (a === b) return true
        return if (a != null && b != null && a.length == b.length) {
            val length = a.length
            if (a is String && b is String) {
                a == b
            } else {
                for (i in 0 until length) {
                    if (a[i] != b[i]) return false
                }
                true
            }
        } else false
    }

    /**
     * 判断两字符串忽略大小写是否相等
     *
     * @param a 待校验字符串a
     * @param b 待校验字符串b
     * @return `true`: 相等<br></br>`false`: 不相等
     */
    fun equalsIgnoreCase(a: String?, b: String?): Boolean {
        return a?.equals(b, ignoreCase = true) ?: (b == null)
    }

    /**
     * null转为长度为0的字符串
     *
     * @param s 待转字符串
     * @return s为null转为长度为0字符串，否则不改变
     */
    fun null2Length0(s: String?): String? {
        return s ?: ""
    }

    /**
     * 返回字符串长度
     *
     * @param s 字符串
     * @return null返回0，其他返回自身长度
     */
    fun length(s: CharSequence?): Int {
        return s?.length ?: 0
    }

    /**
     * 首字母大写
     *
     * @param s 待转字符串
     * @return 首字母大写字符串
     */
    fun upperFirstLetter(s: String?): String? {
        return if (isEmpty(s)) s
        else if (!Character.isLowerCase(s!![0])) s
        else (s[0].toInt() - 32).toString() + s.substring(1)
    }

    /**
     * 首字母小写
     *
     * @param s 待转字符串
     * @return 首字母小写字符串
     */
    fun lowerFirstLetter(s: String): String? {
        if (isEmpty(s) || !Character.isUpperCase(s[0])) return s
        return (s[0].toInt() + 32).toString() + s.substring(1)
    }

    /**
     * 反转字符串
     *
     * @param s 待反转字符串
     * @return 反转字符串
     */
    fun reverse(s: String): String? {
        val len: Int = length(s)
        if (len <= 1) return s
        val mid = len shr 1
        val chars = s.toCharArray()
        var c: Char
        for (i in 0 until mid) {
            c = chars[i]
            chars[i] = chars[len - i - 1]
            chars[len - i - 1] = c
        }
        return String(chars)
    }

    /**
     * 转化为半角字符
     *
     * @param s 待转字符串
     * @return 半角字符串
     */
    fun toDBC(s: String): String? {
        if (isEmpty(s)) return s
        val chars = s.toCharArray()
        var i = 0
        val len = chars.size
        while (i < len) {
            when {
                chars[i].toInt() == 12288 -> {
                    chars[i] = ' '
                }
                chars[i].toInt() in 65281..65374 -> {
                    chars[i] = (chars[i] - 65248)
                }
                else -> {
                    chars[i] = chars[i]
                }
            }
            i++
        }
        return String(chars)
    }

    /**
     * 转化为全角字符
     *
     * @param s 待转字符串
     * @return 全角字符串
     */
    fun toSBC(s: String): String? {
        if (isEmpty(s)) return s
        val chars = s.toCharArray()
        var i = 0
        val len = chars.size
        while (i < len) {
            when {
                chars[i] == ' ' -> {
                    chars[i] = 12288.toChar()
                }
                chars[i].toInt() in 33..126 -> {
                    chars[i] = (chars[i] + 65248)
                }
                else -> {
                    chars[i] = chars[i]
                }
            }
            i++
        }
        return String(chars)
    }

    /**
     * 判断传入的字符串是否是double型
     *
     * @param s
     * @return
     */
    fun isDouble(s: String): Boolean {
        return s.matches(Regex("^[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*$"))
    }

    /**
     * 判断传入的字符串是不是正整数
     *
     * @param s
     * @return
     */
    fun isNumber(s: String): Boolean {
        return s.matches(Regex("^[1-9]\\d*$"))
    }

    /**
     * 判断传入的字符串是不是数字,带小数点
     *
     * @param s
     * @return
     */
    fun isDigital(s: String): Boolean {
        return isDouble(s) || isNumber(s)
    }
}