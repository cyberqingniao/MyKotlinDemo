package com.yjp.mydemo.ui.main.entity

/**
 * $
 * @author yjp
 * @date 2020/4/1 16:20
 */
data class EventEntity(var type: EventType, var arg: Any?) {
    constructor(type: EventType) : this(type, null)
}

enum class EventType {
    //token验证失败
    TOKEN_FAIL,
}