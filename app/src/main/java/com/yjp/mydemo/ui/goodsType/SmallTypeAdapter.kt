package com.yjp.mydemo.ui.goodsType

import androidx.databinding.ViewDataBinding
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter

/**
 * 小分类适配器$
 * @author yjp
 * @date 2020/6/2 15:10
 */
class SmallTypeAdapter : BindingRecyclerViewAdapter<SmallTypeItemViewModel>() {
    override fun onBindBinding(
        binding: ViewDataBinding?,
        variableId: Int,
        layoutRes: Int,
        position: Int,
        item: SmallTypeItemViewModel?
    ) {
        super.onBindBinding(binding, variableId, layoutRes, position, item)
    }
}