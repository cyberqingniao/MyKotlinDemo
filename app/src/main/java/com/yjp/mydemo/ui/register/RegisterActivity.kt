package com.yjp.mydemo.ui.register

import android.os.Bundle
import com.yjp.easytools.base.BaseActivity
import com.yjp.mydemo.BR
import com.yjp.mydemo.R
import com.yjp.mydemo.databinding.ActivityRegisterBinding

/**
*
* @author yjp
* @date 2020-05-11 23:46:20
*/
class RegisterActivity: BaseActivity<ActivityRegisterBinding, RegisterViewModel>() {
	override fun initContentView(saveInstanceState: Bundle?): Int {
		return R.layout.activity_register
		}

	override fun initVariableId(): Int {
		return BR.registerViewModel
	}
}