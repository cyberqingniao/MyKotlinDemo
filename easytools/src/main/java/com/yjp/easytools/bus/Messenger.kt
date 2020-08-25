package com.yjp.easytools.bus

import com.yjp.easytools.databing.command.BindingAction
import com.yjp.easytools.databing.command.BindingConsumer
import com.yjp.easytools.utils.Utils
import java.lang.reflect.Type
import java.util.*

/**
 * 消息$
 * @author yjp
 * @date 2020/6/30 16:30
 */
class Messenger {
    private val recipientsOfSubclassesAction: MutableMap<Type, MutableList<WeakActionAndToken>> =
        mutableMapOf()
    private val recipientsStrictAction: MutableMap<Type, MutableList<WeakActionAndToken>> =
        mutableMapOf()

    fun register(recipient: Any, action: BindingAction) {
        register(
            recipient = recipient,
            token = null,
            receiveDerivedMessagesToo = false,
            action = action
        )
    }

    fun register(recipient: Any, receiveDerivedMessagesToo: Boolean, action: BindingAction) {
        register(
            recipient = recipient,
            token = null,
            receiveDerivedMessagesToo = receiveDerivedMessagesToo,
            action = action
        )
    }

    fun register(recipient: Any, token: Any, action: BindingAction) {
        register(
            recipient = recipient,
            token = token,
            receiveDerivedMessagesToo = false,
            action = action
        )
    }

    fun register(
        recipient: Any,
        token: Any?,
        receiveDerivedMessagesToo: Boolean,
        action: BindingAction
    ) {
        val messageType = NotMsgType::class.java as Type

        val recipients = if (receiveDerivedMessagesToo) {
            recipientsOfSubclassesAction
        } else {
            recipientsStrictAction
        }

        val list = if (!recipients.containsKey(messageType)) {
            val temp = mutableListOf<WeakActionAndToken>()
            recipients[messageType] = temp
            temp
        } else {
            recipients[messageType]
        }

        val weakAction = WeakAction<Any>(recipients, action)
        val item = WeakActionAndToken(weakAction, token)
        list?.add(item)
        cleanup()
    }

    fun register(recipient: Any, tClass: Class<*>, action: BindingConsumer<*>) {
        register(
            recipient = recipient,
            token = null,
            receiveDerivedMessagesToo = false,
            tClass = tClass,
            action = action
        )
    }

    fun register(
        recipient: Any,
        receiveDerivedMessagesToo: Boolean,
        tClass: Class<*>,
        action: BindingConsumer<*>
    ) {
        register(
            recipient = recipient,
            token = null,
            receiveDerivedMessagesToo = receiveDerivedMessagesToo,
            tClass = tClass,
            action = action
        )
    }

    fun register(recipient: Any, token: Any, tClass: Class<*>, action: BindingConsumer<*>) {
        register(
            recipient = recipient,
            token = token,
            receiveDerivedMessagesToo = false,
            tClass = tClass,
            action = action
        )
    }

    fun register(
        recipient: Any,
        token: Any?,
        receiveDerivedMessagesToo: Boolean,
        tClass: Class<*>,
        action: BindingConsumer<*>
    ) {
        val messageType = tClass as Type

        val recipients = if (receiveDerivedMessagesToo) {
            recipientsOfSubclassesAction
        } else {
            recipientsStrictAction
        }

        val list = if (!recipients.containsKey(messageType)) {
            val temp = mutableListOf<WeakActionAndToken>()
            recipients[messageType] = temp
            temp
        } else {
            recipients[messageType]
        }

        val weakAction: WeakAction<Any> = WeakAction(recipient, action) as WeakAction<Any>
        val item = WeakActionAndToken(weakAction, token)
        list?.add(item)
        cleanup()
    }

    private fun cleanup() {
        cleanupList(recipientsOfSubclassesAction)
        cleanupList(recipientsStrictAction)
    }

    private fun cleanupList(mutableMap: MutableMap<Type, MutableList<WeakActionAndToken>>) {
        if (Utils.isEmpty(mutableMap)) {
            return
        }

        val it = mutableMap.entries.iterator() as Iterator<*>

        while (it.hasNext()) {
            val key = it.next()
            val itemList = mutableMap[key]
            if (!Utils.isEmpty(itemList)) {
                itemList!!.forEach {
                    if (!it.action.isLive) {
                        itemList.remove(it)
                    }
                }
                if (itemList.size <= 0) {
                    mutableMap.remove(key)
                }
            }
        }
    }


    fun sendNoMsg(token: Any) {
        sendToTargetOrType(messageTargetType = null, token = token)
    }

    fun sendNoMsgToTarget(target: Any) {
        sendToTargetOrType(messageTargetType = target::class.java, token = null)
    }

    fun sendNoMsgToTargetWithToken(token: Any, target: Any) {
        sendToTargetOrType(messageTargetType = target::class.java, token = token)
    }

    fun send(message: Any) {
        sendToTargetOrType(
            message = message,
            messageTargetType = null,
            token = null
        )
    }

    fun send(message: Any, token: Any) {
        sendToTargetOrType(
            message = message,
            messageTargetType = null,
            token = token
        )
    }

    fun sendToTarget(message: Any, target: Any) {
        sendToTargetOrType(
            message = message,
            messageTargetType = target::class.java,
            token = null
        )
    }

    private fun sendToTargetOrType(message: Any, messageTargetType: Type?, token: Any?) {
        val messageType = message::class.java
        if (recipientsOfSubclassesAction.isNotEmpty()) {
            val listClone = mutableListOf<Type>()
            listClone.addAll(recipientsOfSubclassesAction.keys)
            listClone.forEach {
                var list: MutableList<WeakActionAndToken>? = null
                if (messageType == it ||
                    (it as Class<*>).isAssignableFrom(messageType) ||
                    classImplements(messageType, it)
                ) {
                    list = recipientsOfSubclassesAction[it]
                }
                sendToList(list, messageTargetType, token)
            }
        }
        if (recipientsStrictAction.isNotEmpty()) {
            if (recipientsStrictAction.containsKey(messageType)) {
                val list = recipientsStrictAction[messageType]
                sendToList(message, list, messageTargetType, token)
            }
        }
    }

    private fun sendToList(
        message: Any,
        list: Collection<WeakActionAndToken>?,
        messageTargetType: Type?,
        token: Any?
    ) {
        if (!list.isNullOrEmpty()) {
            val listClone = mutableListOf<WeakActionAndToken>()
            listClone.addAll(list)
            listClone.forEach {
                val action = it.action
                if (action.isLive && action.target != null && (messageTargetType == null || action.target!!::class.java == messageTargetType || classImplements(
                        action.target!!::class.java,
                        messageTargetType
                    )) && ((it.token == null || token == null) || it.token != null && it.token == token)
                ) {
                    it.action.execute(message)
                }
            }
        }
    }

    private fun sendToTargetOrType(messageTargetType: Type?, token: Any?) {
        val messageType = NotMsgType::class.java

        if (recipientsOfSubclassesAction.isNotEmpty()) {
            val listClone = mutableListOf<Type>()
            listClone.addAll(recipientsOfSubclassesAction.keys)
            listClone.forEach {
                var list: MutableList<WeakActionAndToken>? = null
                if (messageType == it ||
                    (it as Class<*>).isAssignableFrom(messageType) ||
                    classImplements(messageType, it)
                ) {
                    list = recipientsOfSubclassesAction[it]
                }
                sendToList(list, messageTargetType, token)
            }
        }
        if (recipientsStrictAction.isNotEmpty()) {
            if (recipientsStrictAction.containsKey(messageType)) {
                val list = recipientsStrictAction[messageType]
                sendToList(list, messageTargetType, token)
            }
        }
        cleanup()
    }

    private fun sendToList(
        list: Collection<WeakActionAndToken>?,
        messageTargetType: Type?,
        token: Any?
    ) {
        if (!list.isNullOrEmpty()) {
            val listClone = mutableListOf<WeakActionAndToken>()
            listClone.addAll(list)
            listClone.forEach {
                val action = it.action
                if (action.isLive && action.target != null && (messageTargetType == null || action.target!!::class.java == messageTargetType || classImplements(
                        action.target!!::class.java,
                        messageTargetType
                    )) && ((it.token == null || token == null) || it.token != null && it.token == token)
                ) {
                    it.action.execute()
                }
            }
        }
    }

    private fun classImplements(instanceType: Type?, interfaceType: Type?): Boolean {
        return if (instanceType == null || interfaceType == null) {
            false
        } else {
            val interfaces = (instanceType as Class<*>).interfaces as Array<Class<*>>
            interfaces.forEach {
                if (it == interfaceType) {
                    return true
                }
            }
            false
        }
    }

    fun unregister(recipient: Any) {
        unregisterFromLists(recipient, recipientsOfSubclassesAction)
        unregisterFromLists(recipient, recipientsStrictAction)
        cleanup()
    }

    fun unregister(recipient: Any, token: Any) {
        unregisterFromLists(recipient, token, null, recipientsOfSubclassesAction)
        unregisterFromLists(recipient, token, null, recipientsStrictAction)
        cleanup()
    }

    private fun unregisterFromLists(
        recipient: Any,
        token: Any?,
        action: BindingAction?,
        lists: MutableMap<Type, MutableList<WeakActionAndToken>>
    ) {
        val messageTargetType = NotMsgType::class.java
        if (lists.isNullOrEmpty() || lists.containsKey(messageTargetType)) {
            return
        }
        synchronized(lists) {
            lists[messageTargetType]?.forEach {
                val weakAction = it.action
                if (recipient == weakAction.target
                    && (action == null || action == weakAction.action)
                    && (token == null || token == it.token)
                ) {
                    it.action.markForDeletion()
                }
            }
        }
    }

    private fun unregisterFromLists(
        recipient: Any,
        lists: MutableMap<Type, MutableList<WeakActionAndToken>>
    ) {
        synchronized(lists) {
            for (messageType in lists.keys) {
                val items = lists[messageType]
                items?.forEach {
                    val action = it.action
                    if (recipient == action.target) {
                        it.action.markForDeletion()
                    }
                }
            }
        }
        cleanupList(lists)
    }

    private data class WeakActionAndToken(var action: WeakAction<Any>, var token: Any?)

    private class NotMsgType

    companion object {
        private var defaultInstance: Messenger? = null

        fun getDefault(): Messenger {
            if (defaultInstance == null) {
                defaultInstance = Messenger()
            }
            return defaultInstance!! as Messenger
        }

        fun overrideDefault(newWeakMessenger: Messenger) {
            defaultInstance = newWeakMessenger
        }

        fun reset() {
            defaultInstance = null
        }

    }
}