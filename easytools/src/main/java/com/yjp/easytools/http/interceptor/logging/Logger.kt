package com.yjp.easytools.http.interceptor.logging

import okhttp3.internal.platform.Platform

/**
 * $
 * @author yjp
 * @date 2020/4/1 14:31
 */
interface Logger {

    fun log(level: Int, tag: String, msg: String)

    companion object {
        val DEFAULT: Logger = object : Logger {
            override fun log(
                level: Int,
                tag: String,
                msg: String
            ) {
                Platform.get().log(msg, level, null)
            }
        }
    }
}