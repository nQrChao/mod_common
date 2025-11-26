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
    val user_account: String = "",
    val nickName: String = "",
    val email: String = "",
    val phonenumber: String = "",
    val sex: String = "",
    var avatar: String = "",
    val password: String = "",
    val status: String = "",
    val delFlag: String = "",
    val loginIp: String = "",
    val loginDate: String = "",
    val pwdUpdateDate: String = "",
    val dept: Dept,
    val postIds: String = "",
    val roleIds: String = "",
    val roleId: String = "",
    val token: String = "",
    val admin: Boolean = false,
    var roles: MutableList<Role> = mutableListOf(),

    //
    var localAvatarResName: String? = null
) : Serializable {

    data class Dept(
        val createBy: String = "",
        val createTime: String = "",
        val updateBy: String = "",
        val updateTime: String = "",
        val remark: String = "",
        val deptId: String = "",
        val parentId: String = "",
        val ancestors: String = "",
        val deptName: String = "",
        val orderNum: String = "",
        val leader: String = "",
        val phone: String = "",
        val email: String = "",
        val status: String = "",
        val delFlag: String = "",
        val children: MutableList<Dept> = mutableListOf(),
    )

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

