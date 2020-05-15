package com.yjp.mydemo.ui.goods

import androidx.databinding.ViewDataBinding
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter

/**
 * $
 *
 * @author yjp
 * @date 2020-05-15 22:04
 */
class GoodsAdapter : BindingRecyclerViewAdapter<GoodItemViewModel<GoodsListViewModel>>() {
    override fun onBindBinding(
        binding: ViewDataBinding?,
        variableId: Int,
        layoutRes: Int,
        position: Int,
        item: GoodItemViewModel<GoodsListViewModel>
    ) {
        super.onBindBinding(binding, variableId, layoutRes, position, item)
    }
}