package com.yjp.easytools.adapter.pager

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter

/**
 * 视图适配器$
 * @author yjp
 * @date 2020/4/1 10:07
 */
class ModelViewAdapter(var context: Context, var views: List<View>) : PagerAdapter() {

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    override fun getCount(): Int {
        return views.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        try {
            container.removeView(`object` as View)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = views[position]
        container.addView(view)
        return view
    }
}