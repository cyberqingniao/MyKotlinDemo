package com.yjp.mydemo.ui.goodsDetails

import android.os.Bundle
import com.yjp.easytools.base.BaseActivity
import com.yjp.mydemo.BR
import com.yjp.mydemo.R
import com.yjp.mydemo.databinding.ActivityGoodsDetailsBinding

/**
 * 商品详情$
 *
 * @author yjp
 * @date 2020-05-24 19:18
 */
class GoodsDetailsActivity : BaseActivity<ActivityGoodsDetailsBinding, GoodsDetailsViewModel>() {
    override fun initContentView(saveInstanceState: Bundle?): Int {
        return R.layout.activity_goods_details
    }

    override fun initVariableId(): Int {
        return BR.goodsDetailsViewModel
    }

}