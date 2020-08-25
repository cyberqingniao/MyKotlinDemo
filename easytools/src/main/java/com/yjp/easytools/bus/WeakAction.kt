package com.yjp.easytools.bus

import com.yjp.easytools.databing.command.BindingAction
import com.yjp.easytools.databing.command.BindingConsumer
import java.lang.ref.WeakReference


/**
 * $
 * @author yjp
 * @date 2020/7/15 9:16
 */
class WeakAction<T> {

    var action: BindingAction? = null
        private set

    var consumer: BindingConsumer<T>? = null
        private set

    private var reference: WeakReference<Any>? = null

    val target: Any?
        get() {
            return reference?.get()
        }

    val isLive: Boolean
        get() {
            if (reference == null || reference?.get() == null) {
                return false
            }
            return true
        }

    constructor(target: Any, action: BindingAction) {
        reference = WeakReference(target)
        this.action = action
    }

    constructor(target: Any, consumer: BindingConsumer<T>) {
        reference = WeakReference(target)
        this.consumer = consumer
    }

    fun execute() {
        if (isLive) {
            action?.call()
        }
    }

    fun execute(parameter: T) {
        if (isLive) {
            consumer?.call(parameter)
        }
    }

    fun markForDeletion() {
        reference?.clear()
        reference = null
        action = null
        consumer = null
    }

}