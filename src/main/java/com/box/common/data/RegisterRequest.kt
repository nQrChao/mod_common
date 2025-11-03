package com.box.common.data

import java.io.Serializable

data class RegisterRequest(
    val userName: String,
    val password: String
): Serializable