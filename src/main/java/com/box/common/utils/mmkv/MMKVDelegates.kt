package com.box.common.utils.mmkv

import com.google.gson.reflect.TypeToken
import com.tencent.mmkv.MMKV
import java.lang.reflect.Type
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import com.box.other.blankj.utilcode.util.GsonUtils

// 外部只通过 mmkv() 函数使用它
class MMKVDelegate<T>(
    private val key: String,
    private val defaultValue: T,
    private val type: Type // 添加这个参数
) : ReadWriteProperty<Any?, T> {

    private val mmkv: MMKV by lazy { MMKV.defaultMMKV() }

    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        // 对于基础类型，为了效率可以直接读写，避免JSON序列化
        return when (defaultValue) {
            is String -> mmkv.decodeString(key, defaultValue) as T
            is Int -> mmkv.decodeInt(key, defaultValue) as T
            is Boolean -> mmkv.decodeBool(key, defaultValue) as T
            is Float -> mmkv.decodeFloat(key, defaultValue) as T
            is Long -> mmkv.decodeLong(key, defaultValue) as T
            // 对于所有其他类型（包括 List），都通过JSON处理
            else -> getObject(key, defaultValue, type)
        }
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        when (value) {
            is String -> mmkv.encode(key, value)
            is Int -> mmkv.encode(key, value)
            is Boolean -> mmkv.encode(key, value)
            is Float -> mmkv.encode(key, value)
            is Long -> mmkv.encode(key, value)
            // 对于所有其他类型（包括 List），都通过JSON处理
            else -> mmkv.encode(key, GsonUtils.toJson(value))
        }
    }

    private fun <T> getObject(key: String, defaultValue: T, type: Type): T {
        val json = mmkv.decodeString(key, null)
        return if (json.isNullOrEmpty()) {
            defaultValue
        } else {
            try {
                // 使用我们传入的、带有完整泛型信息的 type 来解析
                GsonUtils.fromJson(json, type)
            } catch (e: Exception) {
                // 解析失败时返回默认值
                e.printStackTrace()
                defaultValue
            }
        }
    }
}

// 外界使用的主要入口
inline fun <reified T> mmkv(
    key: String,
    defaultValue: T
): ReadWriteProperty<Any?, T> {
    // 因为是 reified，T 在这里是具体化的，可以获取完整的泛型类型
    val type = object : TypeToken<T>() {}.type
    return MMKVDelegate(key, defaultValue, type)
}