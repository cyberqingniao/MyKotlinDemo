package com.yjp.easytools.base

import android.graphics.drawable.Drawable
import android.os.Bundle

/**
 * $
 * @author yjp
 * @date 2020/3/25 13:53
 */
interface IBaseView {

    /**
     * 初始化界面传递参数
     */
    fun initParam()

    /**
     * 初始化根布局
     * @param saveInstanceState
     * @return 布局layout的Id
     */
    fun initContentView(saveInstanceState: Bundle): Int

    /**
     * 初始化ViewModel
     */
    fun initVariableId(): Int

    /**
     * 初始化数据
     */
    fun initData()

    /**
     * 初始化界面观察者的监听
     */
    fun initViewObservable()

    fun showLoading()

    fun showLoading(msg:String)

    fun showLoading(resId:Int)

    fun dismissLoading()

    /**
     * 隐藏软键盘
     */
    fun goneKey()

    /**
     * 图片资源转换
     * @param resId : 资源id
     * @return Drawable
     */
    fun getDrawables(resId: Int): Drawable
}