package com.yjp.easytools.utils

import android.annotation.SuppressLint
import android.content.Context
import okio.internal.commonAsUtf8ToByteArray
import java.io.UnsupportedEncodingException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.experimental.and

/**
 * 通用工具包$
 * @author yjp
 * @date 2020/3/27 19:39
 */
object Utils {
    @SuppressLint("StaticFieldLeak")
    var context: Context? = null
        get() = field
        private set

    fun init(context: Context) {
        this.context = context.applicationContext
    }

    fun md5Decode32(content: String): String {
        var hash: ByteArray
        try {
            hash = MessageDigest.getInstance("MD5").digest(content.commonAsUtf8ToByteArray())
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException("NoSuchAlgorithmException", e)
        } catch (e: UnsupportedEncodingException) {
            throw RuntimeException("UnsupportedEncodingException", e)
        }
        var hex = StringBuilder(hash.size * 2)
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
    fun dp2px(context: Context, value: Float): Int {
        var scale = context.resources.displayMetrics.density
        return (value * (scale + 0.5f)).toInt()
    }
}