package com.yjp.easytools.http.exception

/**
 * $
 * @author yjp
 * @date 2020/4/1 14:21
 */
class ApiException(var throwable: Throwable, var code: Int) : RuntimeException(throwable) {
    var msg: String? = null
}