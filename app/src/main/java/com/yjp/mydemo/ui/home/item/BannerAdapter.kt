package com.yjp.mydemo.ui.home.item

import androidx.databinding.ViewDataBinding
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter
import me.tatarka.bindingcollectionadapter2.BindingViewPagerAdapter

/**
 * $
 * @author yjp
 * @date 2020/5/14 22:25
 */
class BannerAdapter : BindingViewPagerAdapter<BannerItemViewModel>() {
    override fun onBindBinding(
        binding: ViewDataBinding?,
        variableId: Int,
        layoutRes: Int,
        position: Int,
        item: BannerItemViewModel?
    ) {
        super.onBindBinding(binding, variableId, layoutRes, position, item)
    }
}