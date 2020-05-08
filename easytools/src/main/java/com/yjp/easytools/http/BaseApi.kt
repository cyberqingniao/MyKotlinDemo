package com.yjp.easytools.http

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

/**
 * $
 * @author yjp
 * @date 2020/4/9 15:43
 */
interface BaseApi {
    /**
     * 下载
     */
    @Streaming
    @GET
    fun download(@Url url: String): Observable<ResponseBody>
}