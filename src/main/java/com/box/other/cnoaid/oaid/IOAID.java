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


/**
 * OAID接口
 *
 * @author 大定府羡民（1032694760@qq.com）
 * @since 2020/5/30
 */
public interface IOAID {

    /**
     * 是否支持OAID
     *
     * @return 支持则返回true，不支持则返回false
     */
    boolean supported();

    /**
     * 异步获取OAID
     *
     * @param getter 回调
     */
    void doGet(IGetter getter);

}
