package com.yjp.easytools.databing.command

/**
 * $
 * @author yjp
 * @date 2020/3/26 15:02
 */
class BindingCommand<T> {
    private var execute: BindingAction? = null
    private var consumer: BindingConsumer<T?>? = null
    private var canExecute0: BindingFunction<Boolean>? = null

    constructor(execute: BindingAction) {
        this.execute = execute
    }

    constructor(execute: BindingConsumer<T?>) {
        this.consumer = execute
    }

    constructor(execute: BindingAction, canExecute0: BindingFunction<Boolean>) {
        this.execute = execute
        this.canExecute0 = canExecute0
    }

    constructor(execute: BindingConsumer<T?>, canExecute0: BindingFunction<Boolean>) {
        this.consumer = execute
        this.canExecute0 = canExecute0
    }

    private fun canExecute0(): Boolean {
        if (canExecute0 == null) {
            return true
        }
        return canExecute0!!.call()!!
    }

    fun execute() {
        if (execute != null && canExecute0()) {
            execute!!.call()
        }
    }

    fun execute(parameter: T) {
        if (consumer != null && canExecute0()) {
            consumer!!.call(parameter)
        }
    }

}