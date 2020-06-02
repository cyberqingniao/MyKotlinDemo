package com.yjp.easytools.http.observer

import com.yjp.easytools.http.exception.ApiException
import com.yjp.easytools.http.exception.ErrorType
import com.yjp.easytools.http.exception.ExceptionEngine
import io.reactivex.Observer

/**
 * $
 * @author yjp
 * @date 2020/4/1 14:49
 */
abstract class BaseObserver<T> : Observer<T> {

    override fun onNext(t: T) {
        onSuccess(t)
    }

    override fun onError(e: Throwable) {
        val ae = if (e is ApiException) {
            e
        } else {
            ExceptionEngine.handleException(e)
        }
        if (ae.code == ErrorType.SUCCESS) {
            onSuccess(null)
        } else {
            onError(ae)
        }
        onComplete()
    }

    abstract fun onError(e: ApiException)
    abstract fun onSuccess(result: T?)
}