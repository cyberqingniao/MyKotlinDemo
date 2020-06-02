package com.yjp.mydemo.ui.goodsType

import android.app.Application
import androidx.databinding.ObservableArrayList
import com.yjp.easytools.base.BaseViewModel
import com.yjp.mydemo.BR
import com.yjp.mydemo.R
import me.tatarka.bindingcollectionadapter2.ItemBinding

/**
 *
 * @author yjp
 * @date 2020-05-11 23:41:52
 */
class GoodsTypeViewModel(application: Application) : BaseViewModel(application) {

    val categoriesItemBinding = ItemBinding.of<CategoriesItemViewModel>(
        BR.categoriesItemViewModel,
        R.layout.item_categories
    )
    val categoriesItems = ObservableArrayList<CategoriesItemViewModel>()

    val smallTypeItemBinding=ItemBinding.of<SmallTypeItemViewModel>(BR.smallTypeItemViewModel,R.layout.item_small_type)
}