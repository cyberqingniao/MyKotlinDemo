package com.yjp.easytools.databing.viewadapter.recyclerview

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yjp.easytools.databing.command.BindingCommand
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

/**
 * 列表滑动事件监听$
 *
 * @author yjp
 * @date 2020-05-16 13:13
 */
class OnScrollListener(private var onLoadMoreCommand: BindingCommand<Int>) :
    RecyclerView.OnScrollListener() {
    private var methodInvoke = PublishSubject.create<Int>()

    init {
        methodInvoke.throttleFirst(1, TimeUnit.SECONDS).subscribe(onLoadMoreCommand::execute)
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val pastVisiblesItems = layoutManager.findFirstVisibleItemPosition()
        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
            methodInvoke.onNext(recyclerView.adapter!!.itemCount)
        }
    }

}

class ScrollDataWrapper(var scrollX: Float, var scrollY: Float, var state: Int)