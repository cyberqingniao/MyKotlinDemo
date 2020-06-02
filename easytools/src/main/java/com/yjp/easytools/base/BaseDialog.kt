package com.yjp.easytools.base

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.yjp.easytools.utils.Utils

/**
 * Dialog父类$
 * @author yjp
 * @date 2020/4/1 10:47
 */
abstract class BaseDialog<V : ViewDataBinding> : AppCompatDialog {
    var binding: V? = null

    constructor(context: Context) : this(context, false, null)
    constructor(
        context: Context,
        cancelable: Boolean,
        cancelListener: DialogInterface.OnCancelListener?
    ) : super(context, cancelable, cancelListener)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            initContentView(savedInstanceState),
            null, false
        )
        setContentView(binding!!.root)
        init()
    }

    override fun dismiss() {
        binding?.unbind()
        super.dismiss()
    }

    @LayoutRes
    abstract fun initContentView(savedInstanceState: Bundle?): Int;

    abstract fun init();

}