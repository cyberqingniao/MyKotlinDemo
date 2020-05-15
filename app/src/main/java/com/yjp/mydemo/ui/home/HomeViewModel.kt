package com.yjp.mydemo.ui.home

import android.app.Application
import androidx.databinding.ObservableList
import com.yjp.easytools.base.BaseViewModel
import com.yjp.mydemo.BR
import com.yjp.mydemo.R
import com.yjp.mydemo.ui.home.item.GoodsMenuItemViewModel
import me.tatarka.bindingcollectionadapter2.ItemBinding

/**
 *
 * @author yjp
 * @date 2020-05-11 23:41:52
 */
class HomeViewModel(application: Application) : BaseViewModel(application) {

    val menuItem: ObservableList<GoodsMenuItemViewModel>? = null
    val menuItemBinding =
        ItemBinding.of<GoodsMenuItemViewModel>(BR.goodsMenuItemViewModel, R.layout.item_home_menu)
    val menuNames = arrayOf("服装", "鞋靴", "数码", "珠宝", "箱包", "电器", "食品", "美妆", "百货", "更多")
    val menuIcons = arrayOf(
        R.mipmap.icon_qq,
        R.mipmap.icon_qq,
        R.mipmap.icon_qq,
        R.mipmap.icon_qq,
        R.mipmap.icon_qq,
        R.mipmap.icon_qq,
        R.mipmap.icon_qq,
        R.mipmap.icon_qq,
        R.mipmap.icon_qq,
        R.mipmap.icon_qq
    )
    val banner = arrayOf(
        "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=191936283,2923048863&fm=26&gp=0.jpg",
        "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=191936283,2923048863&fm=26&gp=0.jpg",
        "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=191936283,2923048863&fm=26&gp=0.jpg",
        "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=191936283,2923048863&fm=26&gp=0.jpg"
    )

}