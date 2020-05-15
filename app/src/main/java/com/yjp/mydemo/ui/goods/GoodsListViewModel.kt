package com.yjp.mydemo.ui.goods

import android.app.Application
import androidx.databinding.ObservableArrayList
import com.yjp.easytools.base.BaseViewModel
import com.yjp.mydemo.BR
import com.yjp.mydemo.R
import com.yjp.mydemo.entity.GoodEntity
import me.tatarka.bindingcollectionadapter2.ItemBinding

/**
 * $
 *
 * @author yjp
 * @date 2020-05-15 21:51
 */
class GoodsListViewModel(application: Application) : BaseViewModel(application) {
    val items = ObservableArrayList<GoodItemViewModel<GoodsListViewModel>>()
    val itemBinding: ItemBinding<GoodItemViewModel<GoodsListViewModel>> = ItemBinding.of(
        BR.goodItemViewModel,
        R.layout.item_good
    )
}