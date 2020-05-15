package com.yjp.mydemo.ui.login

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import com.yjp.easytools.base.BaseActivity
import com.yjp.easytools.utils.ActivityManager
import com.yjp.easytools.utils.ToastUtils
import com.yjp.mydemo.BR
import com.yjp.mydemo.R
import com.yjp.mydemo.databinding.ActivityLoginBinding

/**
 *
 * @author yjp
 * @date 2020-05-11 23:41:52
 */
class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>() {

    private var oldTime = 0L

    override fun initContentView(saveInstanceState: Bundle?): Int {
        return R.layout.activity_login
    }

    override fun initVariableId(): Int {
        return BR.loginViewModel
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - oldTime < 2000) {
                ActivityManager.instance.AppExit()
            } else {
                ToastUtils.showShort("再次点击退出程序")
                oldTime = System.currentTimeMillis()
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}