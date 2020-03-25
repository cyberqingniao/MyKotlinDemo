package com.yjp.easytools.base

import android.graphics.drawable.Drawable
import android.os.Bundle

/**
 * $
 * @author com.yjp
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

    /**
     * 显示加载弹窗
     */
    fun showLoading()

    /**
     * 显示加载弹窗，带提示文字
     * @param msg ： 显示文字
     */
    fun showLoading(msg: String)

    /**
     * 显示加载弹窗，带提示文字
     * @param resId : 字符串资源ID
     */
    fun showLoading(resId: Int)

    /**
     * 关闭加载弹窗
     */
    fun dismissLoading()

    /**
     * 跳转新Activity
     * @param clazz : Activity
     */
    fun startActivity(clazz: Class<*>)

    /**
     * 跳转新的activity，带参数
     * @param clazz : Activity
     * @param bundle : 需要携带的参数
     */
    fun startActivity(clazz: Class<*>, bundle: Bundle)

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