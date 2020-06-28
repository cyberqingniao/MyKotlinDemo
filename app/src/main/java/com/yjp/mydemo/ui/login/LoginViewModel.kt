package com.yjp.mydemo.ui.login

import android.app.Application
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.yjp.easytools.base.BaseViewModel
import com.yjp.easytools.databing.command.BindingAction
import com.yjp.easytools.databing.command.BindingCommand
import com.yjp.easytools.http.transformer.CommonTransformer
import com.yjp.mydemo.entity.LoginEntity
import com.yjp.mydemo.http.CommonObserver
import com.yjp.mydemo.http.HttpHelp
import com.yjp.mydemo.ui.bindPhone.BindPhoneActivity
import com.yjp.mydemo.ui.forgetPassword.ForgetPasswordActivity
import com.yjp.mydemo.ui.main.MainActivity
import com.yjp.mydemo.ui.register.RegisterActivity
import com.yjp.mydemo.ui.setService.SetServiceActivity

/**
 *
 * @author yjp
 * @date 2020-05-08 23:33:48
 */
class LoginViewModel(application: Application) : BaseViewModel(application) {

    //用户名
    val username = ObservableField<String>("")

    //密码
    val password = ObservableField<String>("")

    //是否记住密码
    val isRememberPassword = ObservableBoolean(false)

    /**
     * 服务器设置
     */
    val setServiceOnClickCommand = BindingCommand<Any>(object : BindingAction {
        override fun call() {
            startActivity(SetServiceActivity::class.java)
        }
    })

    /**
     * 注册
     */
    val registerOnClickCommand = BindingCommand<Any>(object : BindingAction {
        override fun call() {
            startActivity(RegisterActivity::class.java)
        }

    })

    /**
     * 登录事件
     */
    val loginOnClickCommand = BindingCommand<Any>(object : BindingAction {
        override fun call() {
            login()
//            startActivity(MainActivity::class.java)
//            finish()
        }

    })

    /**
     * 忘记密码
     */
    val forgetPasswordOnClickCommand = BindingCommand<Any>(object : BindingAction {
        override fun call() {
            startActivity(ForgetPasswordActivity::class.java)
        }

    })

    /**
     * QQ登录
     */
    val qqOnClickCommand = BindingCommand<Any>(object : BindingAction {
        override fun call() {
            startActivity(BindPhoneActivity::class.java)
        }

    })

    /**
     * 微信登录
     */
    val wxOnClickCommand = BindingCommand<Any>(object : BindingAction {
        override fun call() {
            startActivity(BindPhoneActivity::class.java)
        }

    })

    private fun login() {
        HttpHelp.api().login(username.get()!!, password.get()!!)
            .compose(CommonTransformer<LoginEntity>())
            .subscribe(object : CommonObserver<LoginEntity>(true) {
                override fun onSuccess(result: LoginEntity?) {

                }
            })
    }
}