package com.yjp.mydemo.entity

/**
 * 彩票返回结果$
 *
 * @author yjp
 * @date 2020-05-16 16:12
 */
data class LotteryEntity(
    var lotteryResList: ArrayList<LotteryBean>,
    var page: Int,
    var pageSize: Int,
    var totalPage: Int
)

//lottery_id	string	彩票ID
//lottery_res	string	开奖结果
//lottery_no	string	开奖期号
//lottery_date	string	开奖日期
//lottery_exdate	string	兑奖截止日期
//lottery_sale_amount	string	本期销售额，可能为空
//lottery_pool_amount	string	奖池滚存，可能为空
data class LotteryBean(
    var lottery_id: String,
    var lottery_res: String,
    var lottery_no: String,
    var lottery_date: String,
    var lottery_exdate: String,
    var lottery_sale_amount: String,
    var lottery_pool_amount: String
)