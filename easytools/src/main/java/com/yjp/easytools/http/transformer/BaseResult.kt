package com.yjp.easytools.http.transformer

/**
 * $
 * @author yjp
 * @date 2020/4/1 14:52
 */
data class BaseResult<T>(var status: Int, var message: String?, var data: T?)