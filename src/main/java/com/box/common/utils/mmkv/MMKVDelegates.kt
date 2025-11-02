package com.box.common.utils.mmkv

import com.box.other.blankj.utilcode.util.GsonUtils
import com.google.gson.reflect.TypeToken
import com.tencent.mmkv.MMKV
import java.lang.reflect.Type
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

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





/**
 * 实现 MutableList 接口的持久化列表。
 * 它将所有修改操作（add, remove, clear等）代理到 MMKV 进行自动保存。
 *
 * @param T 列表中元素的类型
 * @param key MMKV 中保存该列表的键
 * @param type 用于 Gson 解析的、包含完整泛型信息的 Type
 */
class MMKVPersistentList<T>(
    private val key: String,
    private val type: Type
) : MutableList<T> {
    // 核心：内部使用你已经写好的 MMKVDelegate 来存取整个列表
    private var internalList: MutableList<T> by MMKVDelegate(key, mutableListOf(), type)
    // 每个修改操作都必须遵循“读取-修改-写回”的模式来触发 MMKVDelegate 的 setValue
    override fun add(element: T): Boolean {
        val currentList = internalList.toMutableList() // 读取
        val result = currentList.add(element)          // 修改
        internalList = currentList                     // 写回 (触发保存)
        return result
    }

    override fun add(index: Int, element: T) {
        val currentList = internalList.toMutableList()
        currentList.add(index, element)
        internalList = currentList
    }

    override fun addAll(
        index: Int,
        elements: Collection<T>
    ): Boolean {
        val currentList = internalList.toMutableList()
        val result = currentList.addAll(index,elements)
        internalList = currentList
        return result
    }

    override fun addAll(elements: Collection<T>): Boolean {
        val currentList = internalList.toMutableList()
        val result = currentList.addAll(elements)
        internalList = currentList
        return result
    }


    override fun remove(element: T): Boolean {
        val currentList = internalList.toMutableList()
        val result = currentList.remove(element)
        internalList = currentList
        return result
    }

    override fun removeAt(index: Int): T {
        val currentList = internalList.toMutableList()
        val result = currentList.removeAt(index)
        internalList = currentList
        return result
    }

    override fun clear() {
        // 对于 clear，直接赋值一个空列表更高效
        internalList = mutableListOf()
    }

    override fun set(index: Int, element: T): T {
        val currentList = internalList.toMutableList()
        val result = currentList.set(index, element)
        internalList = currentList
        return result
    }

    // 所有只读操作都直接代理给内部列表即可，无需写回
    override val size: Int
        get() = internalList.size

    override fun contains(element: T): Boolean = internalList.contains(element)
    override fun containsAll(elements: Collection<T>): Boolean = internalList.containsAll(elements)
    override fun get(index: Int): T = internalList[index]
    override fun indexOf(element: T): Int = internalList.indexOf(element)
    override fun isEmpty(): Boolean = internalList.isEmpty()
    override fun iterator(): MutableIterator<T> = internalList.iterator() // 注意：通过迭代器修改不会自动保存！
    override fun lastIndexOf(element: T): Int = internalList.lastIndexOf(element)
    override fun listIterator(): MutableListIterator<T> = internalList.listIterator()
    override fun listIterator(index: Int): MutableListIterator<T> = internalList.listIterator(index)
    override fun subList(fromIndex: Int, toIndex: Int): MutableList<T> = internalList.subList(fromIndex, toIndex)

    // ... 其他需要实现的 MutableList 方法 ...
    override fun removeAll(elements: Collection<T>): Boolean {
        val currentList = internalList.toMutableList()
        val result = currentList.removeAll(elements)
        internalList = currentList
        return result
    }

    override fun retainAll(elements: Collection<T>): Boolean {
        val currentList = internalList.toMutableList()
        val result = currentList.retainAll(elements)
        internalList = currentList
        return result
    }
}

inline fun <reified T> mmkvList(
    key: String
): MutableList<T> {
    val type = object : TypeToken<MutableList<T>>() {}.type
    return MMKVPersistentList(key, type)
}