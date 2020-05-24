package com.yjp.mydemo.http

/**
 * $
 *
 * @author yjp
 * @date 2020-05-16 16:10
 */
data class AggregateData<T>(
    var error_code: Int,
    var result: T,
    var reason: String,
    var resultcode: String

)