package com.yjp.mydemo.ui.home.item

import androidx.databinding.ObservableField
import com.yjp.easytools.base.ItemViewModel
import com.yjp.mydemo.ui.home.HomeViewModel

/**
 * 商品分类$
 * @author yjp
 * @date 2020/5/14 22:19
 */
class GoodsMenuItemViewModel(
    viewModel: HomeViewModel,
    public val icon: ObservableField<Int>,
    public val name: ObservableField<String>
) : ItemViewModel<HomeViewModel>(viewModel) {
}