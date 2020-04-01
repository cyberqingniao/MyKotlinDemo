package com.yjp.mydemo.ui.main.http

import android.util.Log
import com.yjp.easytools.dialog.LoadingDialog
import com.yjp.easytools.http.exception.ApiException
import com.yjp.easytools.http.exception.ErrorType
import com.yjp.easytools.http.observer.BaseObserver
import com.yjp.easytools.utils.ActivityManager
import com.yjp.easytools.utils.StringUtils
import com.yjp.easytools.utils.ToastUtils
import com.yjp.mydemo.ui.main.entity.EventEntity
import com.yjp.mydemo.ui.main.entity.EventType
import io.reactivex.disposables.Disposable
import org.greenrobot.eventbus.EventBus

/**
 * $
 * @author yjp
 * @date 2020/4/1 16:15
 */
abstract class CommonObserver<T>(var isShowLoading: Boolean) : BaseObserver<T?>() {
    // Disposable 相当于RxJava1.x中的 Subscription，用于解除订阅 disposable.dispose();
    private var disposable: Disposable? = null

    override fun onSubscribe(d: Disposable) {
        disposable = d
        Log.d(TAG, "开始执行请求")
        if (isShowLoading) {
            if (!LoadingDialog.isShowing()) {
                LoadingDialog.showLoading(ActivityManager.instance.currentActivity(), "请稍后...")
            }
        }
    }

    override fun onError(e: ApiException) {
        Log.e(
            TAG,
            "HTTP错误 --> " + "code:" + e.code + ", message:" + e.message
        )
        when (e.code) {
            401 -> {
                if (!StringUtils.isEmpty(e.msg)) {
                    ToastUtils.showShort(e.msg!!)
                }
                EventBus.getDefault().post(EventEntity(EventType.TOKEN_FAIL))
            }
            ErrorType.SUCCESS -> {
                onSuccess(null)
            }
            else -> {
                if (!StringUtils.isEmpty(e.msg)) {
                    ToastUtils.showShort(e.msg!!)
                }
            }
        }
    }

    override fun onComplete() {
        Log.d(TAG, "请求返回了")
        if (isShowLoading) {
            if (LoadingDialog.isShowing()) {
                LoadingDialog.dismissLoading()
            }
        }
    }

    companion object {
        private const val TAG = "CommonObserver"
    }
}