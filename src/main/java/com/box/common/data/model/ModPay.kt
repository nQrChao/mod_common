package com.box.common.data.model

import java.io.Serializable

data class ModPay(
    val version: Int = 0,
    val pay_str: String = "",
    val out_trade_no: Int = 0,
    val amount: String = "",
    val act: String = "",
    val pay_url: String = "",
    ) : Serializable