/*
 * Copyright (c) 2016-present. 贵州纳雍穿青人李裕江 and All Contributors.
 *
 * The software is licensed under the Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *     http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
 * PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package com.box.other.cnoaid.oaid;

import android.util.Log;

/**
 * 调试日志工具类
 *
 * @author 贵州山魈羡民 (1032694760@qq.com)
 * @since 2021/3/26 21:34
 */
public final class OAIDLog {
    private static final String TAG = "OAID";
    private static boolean enable = false;

    private OAIDLog() {
        super();
    }

    /**
     * 启用调试日志
     */
    public static void enable() {
        enable = true;
    }

    /**
     * 打印调试日志
     *
     * @param log 日志信息
     */
    public static void print(Object log) {
        if (!enable) {
            return;
        }
        if (log == null) {
            log = "<null>";
        }
        Log.i(TAG, log.toString());
    }

}
