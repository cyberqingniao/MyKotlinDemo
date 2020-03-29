package com.yjp.easytools.databing.viewadapter.viewpager

import androidx.databinding.BindingAdapter
import androidx.viewpager.widget.ViewPager
import com.yjp.easytools.databing.command.BindingCommand

/**
 * $
 *
 * @author yjp
 * @date 2020-03-29 15:09
 */
object ViewAdapter {
    @BindingAdapter(
        value = ["onPageScrolledCommand", "onPageSelectedCommand", "onPageScrollStateChangedCommand"],
        requireAll = false
    )
    fun onScrollChangeCommand(
        viewPager: ViewPager,
        onPageScrolledCommand: BindingCommand<ViewPagerDataWrapper>?,
        onPageSelectedCommand: BindingCommand<Int>?,
        onPageScrollStateChangedCommand: BindingCommand<Int>?
    ) {
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            private var state: Int = 0
            override fun onPageScrollStateChanged(state: Int) {
                this.state = state
                onPageScrollStateChangedCommand?.execute(state)
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                onPageScrolledCommand?.execute(
                    ViewPagerDataWrapper(
                        position,
                        positionOffset,
                        positionOffsetPixels,
                        state
                    )
                )
            }

            override fun onPageSelected(position: Int) {
                onPageSelectedCommand?.execute(position)
            }

        })
    }

    class ViewPagerDataWrapper(
        var position: Int,
        var positionOffset: Float,
        var positionOffsetPixels: Int,
        var state: Int
    )
}