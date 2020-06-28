package com.yjp.easytools.http.exception

import android.net.ParseException
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import retrofit2.adapter.rxjava2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeoutException

/**
 * 、$
 * @author yjp
 * @date 2020/4/1 14:21
 */
object ExceptionEngine {
    // 对应HTTP的状态码
    private const val FAIL = 0
    private const val UNAUTHORIZED = 401
    private const val FORBIDDEN = 403
    private const val NOT_FOUND = 404
    private const val REQUEST_TIMEOUT = 408
    private const val INTERNAL_SERVER_ERROR = 500
    private const val BAD_GATEWAY = 502
    private const val SERVICE_UNAVAILABLE = 503
    private const val GATEWAY_TIMEOUT = 504

    fun handleException(e: Throwable): ApiException {
        e.printStackTrace()
        var ex: ApiException
        return if (e is ServerException) { // HTTP错误
            ex = ApiException(e, ErrorType.HTTP_ERROR)
            when (e.code) {
                FAIL -> ex.msg = "请求错误"
                FORBIDDEN -> ex.msg = "服务器已经理解请求，但是拒绝执行它"
                NOT_FOUND -> ex.msg = "服务器异常，请稍后再试"
                REQUEST_TIMEOUT -> ex.msg = "请求超时"
                GATEWAY_TIMEOUT -> ex.msg =
                    "作为网关或者代理工作的服务器尝试执行请求时，未能及时从上游服务器（URI标识出的服务器，例如HTTP、FTP、LDAP" +
                            "）或者辅助服务器（例如DNS）收到响应"
                INTERNAL_SERVER_ERROR -> ex.msg = "服务器遇到了一个未曾预料的状况，导致了它无法完成对请求的处理"
                BAD_GATEWAY -> ex.msg = "作为网关或者代理工作的服务器尝试执行请求时，从上游服务器接收到无效的响应"
                SERVICE_UNAVAILABLE -> ex.msg = "由于临时的服务器维护或者过载，服务器当前无法处理请求"
                else -> {
                    // 服务器返回的错误
                    ex = ApiException(e, e.code)
                    ex.msg = e.message
                }
            }
            ex
        } else if (e is JSONException
            || e is ParseException
        ) { // 均视为解析错误
            ex = ApiException(e, ErrorType.PARSE_ERROR)
            ex.msg = "数据解析错误"
            ex
        } else if (e is ConnectException || e is TimeoutException || e is SocketTimeoutException || e is ConnectTimeoutException) { // 均视为网络错误
            ex = ApiException(e, ErrorType.NETWORK_ERROR)
            ex.msg = "连接服务器失败"
            ex
        } else if (e is HttpException) {
            if (e.message?.contains("404") == true) {
                ex = ApiException(e, ErrorType.NETWORK_ERROR)
                ex.msg = "没有发现服务器"
            } else {
                ex = ApiException(e, ErrorType.NETWORK_ERROR)
                ex.msg = "无法连接服务器"
            }
            ex
        } else { // 未知错误
            ex = ApiException(e, ErrorType.UNKONW)
            ex.msg = "未知错误"
            ex
        }
    }
}