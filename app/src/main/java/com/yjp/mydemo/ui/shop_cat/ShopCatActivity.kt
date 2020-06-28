package com.yjp.mydemo.ui.shop_cat

import android.os.Bundle
import com.yjp.easytools.base.BaseActivity
import com.yjp.mydemo.BR
import com.yjp.mydemo.R
import com.yjp.mydemo.databinding.ActivityShopCatBinding

/**
 * 购物车
 * @author yjp
 * @date 2020-06-28 17:23:05
 */
class ShopCatActivity: BaseActivity<ActivityShopCatBinding, ShopCatViewModel>() {

    override fun initContentView(saveInstanceState: Bundle?): Int {
        return R.layout.activity_shop_cat
    }

    override fun initVariableId(): Int {
        return BR.shopCatViewModel
    }
}
