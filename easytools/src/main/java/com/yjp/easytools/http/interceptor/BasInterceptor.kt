package com.yjp.easytools.http.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * $
 * @author yjp
 * @date 2020/4/1 14:30
 */
class BaseInterceptor(private var headers: Map<String, String>?) :
    Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request()
            .newBuilder()
        if (headers != null && headers!!.isNotEmpty()) {
            val keys = headers!!.keys
            for (headerKey in keys) {
                builder.addHeader(headerKey, headers!![headerKey] ?: error("")).build()
            }
        }
        //请求信息
        return chain.proceed(builder.build())
    }

    init {
        this.headers = headers
    }
}