package com.yjp.easytools.http

import com.yjp.easytools.BuildConfig
import com.yjp.easytools.http.cooike.CookieJarImpl
import com.yjp.easytools.http.cooike.store.PersistentCookieStore
import com.yjp.easytools.http.interceptor.logging.Level
import com.yjp.easytools.http.interceptor.logging.LoggingInterceptor
import com.yjp.easytools.utils.SystemUtils
import com.yjp.easytools.utils.Utils
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * $
 * @author yjp
 * @date 2020/4/1 16:24
 */
abstract class BaseHttp private constructor() {
    companion object {
        //最大缓存
        const val MAX_CACHE_SIZE = 1024 * 1024 * 50L
        //链接超时
        const val CONNECT_TIME_OUT = 30L
        //读取超时
        const val READ_TIME_OUT = 60L
        //请求超时
        const val WRITE_TIME_OUT = 60L

    }

    /**
     * 同步创建API
     */
    @Synchronized
    fun <T> getAPI(clazz: Class<T>): T {
        return getRetrofit().create(clazz)
    }

    /**
     * 初始Retrofit
     */
    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(getBaseUrl())
            .client(getOkClient())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * 初始化OkHttp客户端
     */
    fun getOkClient(): OkHttpClient {
        val mLoggingInterceptor = LoggingInterceptor.Builder()
            .loggable(true)
            .setLevel(Level.BASIC)
            .log(Platform.INFO)
            .request("Request")
            .response("Response")
            .addHeader("version", BuildConfig.VERSION_NAME)
            .addHeader("phoneModel", SystemUtils.PHONE_MODEL)
            .build()
        val cacheFile = File(Utils.context!!.cacheDir, "YJP_cache")
        val cache = Cache(cacheFile, MAX_CACHE_SIZE)
        return OkHttpClient.Builder()
            .addInterceptor(getHeaderInterceptor())
            .addInterceptor(mLoggingInterceptor)
            .cookieJar(CookieJarImpl(PersistentCookieStore(Utils.context!!)))
            .cache(cache)
            .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
            .build()
    }

    /**
     * BaseUrl
     */
    abstract fun getBaseUrl(): String

    /**
     * 头参数封装
     */
    abstract fun getHeaderInterceptor(): Interceptor
}