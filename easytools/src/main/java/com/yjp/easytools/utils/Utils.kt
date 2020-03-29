package com.yjp.easytools.utils

import android.content.Context

/**
 * 通用工具包$
 * @author yjp
 * @date 2020/3/27 19:39
 */
object Utils {
    /**
     * dp转px
     */
    fun dp2px(context: Context, value: Float): Int {
        var scale = context.resources.displayMetrics.density
        return (value * (scale + 0.5f)).toInt()
    }
}