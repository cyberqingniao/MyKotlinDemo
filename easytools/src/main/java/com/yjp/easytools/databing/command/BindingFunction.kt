package com.yjp.easytools.databing.command

/**
 * $
 * @author yjp
 * @date 2020/3/26 15:02
 */
interface BindingFunction<T> {
    fun call(): T?
}