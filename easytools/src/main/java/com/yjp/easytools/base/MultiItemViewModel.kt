package com.yjp.easytools.base

/**
 * 多item布局$
 * @author yjp
 * @date 2020/4/1 10:17
 */
class MultiItemViewModel<VM : BaseViewModel>(viewModel: VM) : ItemViewModel<VM>(viewModel) {
    var multiType: Any? = null
        get() = field
        set(value) {
            field = value
        }
}