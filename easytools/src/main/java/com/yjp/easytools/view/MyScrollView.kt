package com.yjp.easytools.view

import android.content.Context
import android.util.AttributeSet
import android.widget.ScrollView

/**
 * 带滑动监听的ScrollView$
 *
 * @author yjp
 * @date 2020-05-24 18:22
 */
class MyScrollView : ScrollView {

    private var onScrollListener: OnScrollListener? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    fun setOnScrollListener(listener: OnScrollListener) {
        this.onScrollListener = listener
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        onScrollListener?.onScroll(oldt, t)
    }

    public interface OnScrollListener {
        fun onScroll(startY: Int, endY: Int)
    }
}