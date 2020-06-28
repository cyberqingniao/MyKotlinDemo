package com.yjp.mydemo.ui.order_details

import android.os.Bundle
import com.yjp.easytools.base.BaseActivity
import com.yjp.mydemo.BR
import com.yjp.mydemo.R
import com.yjp.mydemo.databinding.ActivityOrderDetailsBinding

/**
*
* @author yjp
* @date 2020-06-24 13:55:39
*/
class OrderDetailsActivity: BaseActivity<ActivityOrderDetailsBinding, OrderDetailsViewModel>() {
	override fun initContentView(saveInstanceState: Bundle?): Int {
		return R.layout.activity_order_details
		}

	override fun initVariableId(): Int {
		return BR.orderDetailsViewModel
	}
}