package com.yjp.easytools.databing.viewadapter.view

import android.annotation.SuppressLint
import android.view.View
import androidx.databinding.BindingAdapter
import com.jakewharton.rxbinding2.view.RxView
import com.yjp.easytools.databing.command.BindingCommand
import java.util.concurrent.TimeUnit

/**
 * $
 *
 * @author yjp
 * @date 2020-03-29 14:40
 */
object ViewAdapter {

    //防重复点击间隔(秒)
    const val CLICK_INTERVAL = 1

    @SuppressLint("CheckResult")
    @BindingAdapter(value = ["onClickCommand", "isThrottleFirst"], requireAll = false)
    fun onClickCommand(view: View, clickCommand: BindingCommand<*>, isThrottleFirst: Boolean) {
        if (isThrottleFirst) {
            RxView.clicks(view)
                .subscribe {
                    clickCommand.execute()
                }
        } else {
            RxView.clicks(view)
                .throttleFirst(CLICK_INTERVAL.toLong(), TimeUnit.SECONDS)
                .subscribe {
                    clickCommand.execute()
                }
        }
    }

    @SuppressLint("CheckResult")
    @BindingAdapter(value = ["onLongClickCommand"], requireAll = false)
    fun onLongClickCommand(view: View, clickCommand: BindingCommand<*>) {
        RxView.longClicks(view)
            .subscribe {
                clickCommand.execute()
            }
    }

    @BindingAdapter(value = ["currentView"], requireAll = false)
    fun replyCurrentView(currentView: View, bindingCommand: BindingCommand<View>) {
        bindingCommand.execute(currentView)
    }

    @BindingAdapter("requestFocus")
    fun requestFocusCommand(view: View, needRequestFocus: Boolean) {
        if (needRequestFocus) {
            view.isFocusableInTouchMode = true
            view.requestFocus()
        } else {
            view.clearFocus()
        }
    }

    @BindingAdapter("onFocusChangeCommand")
    fun onFocusChangeCommand(view: View, onFocusChangeCommand: BindingCommand<Boolean>) {
        view.setOnFocusChangeListener { v, hasFocus ->
            {
                onFocusChangeCommand.execute(hasFocus)
            }
        }
    }

    @BindingAdapter("isVisible")
    fun isVisible(view: View, visibility: Boolean) {
        view.visibility = if (visibility) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}