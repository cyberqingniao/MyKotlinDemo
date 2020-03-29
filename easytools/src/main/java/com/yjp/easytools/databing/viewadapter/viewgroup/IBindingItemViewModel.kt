package com.yjp.easytools.databing.viewadapter.viewgroup

import androidx.databinding.ViewDataBinding

/**
 * $
 *
 * @author yjp
 * @date 2020-03-29 14:59
 */
interface IBindingItemViewModel<V : ViewDataBinding> {
    fun injecDataBinding(binding: V)
}