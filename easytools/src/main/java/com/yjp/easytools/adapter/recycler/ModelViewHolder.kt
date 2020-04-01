package com.yjp.easytools.adapter.recycler

import android.graphics.Bitmap
import android.util.SparseArray
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

/**
 * 通用Recycler ViewHolder$
 * @author yjp
 * @date 2020/4/1 9:21
 */
class ModelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var mViewArray: SparseArray<View> = SparseArray<View>()

    /**
     * 返回ItemView
     */
    fun getItemView(): View {
        return itemView
    }

    /**
     * 通过Id得到View
     */
    fun <V : View> getViewAtId(@IdRes viewId: Int): V {
        var view = mViewArray.get(viewId)
        if (view == null) {
            view = itemView.findViewById(viewId)
            mViewArray.put(viewId, view)
        }
        return view as V
    }

    /**
     * 设置文字
     */
    fun setText(@IdRes viewId: Int, text: CharSequence): ModelViewHolder {
        val textView = getViewAtId<TextView>(viewId)
        textView.text = text
        return this
    }

    /**
     * 设置字体颜色
     */
    fun setTextColor(@IdRes viewId: Int, @ColorInt color: Int): ModelViewHolder {
        val textView = getViewAtId<TextView>(viewId)
        textView.setTextColor(color)
        return this
    }

    /**
     * 设置网络图片
     */
    fun setImageUrl(@IdRes viewId: Int, imgUrl: String): ModelViewHolder {
        val imageView = getViewAtId<ImageView>(viewId)
        Glide.with(imageView.context).load(imgUrl).into(imageView)
        return this
    }

    /**
     * 设置资源图片
     */
    fun setImageRes(@IdRes viewId: Int, resId: Int): ModelViewHolder {
        val imageView = getViewAtId<ImageView>(viewId)
        imageView.setImageResource(resId)
        return this
    }

    /**
     * 设置Bitmap图片
     */
    fun setImageBitmap(@IdRes viewId: Int, bitmap: Bitmap): ModelViewHolder {
        val imageView = getViewAtId<ImageView>(viewId)
        imageView.setImageBitmap(bitmap)
        return this
    }

    /**
     * 设置View是否可见
     */
    fun setVisibility(@IdRes viewId: Int, visibility: Int): ModelViewHolder {
        val view = getViewAtId<View>(viewId)
        view.visibility = visibility
        return this
    }

    /**
     * 设置View背景颜色
     */
    fun setBackgroundColor(@IdRes viewId: Int, @ColorInt color: Int): ModelViewHolder {
        val view = getViewAtId<View>(viewId)
        view.setBackgroundColor(color)
        return this
    }

    /**
     * 设置View背景
     */
    fun setBackground(@IdRes viewId: Int, @DrawableRes drawableId: Int): ModelViewHolder {
        val view = getViewAtId<View>(viewId)
        view.setBackgroundResource(drawableId)
        return this
    }

}