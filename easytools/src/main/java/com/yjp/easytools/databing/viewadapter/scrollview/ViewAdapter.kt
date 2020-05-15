package com.yjp.easytools.databing.viewadapter.scrollview

import android.widget.ScrollView
import androidx.core.widget.NestedScrollView
import androidx.databinding.BindingAdapter
import com.yjp.easytools.databing.command.BindingCommand
import com.yjp.easytools.databing.viewadapter.recyclerview.ViewAdapter

/**
 * $
 * @author yjp
 * @date 2020/3/27 20:24
 */
object ViewAdapter {

    @BindingAdapter("onScrollChangeCommand")
    fun onScrollChangeCommand(
        nestedScrollView: NestedScrollView,
        onScrollChangeCommand: BindingCommand<NestScrollDataWrapper>
    ) {
        nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            onScrollChangeCommand.execute(
                NestScrollDataWrapper(scrollX, scrollY, oldScrollX, oldScrollY)
            )
        })
    }

    fun onScrollChangeCommand(scrollView: ScrollView,onScrollChangeCommand: BindingCommand<ScrollDataWrapper>){
        scrollView.viewTreeObserver.addOnScrollChangedListener {
            onScrollChangeCommand.execute(ScrollDataWrapper(scrollView.scaleX,scrollView.scaleY))
        }
    }

    class ScrollDataWrapper(var scrollX:Float,var scrollY:Float)

    class NestScrollDataWrapper(
        var scrollX: Int,
        var scrollY: Int,
        var oldScrollX: Int,
        var oldScrollY: Int
    )
}