package com.box.common.utils

import android.annotation.SuppressLint
import com.box.common.R
import com.box.other.blankj.utilcode.util.Logs
import com.box.other.blankj.utilcode.util.StringUtils
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * 时间处理工具类
 */
object UtilTime {
    // 定义各种日期格式常量
    const val MINUTE_TIME_FORMAT = "mm:ss"
    const val HOUR_TIME_FORMAT = "HH:mm"
    const val MONTH_TIME_FORMAT = "MM/dd HH:mm"
    const val YEAR_TIME_FORMAT = "yyyy/MM/dd HH:mm"
    const val YEAR_MONTH_DAY_FORMAT = "yyyy/MM/dd"
    // 新增的完整日期时间格式
    const val FULL_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss"


    /**
     * 【新增】一个安全的、不会崩溃的重载方法，用于处理字符串时间戳。
     *
     * @param timestamp 字符串形式的时间戳，可以为 null、空或无效格式。
     * @return 格式化后的日期字符串，如果输入无效则返回空字符串。
     */
    fun formatToYearSecond(timestamp: String?): String {
        if (timestamp.isNullOrEmpty()) {
            return ""
        }
        val timeAsLong = timestamp.toLongOrNull()
        return formatToYearSecond(timeAsLong)
    }

    /**
     * 将 Long 类型的时间戳格式化为 "yyyy-MM-dd HH:mm:ss"
     * (这是我们之前创建的接收 Long? 的方法)
     */
    fun formatToYearSecond(timestamp: Long?): String {
        if (timestamp == null || timestamp <= 0) return ""
        return getTime(timestamp, FULL_DATE_TIME_FORMAT)
    }

    /**
     * 将字符串形式的时间戳转换为人性化的时间格式
     */
    fun getTimeString(timestamp: String?): String {
        if (timestamp.isNullOrEmpty()) return ""
        // toLongOrNull() 是更安全的转换方式，失败返回 null
        return getTimeString(timestamp.toLongOrNull())
    }

    /**
     * 将时间戳转换为人性化的时间格式
     * - 今天: HH:mm
     * - 昨天: 昨天 HH:mm
     * - 本周内（昨天除外）: 星期X HH:mm
     * - 今年内（本周外）: MM/dd HH:mm
     * - 非今年: yyyy/MM/dd HH:mm
     *
     * @param timestamp 时间戳 (毫秒)，可为 null
     * @return 格式化后的时间字符串
     */
    fun getTimeString(timestamp: Long?): String {
        if (timestamp == null || timestamp <= 0) return ""

        // 在 object 中，可以像 Java static 一样访问资源
        val weekNames = arrayOf(
            StringUtils.getString(R.string.sunday),
            StringUtils.getString(R.string.monday),
            StringUtils.getString(R.string.tuesday),
            StringUtils.getString(R.string.wednesday),
            StringUtils.getString(R.string.thursday),
            StringUtils.getString(R.string.friday),
            StringUtils.getString(R.string.saturday)
        )

        return try {
            val targetCalendar = Calendar.getInstance().apply { timeInMillis = timestamp }
            val todayCalendar = Calendar.getInstance()

            when {
                // 同一年
                targetCalendar.get(Calendar.YEAR) == todayCalendar.get(Calendar.YEAR) -> {
                    when {
                        // 今天
                        targetCalendar.get(Calendar.DAY_OF_YEAR) == todayCalendar.get(Calendar.DAY_OF_YEAR) -> {
                            getTime(timestamp, HOUR_TIME_FORMAT)
                        }
                        // 昨天
                        targetCalendar.get(Calendar.DAY_OF_YEAR) == todayCalendar.apply { add(Calendar.DAY_OF_MONTH, -1) }.get(Calendar.DAY_OF_YEAR) -> {
                            "昨天 ${getTime(timestamp, HOUR_TIME_FORMAT)}"
                        }
                        // 本周
                        targetCalendar.get(Calendar.WEEK_OF_YEAR) == Calendar.getInstance().get(Calendar.WEEK_OF_YEAR) -> {
                            val dayOfWeek = targetCalendar.get(Calendar.DAY_OF_WEEK)
                            // Kotlin 的索引从0开始，DAY_OF_WEEK 从1开始
                            val weekName = weekNames.getOrNull(dayOfWeek - 1) ?: ""
                            "$weekName ${getTime(timestamp, HOUR_TIME_FORMAT)}"
                        }
                        // 今年其他时间
                        else -> getTime(timestamp, MONTH_TIME_FORMAT)
                    }
                }
                // 非同一年
                else -> getTime(timestamp, YEAR_TIME_FORMAT)
            }
        } catch (e: Exception) {
            Logs.e("getTimeString", e.message)
            ""
        }
    }

    /**
     * 根据指定格式将时间戳转换为字符串
     */
    fun getTime(time: Long, pattern: String): String {
        return dateFormat(Date(time), pattern)
    }

    /**
     * 根据指定格式将 Date 对象转换为字符串
     */
    @SuppressLint("SimpleDateFormat")
    fun dateFormat(date: Date, pattern: String): String {
        // SimpleDateFormat 不是线程安全的，每次都创建新的实例
        val format = SimpleDateFormat(pattern, Locale.getDefault())
        return format.format(date)
    }

    /**
     * [已优化] 将秒数格式化为 HH:MM:SS 或 MM:SS 的形式
     *
     * @param totalSeconds 总秒数
     * @return 格式化后的字符串
     */
    fun formatDuration(totalSeconds: Int): String {
        if (totalSeconds < 0) return "00:00"
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60

        return if (hours > 0) {
            String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
        } else {
            String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
        }
    }

    /**
     * [已优化] 将秒数格式化为 "X时Y分Z秒" 的形式
     *
     * @param totalSeconds 总秒数
     * @return 格式化后的中文字符串
     */
    fun formatDurationChinese(totalSeconds: Int): String {
        if (totalSeconds <= 0) return "0秒"
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60

        // 使用 buildString 替代 StringBuilder，是更 idiomatic 的 Kotlin 写法
        return buildString {
            if (hours > 0) append("${hours}小时")
            if (minutes > 0) append("${minutes}分钟")
            if (seconds > 0 || isEmpty()) append("${seconds}秒")
        }
    }

    /**
     * 判断时间属于 本周/本月/某月
     */
    fun getTimeRules(time: Long): String {
        val today = Calendar.getInstance()
        val target = Calendar.getInstance().apply { timeInMillis = time }

        // 必须是同一年才有"本周"、"本月"的概念
        if (target.get(Calendar.YEAR) == today.get(Calendar.YEAR)) {
            if (target.get(Calendar.WEEK_OF_YEAR) == today.get(Calendar.WEEK_OF_YEAR)) {
                return StringUtils.getString(R.string.in_week)
            }
            if (target.get(Calendar.MONTH) == today.get(Calendar.MONTH)) {
                return StringUtils.getString(R.string.in_month)
            }
        }
        // Calendar.MONTH 从0开始，所以要+1
        return "${target.get(Calendar.MONTH) + 1}月"
    }

    /**
     * 获取当前时间的 "yyyy-MM-dd HH:mm:ss" 格式字符串 (24小时制)
     */
    fun getCurrentDataString(): String {
        return formatToYearSecond(System.currentTimeMillis())
    }
}