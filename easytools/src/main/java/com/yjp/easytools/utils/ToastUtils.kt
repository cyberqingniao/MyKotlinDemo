package com.yjp.easytools.utils

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import java.lang.ref.WeakReference

/**
 * 提示消息$
 *
 * @author yjp
 * @date 2020-03-29 16:23
 */
object ToastUtils {
    //默认颜色
    private const val DEFAULT_COLOR = 0X12000000
    //显示位置
    private var gravity = Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM
    //X轴偏移量
    private var xOffset: Int = 0
    //Y轴偏移量
    private var yOffset: Int = 0
    //背景颜色
    private var backgroundColor = DEFAULT_COLOR
    //背景资源
    private var bgResource = -1
    //文字颜色
    private var messageColor = DEFAULT_COLOR
    //View
    private var sViewWeakReference: WeakReference<View>? = null
    //线程安全显示
    private val sHandler: Handler = Handler(Looper.getMainLooper())
    //Toast
    private var mToast: Toast? = null

    //初始化
    init {
        this.yOffset =
            (64 * (Utils.context?.resources?.displayMetrics?.density?.plus(0.5)!!)).toInt()
    }

    /**
     * 设置显示位置
     */
    fun setGravity(gravity: Int, xOffset: Int, yOffset: Int) {
        this.gravity = gravity
        this.xOffset = xOffset
        this.yOffset = yOffset
    }

    /**
     * 设置ToastView
     */
    fun setView(@LayoutRes layoutId: Int) {
        val inflate: LayoutInflater =
            Utils.context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        sViewWeakReference = WeakReference<View>(inflate.inflate(layoutId, null))

    }

    /**
     * 设置ToastView
     */
    fun setView(view: View) {
        sViewWeakReference = WeakReference<View>(view)
    }

    /**
     * 获得ToastView
     */
    fun getView(): View? {
        return if (sViewWeakReference != null) {
            sViewWeakReference?.get()
        } else {
            mToast?.view
        }
    }

    /**
     * 设置Toast背景颜色
     */
    fun setBackgroundColor(@ColorInt color: Int) {
        this.backgroundColor = color
    }

    /**
     * 设置Toast背景资源
     */
    fun setBackgroundResource(@DrawableRes resId: Int) {
        this.bgResource = resId
    }

    /**
     * 设置Toast消息颜色
     */
    fun setMessageColor(@ColorInt color: Int) {
        this.messageColor = color
    }

    /**
     * 安全的显示短Toast
     */
    fun showShortSafe(text: CharSequence) {
        sHandler.post {
            showToast(text, Toast.LENGTH_SHORT)
        }
    }

    /**
     * 安全的显示短Toast
     */
    fun showShortSafe(@StringRes resId: Int) {
        sHandler.post {
            show(resId, Toast.LENGTH_SHORT)
        }
    }

    /**
     * 安全的显示短Toast
     */
    fun showShortSafe(@StringRes resId: Int, vararg args: Any) {
        sHandler.post {
            show(resId, Toast.LENGTH_SHORT, args)
        }
    }

    /**
     * 安全的显示短Toast
     */
    fun showShortSafe(format: String, vararg args: Any) {
        sHandler.post {
            show(format, Toast.LENGTH_SHORT, args)
        }
    }

    /**
     * 安全的显示长Toast
     */
    fun showLongSafe(text: CharSequence) {
        sHandler.post {
            showToast(text, Toast.LENGTH_LONG)
        }
    }

    /**
     * 安全的显示长Toast
     */
    fun showLongSafe(@StringRes resId: Int) {
        sHandler.post {
            show(resId, Toast.LENGTH_LONG)
        }
    }

    /**
     * 安全的显示长Toast
     */
    fun showLongSafe(@StringRes resId: Int, vararg args: Any) {
        sHandler.post {
            show(resId, Toast.LENGTH_LONG, args)
        }
    }

    /**
     * 安全的显示长Toast
     */
    fun showLongSafe(format: String, vararg args: Any) {
        sHandler.post {
            show(format, Toast.LENGTH_LONG, args)
        }
    }

    /**
     * 显示短Toast
     */
    fun showShort(text: CharSequence) {
        showToast(text, Toast.LENGTH_SHORT)
    }

    /**
     * 显示短Toast
     */
    fun showShort(@StringRes resId: Int) {
        show(resId, Toast.LENGTH_SHORT)
    }

    /**
     * 显示短Toast
     */
    fun showShort(@StringRes resId: Int, vararg args: Any) {
        show(resId, Toast.LENGTH_SHORT, args)
    }

    /**
     * 显示短Toast
     */
    fun showShort(format: String, vararg args: Any) {
        show(format, Toast.LENGTH_SHORT, args)
    }

    /**
     * 显示长Toast
     */
    fun showLong(text: CharSequence) {
        showToast(text, Toast.LENGTH_LONG)
    }

    /**
     * 显示长Toast
     */
    fun showLong(@StringRes resId: Int) {
        show(resId, Toast.LENGTH_LONG)
    }

    /**
     * 显示长Toast
     */
    fun showLong(@StringRes resId: Int, vararg args: Any) {
        show(resId, Toast.LENGTH_LONG, args)
    }

    /**
     * 显示长Toast
     */
    fun showLong(format: String, vararg args: Any) {
        show(format, Toast.LENGTH_LONG, args)
    }

    /**
     * 显示Toast
     */
    fun show(@StringRes resId: Int, duration: Int) {
        showToast(Utils.context?.resources?.getText(resId).toString(), duration)
    }

    /**
     * 显示Toast
     */
    fun show(@StringRes resId: Int, duration: Int, vararg args: Any) {
        showToast(String.format(Utils.context?.resources?.getString(resId)!!, args), duration)
    }

    /**
     * 显示Toast
     */
    fun show(format: String, duration: Int, vararg args: Any) {
        showToast(String.format(format, args), duration)
    }

    /**
     * 显示Toast
     */
    private fun showToast(text: CharSequence, duration: Int) {
        cancel()
        var isCustom: Boolean = false
        if (sViewWeakReference != null) {
            val view = sViewWeakReference?.get()
            if (view != null) {
                mToast = Toast(Utils.context)
                mToast?.view = view
                mToast?.duration = duration
                isCustom = true
            }
        }
        if (!isCustom) {
            mToast = if (messageColor != DEFAULT_COLOR) {
                val spannableString = SpannableString(text)
                val colorSpan = ForegroundColorSpan(messageColor)
                spannableString.setSpan(
                    colorSpan,
                    0,
                    spannableString.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                Toast.makeText(Utils.context, spannableString, duration)
            } else {
                Toast.makeText(Utils.context, text, duration)
            }
        }
        val view = mToast?.view
        if (bgResource != -1) {
            view?.setBackgroundResource(bgResource)
        } else if (backgroundColor != DEFAULT_COLOR) {
            view?.setBackgroundColor(backgroundColor)
        }
        mToast?.setGravity(gravity, xOffset, yOffset)
        mToast?.show()
    }

    /**
     * 取消Toast
     */
    fun cancel() {
        mToast?.cancel()
        mToast = null
    }
}