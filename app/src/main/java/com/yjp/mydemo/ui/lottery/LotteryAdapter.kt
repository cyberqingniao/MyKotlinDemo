package com.yjp.mydemo.ui.lottery

import androidx.databinding.ViewDataBinding
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter

/**
 * $
 *
 * @author yjp
 * @date 2020-05-16 15:48
 */
class LotteryAdapter : BindingRecyclerViewAdapter<LotteryItemViewModel>() {
    override fun onBindBinding(
        binding: ViewDataBinding?,
        variableId: Int,
        layoutRes: Int,
        position: Int,
        item: LotteryItemViewModel?
    ) {
        super.onBindBinding(binding, variableId, layoutRes, position, item)
    }
}
