package com.yjp.mydemo.ui.home.adapter

import androidx.databinding.ViewDataBinding
import com.yjp.mydemo.ui.goods.GoodItemViewModel
import com.yjp.mydemo.ui.home.HomeViewModel
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter

/**
 * $
 *
 * @author yjp
 * @date 2020-05-16 15:24
 */
class GoodsAdapter : BindingRecyclerViewAdapter<GoodItemViewModel<HomeViewModel>>() {
    override fun onBindBinding(
        binding: ViewDataBinding?,
        variableId: Int,
        layoutRes: Int,
        position: Int,
        item: GoodItemViewModel<HomeViewModel>
    ) {
        super.onBindBinding(binding, variableId, layoutRes, position, item)
    }
}