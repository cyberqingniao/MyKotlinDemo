package com.yjp.mydemo.ui.forgetPassword

import android.os.Bundle
import com.yjp.easytools.base.BaseActivity
import com.yjp.mydemo.BR
import com.yjp.mydemo.R
import com.yjp.mydemo.databinding.ActivityForgetPasswordBinding

/**
*
* @author yjp
* @date 2020-05-11 23:42:09
*/
class ForgetPasswordActivity: BaseActivity<ActivityForgetPasswordBinding, ForgetPasswordViewModel>() {
	override fun initContentView(saveInstanceState: Bundle?): Int {
		return R.layout.activity_forget_password
		}

	override fun initVariableId(): Int {
		return BR.forgetPasswordViewModel
	}
}