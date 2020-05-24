package com.yjp.mydemo.ui.home.item

import androidx.databinding.ObservableField
import com.yjp.easytools.base.ItemViewModel
import com.yjp.easytools.databing.command.BindingAction
import com.yjp.easytools.databing.command.BindingCommand
import com.yjp.mydemo.ui.goods.GoodsListActivity
import com.yjp.mydemo.ui.home.HomeViewModel

/**
 * 商品分类$
 * @author yjp
 * @date 2020/5/14 22:19
 */
class GoodsMenuItemViewModel(
    viewModel: HomeViewModel,
    icon: Int,
    name: String
) : ItemViewModel<HomeViewModel>(viewModel) {
    val icon = ObservableField<Int>(icon)
    val name = ObservableField<String>(name)

    val itemOnClickCommand = BindingCommand<Any>(object : BindingAction {
        override fun call() {
            viewModel.startActivity(GoodsListActivity::class.java)
        }

    })
}