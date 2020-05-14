package com.yjp.mydemo.constant

/**
* EventBus Key
* @author yjp
* @date 2020-05-11 23:41:52
*/
data class EventKey(var type: EventType, var arg: Any?){
	constructor(type: EventType) : this(type, null)}

enum class EventType {
	//token验证失败
	TOKEN_FAIL,
	//登录成功
	LOGIN_SUCCESS,
	//退出登录
	LOGOUT_SUCCESS,
}