package com.yjp.easytools.databing.viewadapter.swiperefresh

import android.content.Intent
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.yjp.easytools.databing.command.BindingAction
import com.yjp.easytools.databing.command.BindingCommand

/**
 * $
 *
 * @author yjp
 * @date 2020-03-29 14:35
 */
object ViewAdapter {

    @BindingAdapter("onRefreshCommand")
    fun onRefreshCommand(swipeRefreshLayout: SwipeRefreshLayout, onRefreshCommand: BindingCommand<*>) {
        swipeRefreshLayout.setOnRefreshListener {
            onRefreshCommand.execute()
        }
    }

    @BindingAdapter("refreshing")
    fun setRefreshing(swipeRefreshLayout: SwipeRefreshLayout, refreshing: Boolean) {
        swipeRefreshLayout.isRefreshing = refreshing
    }
}