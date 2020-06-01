package com.yjp.mydemo.http

import com.yjp.easytools.http.BaseApi
import com.yjp.mydemo.entity.LotteryEntity
import io.reactivex.Observable
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Url

/**
 * API$
 * @author yjp
 * @date 2020-05-11 23:41:52
 */
interface ApiService : BaseApi {

    @POST
    fun getLotteryHistory(
        @Url url: String,
        @Query("lottery_id") lottery_id: String,
        @Query("key") key: String,
        @Query("page_size") page_size: Int,
        @Query("page") page: Int
    ): Observable<AggregateData<LotteryEntity>>

    @POST
    fun getTrans(
        @Url url: String,
        @Query("q") q: String,
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("appid") appid: String,
        @Query("salt") salt: String,
        @Query("sign") sign: String
    ): Observable<AggregateData<LotteryEntity>>
}