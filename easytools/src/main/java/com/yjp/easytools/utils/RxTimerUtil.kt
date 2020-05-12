package com.yjp.easytools.utils

import android.util.Log
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

/**
 * 计时器$
 * @author yjp
 * @date 2020/3/31 17:38
 */
class RxTimerUtil {

    private var mDisposable: Disposable? = null

    /**
     * 定时回调接口
     */
    interface IRxNext {
        fun next(number: Long)
    }

    /**
     * 取消订阅
     */
    fun cancel() {
        if (mDisposable != null && !mDisposable!!.isDisposed) {
            mDisposable?.dispose()
            Log.e("RxTimerUtil", "=====定时器取消=====");
        }
    }

    /**
     * milliseconds毫秒后执行Next操作
     */
    fun timer(milliseconds: Long, next: IRxNext) {
        Observable.timer(milliseconds, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Long> {
                override fun onComplete() {
                    cancel()
                }

                override fun onSubscribe(d: Disposable) {
                    mDisposable = d
                }

                override fun onNext(t: Long) {
                    next.next(t)
                }

                override fun onError(e: Throwable) {
                    cancel()
                }

            })
    }

    /**
     * 每milliseconds毫秒后执行Next操作
     */
    fun interval(milliseconds: Long, next: IRxNext) {
        Observable.interval(milliseconds, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Long> {
                override fun onComplete() {
                    cancel()
                }

                override fun onSubscribe(d: Disposable) {
                    mDisposable = d
                }

                override fun onNext(t: Long) {
                }

                override fun onError(e: Throwable) {
                }

            })
    }

    fun isDispose(): Boolean {
        return mDisposable == null || mDisposable!!.isDisposed
    }
}