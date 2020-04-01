package com.yjp.easytools.http.transformer

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * $
 * @author yjp
 * @date 2020/4/1 15:14
 */
class CommonTransformer<T> : ObservableTransformer<BaseResult<T>, T> {
    override fun apply(upstream: Observable<BaseResult<T>>): ObservableSource<T> {
        return upstream.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .compose(CallbackDataTransFormer.getInstance<T>())
    }
}