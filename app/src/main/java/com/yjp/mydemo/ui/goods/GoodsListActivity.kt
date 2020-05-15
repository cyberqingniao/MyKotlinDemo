package com.yjp.mydemo.ui.goods

import android.os.Bundle
import com.yjp.easytools.base.BaseActivity
import com.yjp.mydemo.BR
import com.yjp.mydemo.R
import com.yjp.mydemo.databinding.ActivityGoodsListBinding

/**
 * 商品列表$
 *
 * @author yjp
 * @date 2020-05-15 21:50
 */
class GoodsListActivity : BaseActivity<ActivityGoodsListBinding, GoodsListViewModel>() {
    override fun initContentView(saveInstanceState: Bundle?): Int {
        return R.layout.activity_goods_list
    }

    override fun initVariableId(): Int {
        return BR.goodsViewModel
    }
}