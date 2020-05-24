package com.yjp.mydemo.ui.home.adapter

import androidx.databinding.ViewDataBinding
import com.yjp.mydemo.ui.home.item.GoodsMenuItemViewModel
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter

/**
 * 商品分类适配器$
 * @author yjp
 * @date 2020/5/14 22:19
 */
class GoodsMenuAdapter : BindingRecyclerViewAdapter<GoodsMenuItemViewModel>() {
    override fun onBindBinding(
        binding: ViewDataBinding,
        variableId: Int,
        layoutRes: Int,
        position: Int,
        item: GoodsMenuItemViewModel?
    ) {
        super.onBindBinding(binding, variableId, layoutRes, position, item)
    }
}