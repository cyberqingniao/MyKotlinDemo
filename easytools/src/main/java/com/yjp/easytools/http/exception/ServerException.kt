package com.yjp.easytools.http.exception

/**
 * $
 * @author yjp
 * @date 2020/4/1 14:22
 */
class ServerException(var code: Int, var msg: String) : RuntimeException(msg)