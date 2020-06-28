package com.yjp.mydemo.entity

/**
 * 登录信息$
 * @author yjp
 * @date 2020/6/8 9:48
 */
data class LoginEntity(
    var token: String,
    var user: UserEntity
)