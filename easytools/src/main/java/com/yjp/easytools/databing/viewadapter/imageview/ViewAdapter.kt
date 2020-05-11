package com.yjp.easytools.databing.viewadapter.imageview

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.yjp.easytools.utils.StringUtils

/**
 * $
 * @author yjp
 * @date 2020/3/26 15:34
 */
object ViewAdapter {

    @BindingAdapter(value = ["url", "placeholderRes"], requireAll = false)
    fun setImageUri(imageView: ImageView, url: String, placeholderRes: Int) {
        if (StringUtils.isEmpty(url) && placeholderRes > 0) {
            imageView.setImageResource(placeholderRes)
        } else if (!StringUtils.isEmpty(url)) {
            if (placeholderRes > 0) {
                Glide.with(imageView.context)
                    .load(url)
                    .apply(RequestOptions().placeholder(placeholderRes))
                    .into(imageView)
            } else {
                Glide.with(imageView.context)
                    .load(url)
                    .into(imageView)
            }
        }
    }
}