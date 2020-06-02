package com.yjp.easytools.dialog

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import com.com.yjp.easytools.R
import com.com.yjp.easytools.databinding.DialogLoadingBinding
import com.yjp.easytools.base.BaseDialog
import com.yjp.easytools.utils.ActivityManager

/**
 * 加载框$
 * @author yjp
 * @date 2020/6/1 16:40
 */
class LoadingDialog : BaseDialog<DialogLoadingBinding> {

    companion object {
        private var msg: String = "请稍后..."

        private var dialog: LoadingDialog? = null

        /**
         * 初始化Dialog
         */
        private fun initDialog(): LoadingDialog {
            if (dialog == null) {
                dialog = LoadingDialog(ActivityManager.instance.currentActivity())
            }
            dialog!!.setMsg(msg)
            return dialog!!
        }

        /**
         * 显示Dialog
         */
        fun show(): LoadingDialog {
            if (initDialog().isShowing) {
                return dialog!!
            }
            dialog!!.show()
            dialog!!.window!!.decorView.tag = System.currentTimeMillis()
            return dialog!!
        }

        /**
         * 设置加载提示消息
         */
        fun show(msg: String): LoadingDialog {
            this.msg = msg
            return show()
        }

        /**
         * 判断是否在运行中
         */
        fun isShowing(): Boolean {
            return dialog?.isShowing ?: false
        }

        /**
         * 关闭弹窗
         */
        fun dismiss() {
            if (dialog != null && dialog!!.window != null) {
                //判断Dialog显示时间，避免时间过短照成闪屏
                val createTimeMillis = dialog!!.window!!.decorView.tag as Long
                if (System.currentTimeMillis() - createTimeMillis < 1000) {
                    Handler().postDelayed({
                        dismiss()
                    }, 500)
                } else {
                    dialog?.dismiss()
                    dialog = null
                }
            }
        }

    }

    constructor(context: Context) : this(context, false, null)
    constructor(
        context: Context,
        cancelable: Boolean,
        cancelListener: DialogInterface.OnCancelListener?
    ) : super(context, cancelable, cancelListener)

    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.dialog_loading;
    }

    override fun init() {
        binding!!.tvLoading.text = msg
    }

    /**
     * 设置显示提示消息
     */
    fun setMsg(msg: String): LoadingDialog {
        binding?.tvLoading?.text = msg
        return this
    }

}