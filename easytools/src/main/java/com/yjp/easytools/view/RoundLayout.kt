package com.yjp.easytools.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.com.yjp.easytools.R
import com.yjp.easytools.utils.Utils

/**
 * 可设置圆角布局$
 * @author yjp
 * @date 2020/4/1 8:32
 */
class RoundLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    RelativeLayout(context, attrs, defStyleAttr) {
    var radii =
        FloatArray(8) // top-left, top-right, bottom-right, bottom-left
    var mClipPath // 剪裁区域路径
            : Path? = null
    var mPaint // 画笔
            : Paint? = null
    var mStrokeColor // 描边颜色
            = 0
    var mStrokeWidth // 描边半径
            = 0
    var mClipBackground // 是否剪裁背景
            = false
    var mAreaRegion // 内容区域
            : Region? = null
    var mLayer // 画布图层大小
            : RectF? = null
    private var mRoundCorner // 圆角大小
            = 0

    constructor(context: Context, radius: Int) : this(context) {
        mRoundCorner = Utils.dp2px(radius.toFloat())
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mLayer!![0f, 0f, w.toFloat()] = h.toFloat()
        refreshRegion(this)
    }

    override fun dispatchDraw(canvas: Canvas) {
        canvas.saveLayer(mLayer, null, Canvas.ALL_SAVE_FLAG)
        super.dispatchDraw(canvas)
        onClipDraw(canvas)
        canvas.restore()
    }

    override fun draw(canvas: Canvas) {
        refreshRegion(this)
        if (mClipBackground) {
            canvas.save()
            canvas.clipPath(mClipPath!!)
            super.draw(canvas)
            canvas.restore()
        } else {
            super.draw(canvas)
        }
    }

    fun initAttrs(
        context: Context,
        attrs: AttributeSet?
    ) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.RoundLayout)
        mStrokeColor =
            ta.getColor(R.styleable.RoundLayout_stroke_color, Color.WHITE)
        mStrokeWidth = ta.getDimensionPixelSize(R.styleable.RoundLayout_stroke_width, 0)
        mClipBackground = ta.getBoolean(R.styleable.RoundLayout_clip_background, false)
        if (mRoundCorner == 0) {
            mRoundCorner = ta.getDimensionPixelSize(R.styleable.RoundLayout_round_corner, 4)
        }
        val roundCornerTopLeft = ta.getDimensionPixelSize(
            R.styleable.RoundLayout_round_corner_top_left, mRoundCorner
        )
        val roundCornerTopRight = ta.getDimensionPixelSize(
            R.styleable.RoundLayout_round_corner_top_right, mRoundCorner
        )
        val roundCornerBottomLeft = ta.getDimensionPixelSize(
            R.styleable.RoundLayout_round_corner_bottom_left, mRoundCorner
        )
        val roundCornerBottomRight = ta.getDimensionPixelSize(
            R.styleable.RoundLayout_round_corner_bottom_right, mRoundCorner
        )
        ta.recycle()
        radii[0] = roundCornerTopLeft.toFloat()
        radii[1] = roundCornerTopLeft.toFloat()
        radii[2] = roundCornerTopRight.toFloat()
        radii[3] = roundCornerTopRight.toFloat()
        radii[4] = roundCornerBottomRight.toFloat()
        radii[5] = roundCornerBottomRight.toFloat()
        radii[6] = roundCornerBottomLeft.toFloat()
        radii[7] = roundCornerBottomLeft.toFloat()
        mLayer = RectF()
        mClipPath = Path()
        mAreaRegion = Region()
        mPaint = Paint()
        mPaint!!.color = Color.WHITE
        mPaint!!.isAntiAlias = true
        setLayerType(View.LAYER_TYPE_HARDWARE, null)
    }

    fun refreshRegion(view: View) {
        val w = mLayer!!.width().toInt()
        val h = mLayer!!.height().toInt()
        val areas = RectF()
        areas.left = view.paddingLeft.toFloat()
        areas.top = view.paddingTop.toFloat()
        areas.right = w - view.paddingRight.toFloat()
        areas.bottom = h - view.paddingBottom.toFloat()
        mClipPath!!.reset()
        mClipPath!!.addRoundRect(areas, radii, Path.Direction.CW)
        val clip = Region(
            areas.left.toInt(), areas.top.toInt(),
            areas.right.toInt(), areas.bottom.toInt()
        )
        mAreaRegion!!.setPath(mClipPath!!, clip)
    }

    fun onClipDraw(canvas: Canvas) {
        if (mStrokeWidth > 0) { // 将与描边区域重叠的内容裁剪掉
            mPaint!!.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
            mPaint!!.color = Color.WHITE
            mPaint!!.strokeWidth = mStrokeWidth * 2.toFloat()
            mPaint!!.style = Paint.Style.STROKE
            canvas.drawPath(mClipPath!!, mPaint!!)
            // 绘制描边
            mPaint!!.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)
            mPaint!!.color = mStrokeColor
            mPaint!!.style = Paint.Style.STROKE
            canvas.drawPath(mClipPath!!, mPaint!!)
        }
        mPaint!!.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        mPaint!!.color = Color.WHITE
        mPaint!!.style = Paint.Style.FILL
        canvas.drawPath(mClipPath!!, mPaint!!)
    }

    init {
        initAttrs(context, attrs)
    }
}
