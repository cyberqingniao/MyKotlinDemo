package com.yjp.mydemo.http

import com.yjp.easytools.dialog.LoadingDialog
import com.yjp.easytools.http.exception.ApiException
import com.yjp.easytools.http.observer.BaseObserver
import io.reactivex.disposables.Disposable

/**
 * 数据解析
 * @author yjp
 * @date 2020-05-11 23:41:52
 */
abstract class CommonObserver<T>(private var isShowLoading: Boolean) : BaseObserver<T?>() {
    var disposable: Disposable? = null
    override fun onSubscribe(d: Disposable) {
        disposable = d
        if (isShowLoading) {
            if (!LoadingDialog.isShowing()) {
                LoadingDialog.show("请稍后...")
            }
        }
    }

    override fun onError(e: ApiException) {
    }

    override fun onComplete() {
        if (isShowLoading) {
            if (LoadingDialog.isShowing()) {
                LoadingDialog.dismiss()
            }
        }
    }
}
