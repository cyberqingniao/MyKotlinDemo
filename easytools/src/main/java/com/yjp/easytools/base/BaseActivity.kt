package com.yjp.easytools.base

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.yjp.easytools.dialog.LoadingDialog
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Activity父类$
 * @author yjp
 * @date 2020/3/26 13:30
 */
abstract class BaseActivity<V : ViewDataBinding, VM : BaseViewModel> : RxAppCompatActivity(),
    IBaseView {

    var binding: V? = null
    var viewModel: VM? = null
    private var viewModelId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //页面接收的参数方法
        initParam()
        //私有的初始化Databinding和ViewModel方法
        initViewDatabinding(savedInstanceState)
        //私有的ViewModel与View的契约事件回调逻辑
        registerUIChangeLiveDataCallBack()
        //页面数据初始化方法
        initData()
        //页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
        initViewObservable()
        //注册RxBus
        viewModel!!.registerRxBus()
    }

    override fun onDestroy() {
        super.onDestroy()
        //解除Messenger注册
        if (viewModel != null) {
            viewModel!!.removeRxBus()
        }
        if (binding != null) {
            binding!!.unbind()
        }
    }

    /**
     * 注入绑定
     */
    private fun initViewDatabinding(savedInstanceState: Bundle?) {
        //DataBindingUtil类需要在project的build中配置 dataBinding {enabled true }, 同步后会自动关联android.databinding包
        binding = DataBindingUtil.setContentView(this, initContentView(savedInstanceState))
        //获取ViewModel Id
        viewModelId = initVariableId()
        //获取ViewModel,子类如果实现并有返回ViewModel后将不在初始花ViewModel
        viewModel = initViewModel()
        if (viewModel == null) {
            var modelClass: Class<BaseViewModel>
            var type: Type? = javaClass.genericSuperclass
            modelClass = if (type is ParameterizedType) {
                type.actualTypeArguments[1] as Class<BaseViewModel>
            } else {
                //如果没有指定泛型参数，则默认使用BaseViewModel
                BaseViewModel(application).javaClass
            }
            viewModel = createViewModel(this, modelClass) as VM
        }
        //关联ViewModel
        binding!!.setVariable(viewModelId, viewModel)
        //让ViewModel拥有View的生命周期感应
        lifecycle.addObserver(viewModel!!)
        //注入RxLifecycle生命周期
        viewModel!!.injectLifecycleProvider(this)
    }

    /**
     * 注册ViewModel与View的契约UI回调事件
     */
    private fun registerUIChangeLiveDataCallBack() {
        //加载对话框显示
        viewModel!!.uc.showLoadingEvent.observe(this, Observer { msg: String -> showLoading(msg) })
        //加载对话框消失
        viewModel!!.uc.dismissLoadingDialogEvent.observe(this, Observer { this::dismissLoading })
        //跳入新页面
        viewModel!!.uc.startActivityEvent.observe(this, Observer { params ->
            run {
                var intent = params[BaseViewModel.ParameterField.INTENT]
                if (intent == null) {
                    var clazz: Class<*> = params[BaseViewModel.ParameterField.CLASS] as Class<*>
                    var bundle: Bundle? = params[BaseViewModel.ParameterField.BUNDLE] as Bundle
                    startActivity(clazz, bundle)
                } else if (intent is Intent) {
                    startActivity(intent)
                }
            }
        })
        viewModel!!.uc.finishEvent.observe(this, Observer { this::finish })
    }

    /**
     * 创建ViewModel
     */
    fun <T : ViewModel> createViewModel(activity: FragmentActivity, clazz: Class<T>): T {
        return ViewModelProviders.of(activity).get(clazz)
    }

    open fun initViewModel(): VM? {
        return null
    }

    /**
     * 刷新布局
     */
    fun refreshLayout() {
        if (viewModel != null) {
            binding!!.setVariable(viewModelId, viewModel)
        }
    }


    override fun initParam() {
    }

    override fun initData() {
    }

    override fun initViewObservable() {
    }

    override fun showLoading() {
        showLoading("请稍后...")
    }

    override fun showLoading(msg: String) {
        if (!LoadingDialog.isShowing()) {
            LoadingDialog.showLoading(this, msg)
        }
    }

    override fun showLoading(resId: Int) {
        if (!LoadingDialog.isShowing()) {
            LoadingDialog.showLoading(this, getString(resId))
        }
    }

    override fun dismissLoading() {
        LoadingDialog.dismissLoading()
    }

    override fun startActivity(clazz: Class<*>) {
        startActivity(clazz, null)
    }

    override fun startActivity(clazz: Class<*>, bundle: Bundle?) {
        var intent = Intent(this, clazz)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
    }

}