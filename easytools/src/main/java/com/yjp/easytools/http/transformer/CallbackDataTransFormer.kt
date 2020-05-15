package com.yjp.easytools.http.transformer

import com.yjp.easytools.http.exception.ErrorType
import com.yjp.easytools.http.exception.ExceptionEngine
import com.yjp.easytools.http.exception.ServerException
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer

/**
 * $
 * @author yjp
 * @date 2020/4/1 14:51
 */
class CallbackDataTransFormer<T> private constructor() : ObservableTransformer<BaseResult<T>, T> {

    companion object {
        private var instance: CallbackDataTransFormer<*>? = null

        @Synchronized
        fun <T> getInstance(): CallbackDataTransFormer<T> {
            if (instance == null) {
                synchronized(this) {
                    instance = CallbackDataTransFormer<T>()
                }
            }
            return instance as CallbackDataTransFormer<T>
        }
    }

    override fun apply(upstream: Observable<BaseResult<T>>): ObservableSource<T> {
        return upstream.map<T> {
            if (it.status != ErrorType.SUCCESS || it.data == null) {
                throw ServerException(
                    it.status,
                    it.message!!
                )
            }
            it.data
        }.onErrorResumeNext { throwable: Throwable ->
            // ExceptionEngine 为处理异常的驱动器 throwable
            throwable.printStackTrace()
            Observable.error(ExceptionEngine.handleException(throwable))
        }
    }

}