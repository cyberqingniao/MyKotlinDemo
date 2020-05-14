package com.yjp.mydemo.ui.bindPhone

import android.os.Bundle
import com.yjp.easytools.base.BaseActivity
import com.yjp.mydemo.BR
import com.yjp.mydemo.R
import com.yjp.mydemo.databinding.ActivityBindPhoneBinding

/**
 *
 * @author yjp
 * @date 2020-05-11 23:48:27
 */
class BindPhoneActivity : BaseActivity<ActivityBindPhoneBinding, BindPhoneViewModel>() {
    override fun initContentView(saveInstanceState: Bundle?): Int {
        return R.layout.activity_bind_phone
    }

    override fun initVariableId(): Int {
        return BR.bindPhoneViewModel
    }
}