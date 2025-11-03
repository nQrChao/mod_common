package com.box.common.data.model

import java.io.Serializable

data class ModUserInfo(
    val createBy: String = "",
    val createTime: String = "",
    var updateBy: String = "",
    val updateTime: String = "",
    val remark: String = "",
    val userId: String = "",
    val deptId: String = "",
    val userName: String = "",
    val nickName: String = "",
    val email: String = "",
    val phonenumber: String = "",
    val sex: String = "",
    val avatar: String = "",
    val password: String = "",
    val status: String = "",
    val delFlag: String = "",
    val loginIp: String = "",
    val loginDate: String = "",
    val pwdUpdateDate: String = "",
    val dept: String = "",
    val postIds: String = "",
    val roleIds: String = "",
    val roleId: String = "",
    val token: String = "",
    val admin: Boolean = false,
    var roles: MutableList<Role> = mutableListOf(),

    //
    var localAvatarResName: String? = null
) : Serializable {
    data class Role(
        val createBy: String = "",
        val createTime: String = "",
        val updateBy: String = "",
        val updateTime: String = "",
        val remark: String = "",
        val roleId: String = "",
        val roleName: String = "",
        val roleKey: String = "",
        val roleSort: String = "",
        val dataScope: String = "",
        val menuCheckStrictly: Boolean = false,
        val deptCheckStrictly: Boolean = false,
        val status: String = "",
        val delFlag: String = "",
        val flag: Boolean = false,
        val menuIds: String = "",
        val deptIds: String = "",
        val permissions: String = "",
        val admin: Boolean = false,
    )

}

