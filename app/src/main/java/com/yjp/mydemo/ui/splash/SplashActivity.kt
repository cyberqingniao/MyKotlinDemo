package com.yjp.mydemo.ui.splash

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import com.tbruyelle.rxpermissions2.RxPermissions
import com.yjp.easytools.base.BaseActivity
import com.yjp.easytools.utils.ToastUtils
import com.yjp.mydemo.BR
import com.yjp.mydemo.R
import com.yjp.mydemo.databinding.ActivitySplashBinding

/**
 *
 * @author yjp
 * @date 2020-05-11 23:41:52
 */
class SplashActivity : BaseActivity<ActivitySplashBinding, SplashViewModel>() {
    override fun initContentView(saveInstanceState: Bundle?): Int {
        return R.layout.activity_splash
    }

    override fun initVariableId(): Int {
        return BR.splashViewModel
    }

    @SuppressLint("CheckResult")
    override fun initData() {
        super.initData()
        val rp = RxPermissions(this)
        rp.request(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.INTERNET,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.MODIFY_AUDIO_SETTINGS,
            Manifest.permission.WRITE_SETTINGS,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_MULTICAST_STATE
        ).subscribe {
            if (it) {
                ToastUtils.showShort("获取到了所有权限")
            } else {
                ToastUtils.showShort("未授权权限，部分功能将无法正常使用")
            }
            viewModel?.openTimer()
        }
    }
}