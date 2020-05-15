package com.yjp.easytools.base

import android.app.Application
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.trello.rxlifecycle2.LifecycleProvider
import com.yjp.easytools.bus.event.SingleLiveEvent
import com.yjp.easytools.databing.command.BindingAction
import com.yjp.easytools.databing.command.BindingCommand
import java.lang.ref.WeakReference

/**
 * ViewModel父类$
 * @author yjp
 * @date 2020/3/26 10:19
 */
open class BaseViewModel(application: Application) : AndroidViewModel(application), IBaseViewModel {

    val uc: UIChangeLiveData by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { UIChangeLiveData() }

    private var lifecycle: WeakReference<LifecycleProvider<*>>? = null

    //返回事件
    var backOnClickCommand: BindingCommand<*> = BindingCommand<Any>(object : BindingAction {
        override fun call() {
            finish()
        }
    })

    /**
     * 注入RxLifecycle生命周期
     * @param lifecycleProvider
     */
    fun injectLifecycleProvider(lifecycleProvider: LifecycleProvider<*>) {
        this.lifecycle = WeakReference(lifecycleProvider)
    }

    /**
     * 获取RxLifecycle生命周期
     * @return LifecycleProvider<Any>
     */
    fun getLifecycleProvider(): LifecycleProvider<*> {
        return lifecycle!!.get()!!
    }

    /**
     * 显示加载弹窗
     */
    fun showLoading() {
        showLoading("请稍后...")
    }

    /**
     * 显示加载弹窗
     * @param msg : 显示文字
     */
    fun showLoading(msg: String) {
        uc.showLoadingEvent.postValue(msg)
    }

    /**
     * 关闭加载弹窗
     */
    fun dismissLoading() {
        uc.dismissLoadingDialogEvent.call()
    }

    /**
     * 跳转新的界面
     * @param clazz ： Activity
     */
    fun startActivity(clazz: Class<*>) {
        startActivity(clazz, null)
    }

    /**
     * 跳转新的界面
     * @param intent
     */
    fun startActivity(intent: Intent) {
        val params = mutableMapOf<String, Any>()
        params[ParameterField.INTENT] = intent
        uc.startActivityEvent.postValue(params)
    }

    /**
     * 跳转新的界面
     * @param clazz ： Activity
     * @param bundle : 携带参数
     */
    fun startActivity(clazz: Class<*>, bundle: Bundle?) {
        val params = mutableMapOf<String, Any>()
        params[ParameterField.CLASS] = clazz
        if (bundle != null) {
            params[ParameterField.BUNDLE] = bundle
        }
        uc.startActivityEvent.postValue(params)
    }

    /**
     * 关闭界面
     */
    fun finish() {
        uc.finishEvent.call()
    }

    override fun onAny(owner: LifecycleOwner, event: Lifecycle.Event) {
    }

    override fun onCreate() {
    }

    override fun onStart() {
    }

    override fun onResume() {
    }

    override fun onPause() {
    }

    override fun onStop() {
    }

    override fun onDestroy() {
    }

    class UIChangeLiveData {
        val showLoadingEvent: SingleLiveEvent<String> by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { SingleLiveEvent<String>() }
        val dismissLoadingDialogEvent: SingleLiveEvent<Void> by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { SingleLiveEvent<Void>() }
        val startActivityEvent: SingleLiveEvent<Map<String, *>> by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { SingleLiveEvent<Map<String, *>>() }
        val finishEvent: SingleLiveEvent<Void> by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { SingleLiveEvent<Void>() }
    }

    object ParameterField {
        const val CLASS = "CLASS"
        const val BUNDLE = "BUNDLE"
        const val INTENT = "INTENT"
    }

}