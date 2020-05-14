package com.yjp.mydemo.ui.splash

import android.os.Bundle
import com.yjp.easytools.base.BaseActivity
import com.yjp.mydemo.BR
import com.yjp.mydemo.R
import com.yjp.mydemo.databinding.ActivitySplashBinding

/**
*
* @author yjp
* @date 2020-05-11 23:41:52
*/
class SplashActivity: BaseActivity<ActivitySplashBinding, SplashViewModel>() {
	override fun initContentView(saveInstanceState: Bundle?): Int {
		return R.layout.activity_splash
		}

	override fun initVariableId(): Int {
		return BR.splashViewModel
	}
}