package com.yjp.easytools.bus

import com.yjp.easytools.databing.command.BindingAction
import com.yjp.easytools.databing.command.BindingConsumer
import java.lang.ref.WeakReference


/**
 * About : kelin的WeakBindingAction
 */
class WeakAction<T> {
    private var action: BindingAction? = null
    private var consumer: BindingConsumer<T>? = null
    val isLive: Boolean
        get() {
            if (reference == null) {
                return false
            }
            return reference.get() != null
        }
    val target: Any?
        get() = if (reference != null) {
            reference.get()
        } else null

    private var reference: WeakReference<*>?

    constructor(target: Any?, action: BindingAction?) {
        reference = WeakReference(target)
        this.action = action
    }

    constructor(target: Any?, consumer: BindingConsumer<T>?) {
        reference = WeakReference(target)
        this.consumer = consumer
    }

    fun execute() {
        if (action != null && isLive) {
            action!!.call()
        }
    }

    fun execute(parameter: T) {
        if (consumer != null
            && isLive
        ) {
            consumer!!.call(parameter)
        }
    }

    fun markForDeletion() {
        reference!!.clear()
        reference = null
        action = null
        consumer = null
    }

    val bindingAction: BindingAction?
        get() = action

    val bindingConsumer: BindingConsumer<T>?
        get() = consumer

}