package com.yjp.easytools.view

import android.content.Context
import android.util.AttributeSet
import androidx.viewpager.widget.ViewPager

/**
 * 自适应高度ViewPager$
 * @author yjp
 * @date 2020/4/1 8:50
 */
class ContentViewPager : ViewPager {
    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!,
        attrs
    ) {
    }

    override fun onMeasure(widthMeasureSpec: Int, hms: Int) {
        var heightMeasureSpec: Int = hms
        var height = 0
        //下面遍历所有child的高度
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))
            val h = child.measuredHeight
            if (h > height) //采用最大的view的高度。
                height = h
        }
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(
            height,
            MeasureSpec.EXACTLY
        )
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}