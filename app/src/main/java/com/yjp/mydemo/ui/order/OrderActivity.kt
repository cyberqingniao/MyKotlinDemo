package com.yjp.mydemo.ui.order

import android.os.Bundle
import com.yjp.easytools.base.BaseActivity
import com.yjp.mydemo.BR
import com.yjp.mydemo.R
import com.yjp.mydemo.databinding.ActivityOrderBinding

/**
 *
 * @author yjp
 * @date 2020-06-24 13:41:47
 */
class OrderActivity: BaseActivity<ActivityOrderBinding, OrderViewModel>() {
	override fun initContentView(saveInstanceState: Bundle?): Int {
		return R.layout.activity_order
	}

	override fun initVariableId(): Int {
		return BR.orderViewModel
	}
}