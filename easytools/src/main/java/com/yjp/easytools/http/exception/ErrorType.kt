package com.yjp.easytools.http.exception

/**
 * $
 * @author yjp
 * @date 2020/4/1 14:21
 */
object ErrorType {
    /**
     * 请求成功
     */
    var SUCCESS = 200
    /**
     * Token认证失败
     */
    var LOGIN_TIME_OUT = 401
    /**
     * 未知错误
     */
    var UNKONW = 1000
    /**
     * 解析错误
     */
    var PARSE_ERROR = 1001
    /**
     * 网络错误
     */
    var NETWORK_ERROR = 1002
    /**
     * HTTP请求错误
     */
    var HTTP_ERROR = 1003
    /**
     * 解析对象为空
     */
    var EMPTY_BEAN = 1004
    /**
     * 无法连接服务器
     */
    var FAILED_CONNECT = 1005
}