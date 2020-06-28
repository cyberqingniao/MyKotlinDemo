package com.yjp.mydemo.dialog

import android.os.Bundle
import com.yjp.easytools.base.BaseDialog
import com.yjp.easytools.utils.ActivityManager
import com.yjp.mydemo.R
import com.yjp.mydemo.databinding.DialogTipBinding

/**
*$
* @author yjp
* @date 2020-06-24 13:55:39
*/
class TipDialog: BaseDialog<DialogTipBinding>(ActivityManager.instance.currentActivity()) {

	override fun initContentView(savedInstanceState: Bundle?): Int {
		return R.layout.dialog_tip
	}

	override fun init() {

	}
}
