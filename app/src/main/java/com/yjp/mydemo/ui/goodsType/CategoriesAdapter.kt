package com.yjp.mydemo.ui.goodsType

import androidx.databinding.ViewDataBinding
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter

/**
 * 大分类适配器$
 * @author yjp
 * @date 2020/6/2 15:08
 */
class CategoriesAdapter : BindingRecyclerViewAdapter<CategoriesItemViewModel>() {
    override fun onBindBinding(
        binding: ViewDataBinding?,
        variableId: Int,
        layoutRes: Int,
        position: Int,
        item: CategoriesItemViewModel?
    ) {
        super.onBindBinding(binding, variableId, layoutRes, position, item)
    }
}