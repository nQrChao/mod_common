package com.box.common.data.model

import java.io.Serializable
import java.lang.reflect.Field

data class ModAppMenu(
    var api: String? = null,
    var id: Int = 0,
    var name: String? = null,
    var params: ParamsBean? = null,
    var icon: String? = null,
    var icon_active: String? = null,
    var labelSelect: Boolean = false
) : Serializable {
    data class ParamsBean(
        // 在Kotlin中注解Java字段时，需要使用 @field: 目标
        @field:ModKeyParam(name = "url")
        var url: String? = null,

        @field:ModKeyParam(name = "container_id")
        var container_id: String? = null
    ) : Serializable {

        /**
         * 获取通过注解构建的参数Map。
         * 注意：原Java代码中 getParams() 方法的逻辑是调用 getParamsByReflect()，
         * 因此这里直接保留了其核心功能。
         */
        fun getParams(): Map<String, String>? {
            return getParamsByReflect(this)
        }

        /**
         * 这段代码直接从Java翻译而来，并继续使用Java反射API，
         * 这在Kotlin中是完全有效和支持的。
         */
        private fun getParamsByReflect(bean: ParamsBean?): Map<String, String>? {
            if (bean == null) {
                return null
            }
            val beanClass = bean.javaClass
            val fields: Array<Field> = beanClass.fields
            val params = HashMap<String, String>()

            for (field in fields) {
                if (field.isAnnotationPresent(ModKeyParam::class.java)) {
                    val keyParamVo = field.getAnnotation(ModKeyParam::class.java)
                    try {
                        val key = keyParamVo.name
                        val value = field.get(bean)
                        if (value != null) {
                            params[key] = value.toString()
                        }
                    } catch (e: IllegalAccessException) {
                        e.printStackTrace()
                    }
                }
            }
            return params
        }
    }
}