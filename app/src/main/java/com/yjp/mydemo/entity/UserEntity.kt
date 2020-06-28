package com.yjp.mydemo.entity

/**
 * 用户信息$
 * @author yjp
 * @date 2020/6/8 9:49
 */
data class UserEntity(
    var id: Int,
    var username: String,
    var password: String,
    var nickname: String,
    var realname: String,
    var userAge: String,
    var userSex: String,
    var userAddress: String,
    var userPhone: String,
    var userMail: String,
    var userQQ: String,
    var department: String,
    var createDate: String,
    var lastLoginDate: String,
    var lastLoginIp: String,
    var isEnabled: Int,
    var lastradar: String,
    var spare1: String,
    var spare2: String,
    var spare3: String,
    var roleId: String,
    var deptName: String
)