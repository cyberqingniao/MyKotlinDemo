package com.yjp.mydemo.ui.goodsType

import android.os.Bundle
import com.yjp.easytools.base.BaseFragment
import com.yjp.mydemo.BR
import com.yjp.mydemo.R
import com.yjp.mydemo.databinding.FragmentGoodsTypeBinding

/**
 *
 * @author yjp
 * @date 2020-05-11 23:41:52
 */
class GoodsTypeFragment : BaseFragment<FragmentGoodsTypeBinding, GoodsTypeViewModel>() {

    companion object {
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { GoodsTypeFragment() }
    }

    override fun initContentView(saveInstanceState: Bundle?): Int {
        return R.layout.fragment_goods_type
    }

    override fun initVariableId(): Int {
        return BR.goodsTypeViewModel
    }
}