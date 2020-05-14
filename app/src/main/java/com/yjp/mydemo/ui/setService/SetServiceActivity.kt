package com.yjp.mydemo.ui.setService

import android.os.Bundle
import com.yjp.easytools.base.BaseActivity
import com.yjp.mydemo.BR
import com.yjp.mydemo.R
import com.yjp.mydemo.databinding.ActivitySetServiceBinding

/**
*
* @author yjp
* @date 2020-05-11 23:41:52
*/
class SetServiceActivity: BaseActivity<ActivitySetServiceBinding, SetServiceViewModel>() {
	override fun initContentView(saveInstanceState: Bundle?): Int {
		return R.layout.activity_set_service
		}

	override fun initVariableId(): Int {
		return BR.setServiceViewModel
	}
}