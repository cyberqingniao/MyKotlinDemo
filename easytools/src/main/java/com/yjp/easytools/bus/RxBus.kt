package com.yjp.easytools.bus

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import java.util.concurrent.ConcurrentHashMap
/**
 * $
 *
 * @author yjp
 * @date 2020/7/15 9:10
 */
class RxBus {

    companion object {

        @Volatile
        private var mDefaultInstance: RxBus? = null

        fun getDefault(): RxBus {
            if (mDefaultInstance == null) {
                synchronized(RxBus::class.java) {
                    if (mDefaultInstance == null) {
                        mDefaultInstance = RxBus()
                    }
                }
            }
            return mDefaultInstance!!
        }
    }

    private var mBus: Subject<Any>? = null

    private var mStickyEventMap: Map<Class<*>, Any>? = null

    init {
        mBus = PublishSubject.create<Any>().toSerialized()
        mStickyEventMap = ConcurrentHashMap()
    }


    /**
     * 发送事件
     */
    fun post(event: Any) {
        mBus!!.onNext(event)
    }

    /**
     * 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
     */
    fun <T> toObservable(eventType: Class<T>?): Observable<T>? {
        return mBus!!.ofType(eventType)
    }

    /**
     * 判断是否有订阅者
     */
    fun hasObservers(): Boolean {
        return mBus!!.hasObservers()
    }

    fun reset() {
        mDefaultInstance = null
    }

}

data class EventEntity(
    var type: Any,
    var arg: Any?
)