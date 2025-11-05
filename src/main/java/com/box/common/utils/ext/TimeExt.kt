package com.box.common.utils.ext

import android.annotation.SuppressLint
import com.box.com.R
import com.box.other.blankj.utilcode.util.Logs
import com.box.other.blankj.utilcode.util.StringUtils
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

const val MINUTE_TIME_FORMAT = "mm:ss"
const val HOUR_TIME_FORMAT = "HH:mm"
const val MONTH_TIME_FORMAT = "MM/dd HH:mm"
const val YEAR_TIME_FORMAT = "yyyy/MM/dd HH:mm"
const val YEAR_MONTH_DAY_FORMAT = "yyyy/MM/dd"
const val FULL_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss"

// --- Long 和 String 时间戳的扩展函数 ---

/**
 * 将 Long? 类型的 nullable 时间戳格式化为 "yyyy-MM-dd HH:mm:ss"
 * 调用方式: timestamp?.toFullDateTimeString()
 */
fun Long?.toFullDateTimeString(): String {
    if (this == null || this <= 0) return ""
    return toDate().format(FULL_DATE_TIME_FORMAT)
}

/**
 * 将 String? 类型的 nullable 时间戳格式化为 "yyyy-MM-dd HH:mm:ss"
 * 调用方式: timestampString?.toFullDateTimeString()
 */
fun String?.toFullDateTimeString(): String {
    val timeAsLong = this?.toLongOrNull()
    return timeAsLong.toFullDateTimeString()
}


/**
 * 将时间戳转换为人性化的时间格式，这是对原 getTimeString 的重构
 * - 调用方式: timestamp.toFriendlyTimeString()
 *
 * @param weekNamesArray 可选参数，允许传入自定义的星期名称，默认为资源文件中的名称
 * @return 格式化后的时间字符串
 */
fun Long?.toFriendlyTimeString(
    weekNamesArray: Array<String> = arrayOf(
        StringUtils.getString(R.string.sunday), StringUtils.getString(R.string.monday),
        StringUtils.getString(R.string.tuesday), StringUtils.getString(R.string.wednesday),
        StringUtils.getString(R.string.thursday), StringUtils.getString(R.string.friday),
        StringUtils.getString(R.string.saturday)
    )
): String {
    if (this == null || this <= 0) return ""

    return try {
        val targetDate = this.toDate()
        val targetCalendar = this.toCalendar()
        val todayCalendar = Calendar.getInstance()

        // 创建一个昨天的实例，而不是修改 todayCalendar，避免 bug
        val yesterdayCalendar = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, -1) }

        // 使用 when 表达式，让逻辑更清晰、更简洁
        when {
            // 今天
            targetCalendar.isSameDayAs(todayCalendar) -> targetDate.format(HOUR_TIME_FORMAT)

            // 昨天
            targetCalendar.isSameDayAs(yesterdayCalendar) -> "昨天 ${targetDate.format(HOUR_TIME_FORMAT)}"

            // 本周 (且在同一年)
            targetCalendar.get(Calendar.YEAR) == todayCalendar.get(Calendar.YEAR) &&
                    targetCalendar.get(Calendar.WEEK_OF_YEAR) == todayCalendar.get(Calendar.WEEK_OF_YEAR) -> {
                val dayOfWeek = targetCalendar.get(Calendar.DAY_OF_WEEK) // 星期日是1, 星期一是2...
                val weekName = weekNamesArray.getOrNull(dayOfWeek - 1) ?: ""
                "$weekName ${targetDate.format(HOUR_TIME_FORMAT)}"
            }

            // 今年
            targetCalendar.get(Calendar.YEAR) == todayCalendar.get(Calendar.YEAR) -> targetDate.format(MONTH_TIME_FORMAT)

            // 非今年
            else -> targetDate.format(YEAR_TIME_FORMAT)
        }
    } catch (e: Exception) {
        Logs.e("toFriendlyTimeString", e.message)
        ""
    }
}

/**
 * 为 String? 类型提供一个方便的重载
 * 调用方式: timestampString.toFriendlyTimeString()
 */
fun String?.toFriendlyTimeString(): String = this?.toLongOrNull().toFriendlyTimeString()


// --- Int (秒) 持续时间的扩展函数 ---

/**
 * 将总秒数格式化为 HH:MM:SS 或 MM:SS
 * 调用方式: seconds.formatDuration()
 */
fun Int.formatDuration(): String {
    if (this < 0) return "00:00"
    val hours = this / 3600
    val minutes = (this % 3600) / 60
    val seconds = this % 60

    return if (hours > 0) {
        String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
    }
}

/**
 * 将总秒数格式化为 "X时Y分Z秒"
 * 调用方式: seconds.formatDurationChinese()
 */
fun Int.formatDurationChinese(): String {
    if (this <= 0) return "0秒"
    val hours = this / 3600
    val minutes = (this % 3600) / 60
    val seconds = this % 60

    return buildString {
        if (hours > 0) append("${hours}小时")
        if (minutes > 0) append("${minutes}分钟")
        // 如果没有任何内容（例如总时间小于1秒），则显示0秒
        if (seconds > 0 || isEmpty()) append("${seconds}秒")
    }
}

// --- 辅助和遗留功能的扩展函数 ---

/**
 * 判断时间属于 本周/本月/某月
 * 调用方式: timestamp.getTimeRuleString()
 */
fun Long.getTimeRuleString(): String {
    val today = Calendar.getInstance()
    val target = this.toCalendar()

    if (target.get(Calendar.YEAR) == today.get(Calendar.YEAR)) {
        if (target.get(Calendar.WEEK_OF_YEAR) == today.get(Calendar.WEEK_OF_YEAR)) {
            return StringUtils.getString(R.string.in_week)
        }
        if (target.get(Calendar.MONTH) == today.get(Calendar.MONTH)) {
            return StringUtils.getString(R.string.in_month)
        }
    }
    return "${target.get(Calendar.MONTH) + 1}月" // Calendar.MONTH 从0开始
}

/**
 * 获取当前时间的 "yyyy-MM-dd HH:mm:ss" 格式字符串 (24小时制)
 * 这是一个静态功能，所以我们把它放在伴生对象中或作为顶层函数
 */
fun getCurrentDateTimeString(): String {
    return System.currentTimeMillis().toFullDateTimeString()
}


// --- 私有的辅助扩展函数 ---

/**
 * 【新增】私有辅助函数，用于将 Date 格式化为字符串
 */
@SuppressLint("SimpleDateFormat")
private fun Date.format(pattern: String): String {
    // SimpleDateFormat 不是线程安全的，每次都创建新的实例是最安全的方式
    val format = SimpleDateFormat(pattern, Locale.getDefault())
    return format.format(this)
}

/**
 * 【新增】私有辅助函数，用于将 Long 转换为 Date
 */
private fun Long.toDate(): Date = Date(this)

/**
 * 【新增】私有辅助函数，用于将 Long 转换为 Calendar
 */
private fun Long.toCalendar(): Calendar = Calendar.getInstance().apply { timeInMillis = this@toCalendar }

/**
 * 【新增】私有辅助函数，用于判断两个 Calendar 实例是否为同一天
 */
private fun Calendar.isSameDayAs(other: Calendar): Boolean {
    return this.get(Calendar.YEAR) == other.get(Calendar.YEAR) &&
            this.get(Calendar.DAY_OF_YEAR) == other.get(Calendar.DAY_OF_YEAR)
}