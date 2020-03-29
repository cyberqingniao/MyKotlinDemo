package com.yjp.easytools.databing.viewadapter.recyclerview

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yjp.easytools.databing.command.BindingCommand
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

/**
 * $
 * @author yjp
 * @date 2020/3/26 15:54
 */
object ViewAdapter {
    @BindingAdapter("lineManager")
    fun setLineManager(
        recyclerView: RecyclerView,
        lineManagerFactory: LineManagers.LineManagerFactory
    ) {
        recyclerView.addItemDecoration(lineManagerFactory.create(recyclerView))
    }

    @BindingAdapter(
        value = ["onScrollChangeCommand", "onScrollStateChangedCommand"],
        requireAll = false
    )
    fun onScrollChangeCommand(
        recyclerView: RecyclerView,
        onScrollChangeCommand: BindingCommand<ScrollDataWrapper>,
        onScrollStateChangedCommand: BindingCommand<Int>
    ) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            private var state: Int = 0
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                onScrollChangeCommand?.execute(ScrollDataWrapper(dx.toFloat(), dy.toFloat(), state))
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                state = newState
                onScrollStateChangedCommand?.execute(newState)
            }
        })
    }

    @BindingAdapter("onLoadMoreCommand")
    fun onLoadMoreCommand(recyclerView: RecyclerView, onLoadMoreCommand: BindingCommand<Int>) {
        var listener = OnScrollListener(onLoadMoreCommand)
        recyclerView.addOnScrollListener(listener)
    }

    @BindingAdapter("itemAnimator")
    fun setItemAnimator(recyclerView: RecyclerView, animator: RecyclerView.ItemAnimator) {
        recyclerView.itemAnimator = animator
    }

    class OnScrollListener(var onLoadMoreCommand: BindingCommand<Int>) :
        RecyclerView.OnScrollListener() {
        private var methodInvoke = PublishSubject.create<Int>()

        init {
            methodInvoke.throttleFirst(1, TimeUnit.SECONDS).subscribe {
                onLoadMoreCommand.execute(it)
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            var layoutManager = recyclerView.layoutManager as LinearLayoutManager
            var visibleItemCount = layoutManager.childCount
            var totalItemCount = layoutManager.itemCount
            var pastVisiblesItems = layoutManager.findFirstVisibleItemPosition()
            if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                if (onLoadMoreCommand != null) {
                    methodInvoke.onNext(recyclerView.adapter!!.itemCount)
                }
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
        }
    }

    class ScrollDataWrapper(var scrollX: Float, var scrollY: Float, var state: Int)
}