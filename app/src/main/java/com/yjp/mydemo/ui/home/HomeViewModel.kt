package com.yjp.mydemo.ui.home

import android.app.Application
import androidx.databinding.ObservableArrayList
import com.yjp.easytools.base.BaseViewModel
import com.yjp.easytools.databing.command.BindingCommand
import com.yjp.easytools.databing.command.BindingConsumer
import com.yjp.easytools.utils.ToastUtils
import com.yjp.mydemo.BR
import com.yjp.mydemo.R
import com.yjp.mydemo.ui.home.item.BannerItemViewModel
import com.yjp.mydemo.ui.home.item.GoodsMenuItemViewModel
import me.tatarka.bindingcollectionadapter2.BindingViewPagerAdapter.PageTitles
import me.tatarka.bindingcollectionadapter2.ItemBinding

/**
 *
 * @author yjp
 * @date 2020-05-11 23:41:52
 */
class HomeViewModel(application: Application) : BaseViewModel(application) {

    val menuItems = ObservableArrayList<GoodsMenuItemViewModel>()
    val menuItemBinding =
        ItemBinding.of<GoodsMenuItemViewModel>(BR.goodsMenuItemViewModel, R.layout.item_home_menu)
    val bannerItems = ObservableArrayList<BannerItemViewModel>()
    val bannerItemBinding =
        ItemBinding.of<BannerItemViewModel>(BR.bannerItemViewModel, R.layout.item_home_banner)
    private val menuNames = arrayOf("服装", "鞋靴", "数码", "珠宝", "箱包", "电器", "食品", "美妆", "百货", "更多")
    private val menuIcons = arrayOf(
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

    //给ViewPager添加PageTitle
    val pageTitles =
        PageTitles<BannerItemViewModel> { position, item -> "${position}/${banner.size}" }

    //ViewPager切换监听
    var onPageSelectedCommand =
        BindingCommand(object : BindingConsumer<Int?> {
            override fun call(t: Int?) {
                ToastUtils.showShort("ViewPager切换：$t")
            }
        })

    override fun onCreate() {
        super.onCreate()
        for (i in menuNames.indices) {
            menuItems.add(GoodsMenuItemViewModel(this, menuIcons[i], menuNames[i]))
        }
        for (i in banner) {
            bannerItems.add(BannerItemViewModel(this, i, R.mipmap.ic_launcher))
        }
    }

}