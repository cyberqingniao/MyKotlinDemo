package com.yjp.easytools.databing.viewadapter.imageview

import android.text.TextUtils
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

/**
 * $
 * @author yjp
 * @date 2020/3/26 15:34
 */
object ViewAdapter {

    @BindingAdapter(value = ["url", "placeholderRes"], requireAll = false)
    fun setImageUri(imageView: ImageView, url: String, placeholderRes: Int) {
        if (!TextUtils.isEmpty(url)) {
            Glide.with(imageView.context)
                .load(url)
                .apply(RequestOptions().placeholder(placeholderRes))
                .into(imageView)
        }
    }
}