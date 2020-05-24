package com.yjp.easytools.databing.viewadapter.scrollview

/**
 * $
 *
 * @author yjp
 * @date 2020-05-16 13:48
 */
data class ScrollDataWrapper(var scrollX:Float,var scrollY:Float)
data class NestScrollDataWrapper(
    var scrollX: Int,
    var scrollY: Int,
    var oldScrollX: Int,
    var oldScrollY: Int
)