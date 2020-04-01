package com.yjp.easytools.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.trello.rxlifecycle2.components.RxDialogFragment
import com.yjp.easytools.utils.Utils

/**
 * Dialog父类$
 * @author yjp
 * @date 2020/4/1 10:47
 */
open class BaseDialog<V : ViewDataBinding> : RxDialogFragment() {
    var binding: V? = null
    private var listener: OnDismissListener? = null

    override fun onCreateView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        var window = dialog.window
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding =
            DataBindingUtil.inflate(
                inflater!!,
                initContentView(savedInstanceState),
                container,
                false
            )
        return binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding?.unbind()
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    fun setMargin(dp: Float) {
        val dm = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(dm)
        dialog.window!!.setLayout(
            dm.widthPixels - Utils.dp2px(dp),
            dialog.window!!.attributes.height
        )
    }

    fun setGravity(gravity: Int) {
        dialog.window!!.setGravity(gravity)
    }

    fun show() {
        show(activity.fragmentManager, "")
    }

    override fun dismiss() {
        listener?.onDismiss()
        super.dismiss()
    }

    open fun initContentView(savedInstanceState: Bundle?): Int {
        return 0
    }

    open fun init() {}

    fun setOnDismissListener(listener: OnDismissListener) {
        this.listener = listener
    }

    interface OnDismissListener {
        fun onDismiss()
    }
}