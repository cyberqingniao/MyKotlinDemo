package com.yjp.easytools.base

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.trello.rxlifecycle2.components.support.RxFragment
import com.yjp.easytools.dialog.LoadingDialog
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Fragment父类$
 * @author yjp
 * @date 2020/3/26 14:30
 */
open class BaseFragment<V : ViewDataBinding, VM : BaseViewModel> : RxFragment(), IBaseView {

    var binding: V? = null
    var viewModel: VM? = null
    private var viewModelId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initParam()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, initContentView(savedInstanceState), container, false)
        return binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (viewModel != null) {
            viewModel!!.removeRxBus()
        }
        if (binding != null) {
            binding!!.unbind()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //私有的初始化Databinding和ViewModel方法
        initViewDatabinding()
        //私有的ViewModel与View的契约事件回调逻辑
        registorUIChangLiveDataCallBack()
        //页面数据初始化
        initData()
        //页面事件监听方法，一般用于ViewModel层转到View层的事件注册
        initViewDatabinding()
        //注册RxBus
        viewModel!!.registerRxBus()
    }

    /**
     * 注入绑定
     */
    private fun initViewDatabinding() {
        viewModelId = initVariableId()
        viewModel = initViewModel()
        if (viewModel == null) {
            var modelClass: Class<BaseViewModel>
            var type: Type = javaClass.genericSuperclass as Type
            modelClass = if (type is ParameterizedType) {
                type.actualTypeArguments[1] as Class<BaseViewModel>
            } else {
                BaseViewModel(activity!!.application).javaClass
            }
            viewModel = createViewModel(this, modelClass) as VM
        }
        binding!!.setVariable(viewModelId, viewModel)
        lifecycle.addObserver(viewModel!!)
        viewModel!!.injectLifecycleProvider(this)
    }

    /**
     * 注册ViewModel与View的契约UI回调事件
     */
    private fun registorUIChangLiveDataCallBack() {
        viewModel!!.uc.showLoadingEvent.observe(this, Observer { msg -> showLoading(msg) })
        viewModel!!.uc.dismissLoadingDialogEvent.observe(this, Observer { this::dismissLoading })
        viewModel!!.uc.startActivityEvent.observe(this, Observer { params ->
            {
                var intent = params[BaseViewModel.ParameterField.INTENT]
                if (intent == null) {
                    var clazz = params[BaseViewModel.ParameterField.CLASS] as Class<*>
                    var bundle = params[BaseViewModel.ParameterField.BUNDLE] as Bundle?
                    startActivity(clazz, bundle)
                } else if (intent is Intent) {
                    startActivity(intent)
                }
            }
        })
    }

    private fun <T : ViewModel> createViewModel(fragment: Fragment, clazz: Class<T>): T {
        return ViewModelProviders.of(fragment).get(clazz)
    }

    open fun initViewModel(): VM? {
        return null
    }

    override fun initParam() {
    }

    open override fun initContentView(saveInstanceState: Bundle?): Int {
        return 0
    }

    open override fun initVariableId(): Int {
        return 0
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
            LoadingDialog.showLoading(context!!, msg)
        }
    }

    override fun showLoading(resId: Int) {
        showLoading(getString(resId))
    }

    override fun dismissLoading() {
        LoadingDialog.dismissLoading()
    }

    override fun startActivity(clazz: Class<*>) {
        startActivity(clazz, null)
    }

    override fun startActivity(clazz: Class<*>, bundle: Bundle?) {
        var intent = Intent(context, clazz)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
    }
}