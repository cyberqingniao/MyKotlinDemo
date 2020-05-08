package com.yjp.easytools.databing.viewadapter.recyclerview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.yjp.easytools.utils.Utils

/**
 * $
 * @author yjp
 * @date 2020/3/26 15:59
 */
class DividerLine : RecyclerView.ItemDecoration {
    companion object {
        private const val DEFAULT_DIVIDER_SIZE = 1
    }

    private var ATTRS: IntArray = IntArray(android.R.attr.listDivider)
    private var dividerDrawable: Drawable? = null
    private var mContext: Context? = null
    var dividerSize: Int = 0
        get() = field
        set(value) {
            field = value
        }

    var mMode: LineDrawModel? = null
        get() = field
        set(value) {
            field = value
        }

    enum class LineDrawModel {
        HORIZONTAL, VERTICAL, BOTH
    }

    constructor(context: Context) {
        this.mContext = context
        var attrArray = context.obtainStyledAttributes(ATTRS)
        this.dividerDrawable = attrArray.getDrawable(0)
        attrArray.recycle()
    }

    constructor(context: Context, mode: LineDrawModel) : this(context) {
        this.mMode = mode
    }

    constructor(context: Context, dividerSize: Int, mode: LineDrawModel) : this(context, mode) {
        this.dividerSize = dividerSize
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        if (mMode == null) {
            throw IllegalStateException("assign LineDrawMode,please!")
        }
        when (mMode) {
            DividerLine.LineDrawModel.VERTICAL -> drawVertical(c, parent, state)
            DividerLine.LineDrawModel.HORIZONTAL -> drawHorizontal(c, parent, state)
            DividerLine.LineDrawModel.BOTH -> {
                drawHorizontal(c, parent, state)
                drawVertical(c, parent, state)
            }
        }
    }

    private fun drawVertical(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val childCount: Int = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top = child.top - params.topMargin
            val bottom = child.bottom - params.bottomMargin
            val left = child.right - params.rightMargin
            val right = if (dividerSize == 0) {
                left + Utils.dp2px(DEFAULT_DIVIDER_SIZE.toFloat())
            } else {
                left + dividerSize
            }
            dividerDrawable?.bounds!!.set(left, top, right, bottom)
            dividerDrawable?.draw(c)
        }
    }

    private fun drawHorizontal(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        var childCount = parent.childCount
        for (i in 0 until childCount) {
            val child: View = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val left = child.left - params.leftMargin
            val top = child.bottom - params.topMargin
            val right = child.right - params.rightMargin
            val bottom = if (dividerSize == 0) {
                top + Utils.dp2px(DEFAULT_DIVIDER_SIZE.toFloat())
            } else {
                top + dividerSize
            }
            dividerDrawable?.bounds?.set(left, top, right, bottom)
            dividerDrawable?.draw(c)
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
    }


}