package com.yjp.mydemo.ui.home.item

import androidx.databinding.ObservableField
import com.yjp.easytools.base.ItemViewModel
import com.yjp.mydemo.ui.home.HomeViewModel

/**
 * $
 * @author yjp
 * @date 2020/5/14 22:25
 */
class BannerItemViewModel(
    viewModel: HomeViewModel,
    public val url: ObservableField<String>,
    public val resId: ObservableField<Int>
) :
    ItemViewModel<HomeViewModel>(viewModel) {
}