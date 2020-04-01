package com.yjp.easytools.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText


/**
 * 带清除的EditText$
 * @author yjp
 * @date 2020/4/1 8:55
 */
class ClearEditText(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : AppCompatEditText(
    context,
    attrs,
    defStyleAttr
), TextWatcher, View.OnFocusChangeListener {

    private var mClearDrawable: Drawable? = null
    private var hasFocus: Boolean = false
    private var mTextWatcher: TextWatcher? = null
    private var mOnFocusChangeListener: OnFocusChangeListener? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(
        context,
        attrs,
        android.R.attr.editTextStyle
    )

    init {
        hasFocus = hasFocus()
        //获取图标
        mClearDrawable = compoundDrawables[2]
        if (mClearDrawable == null) {
            mClearDrawable = compoundDrawablesRelative[2]
        }
        if (mClearDrawable != null) {
            mClearDrawable?.setBounds(
                0,
                0,
                mClearDrawable!!.minimumWidth,
                mClearDrawable!!.minimumHeight
            )
        }
        //默认设置隐藏图标
        setClearIconVisible(false);
        //设置焦点改变的监听
        onFocusChangeListener = this;
        //设置输入框里面内容发生改变的监听
        addTextChangedListener(this);
    }

    /**
     * 用记住我们按下的位置来模拟点击事件
     * 当我们按下的位置在 EditText的宽度 - 文本右边到图标左边缘的距离 - 图标左边缘至控件右边缘的距离
     * 到 EditText的右边缘 之间就算点击了图标，竖直方向就以 EditText高度为边界
     */
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP && mClearDrawable != null) { //getTotalPaddingRight()图标左边缘至控件右边缘的距离
            //getCompoundDrawablePadding()表示从文本右边到图标左边缘的距离
            val left = width - totalPaddingRight - compoundDrawablePadding
            val touchable = event.x > left && event.x < width
            if (touchable) {
                this.setText("")
            }
        }
        return super.onTouchEvent(event)
    }

    /**
     * 当输入框里面内容发生变化的时候：
     * 有焦点并且输入的文本内容不为空时则显示清除按钮
     */
    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        setClearIconVisible(hasFocus && text?.length!! > 0);
        mTextWatcher?.onTextChanged(text, start, lengthBefore, lengthAfter);
    }

    override fun afterTextChanged(s: Editable?) {
        mTextWatcher?.afterTextChanged(s);
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        mTextWatcher?.beforeTextChanged(s, start, count, after);
    }

    /**
     * 当ClearEditText焦点发生变化的时候：
     * 有焦点并且输入的文本内容不为空时则显示清除按钮
     */
    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        this.hasFocus = hasFocus;
        setClearIconVisible(hasFocus && !TextUtils.isEmpty(text));
        if (mOnFocusChangeListener != null) {
            mOnFocusChangeListener!!.onFocusChange(v, hasFocus);
        }
    }

    /**
     * 设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
     * @param visible
     */
    private fun setClearIconVisible(visible: Boolean) {
        if (mClearDrawable == null) return
        val right = if (visible) mClearDrawable else null
        setCompoundDrawables(
            compoundDrawables[0],
            compoundDrawables[1], right, compoundDrawables[3]
        )
    }

    /**
     * 1、是自己本身的话则调用父类的实现，
     * 2、是外部设置的就自己处理回调回去
     */
    override fun setOnFocusChangeListener(l: OnFocusChangeListener) {
        if (l is ClearEditText) {
            super.setOnFocusChangeListener(l)
        } else {
            mOnFocusChangeListener = l
        }
    }

    override fun addTextChangedListener(watcher: TextWatcher) {
        if (watcher is ClearEditText) {
            super.addTextChangedListener(watcher)
        } else {
            mTextWatcher = watcher
        }
    }
}