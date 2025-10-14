package com.box.common.utils

import android.os.Build
import android.text.TextUtils
import androidx.annotation.RequiresApi
import com.box.common.REMIND_FRIENDS
import com.box.common.REMIND_FRIENDS_S
import com.box.common.REMIND_GROUPS
import com.box.common.REMIND_GROUPS_S
import com.box.common.REMIND_SYSTEM
import com.box.other.blankj.utilcode.util.GsonUtils
import com.box.other.blankj.utilcode.util.Logs
import com.box.other.blankj.utilcode.util.StringUtils
import com.box.common.data.model.ChatListAtBean
import com.box.common.data.model.GroupAnnBean
import com.box.common.data.model.GroupAtBean
import com.box.common.data.model.ModTradeGoodDetailBean
import com.box.common.data.model.ModUserInfoBean
import com.box.common.data.model.RemindBean
import com.box.other.blankj.utilcode.util.TimeUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tencent.mmkv.MMKV

object MMKVUtil {
    private val mmkv: MMKV = MMKV.defaultMMKV()
    private const val MKV_APP_INFO = "APP"
    const val USER_INFO = "USER_INFO"
    private fun allData(): MMKV {
        return mmkv
    }

    fun saveMMKVString(key: String?, string: String?) {
        if (string == null) {
            mmkv.removeValueForKey(key)
        } else {
            mmkv.encode(key, string)
        }
    }

    fun getShouQuan(): String? {
        return mmkv.decodeString("shouQuan", "")
    }

    fun saveShouQuan(string: String?) {
        if (string == null) {
            mmkv.removeValueForKey("shouQuan")
        } else {
            mmkv.encode("shouQuan", string)
        }
    }

    fun getRemindDisable(): Boolean {
        return mmkv.decodeBool("remindDisable", true)
    }

    fun setRemindDisable(boolean: Boolean) {
        mmkv.encode("remindDisable", boolean)
    }

    fun getRemindList(): MutableList<RemindBean> {
        val listString = mmkv.decodeString("remindList", "")
        return if (StringUtils.isEmpty(listString)) {
            mutableListOf(
                RemindBean(REMIND_FRIENDS, "好友消息", true),
                RemindBean(REMIND_GROUPS, "群组消息", true),
                RemindBean(REMIND_SYSTEM, "系统消息", true),
                RemindBean(REMIND_FRIENDS_S, "好友申请消息", true),
                RemindBean(REMIND_GROUPS_S, "群组申请消息", true)
            )
        } else {
            GsonUtils.fromJson(listString, object : TypeToken<List<RemindBean>>() {}.type)
        }
    }

    fun saveRemindList(list: MutableList<RemindBean>?) {
        if (list == null) {
            mmkv.removeValueForKey("remindList")
        } else {
            for (i in list) {
                saveRemindItemDisable(i)
            }
            mmkv.encode("remindList", GsonUtils.toJson(list))
        }
    }

    fun getRemindItemDisable(type: String): Boolean {
        return mmkv.decodeBool(type, true)
    }

    private fun saveRemindItemDisable(remind: RemindBean) {
        mmkv.encode(remind.id, remind.select)
    }


    fun saveChatListAt(chatListAtBean: ChatListAtBean?) {
        if (chatListAtBean == null) {
            mmkv.removeValueForKey("chat_list_at")
        } else {
            val listString = mmkv.decodeString("chat_list_at", "")
            val list: MutableList<ChatListAtBean> = if (StringUtils.isEmpty(listString)) {
                mutableListOf()
            } else {
                GsonUtils.fromJson(listString, object : TypeToken<List<ChatListAtBean>>() {}.type)
            }
            list.add(chatListAtBean)
            mmkv.encode("chat_list_at", GsonUtils.toJson(list))
        }
    }


    fun isGroupAtRead(sMessageId: String?): Boolean {
        val listString = mmkv.decodeString("group_at_read", "")
        if (StringUtils.isEmpty(listString)) {
            return false
        }
        val list: MutableList<GroupAtBean> = GsonUtils.fromJson(listString, object : TypeToken<List<GroupAtBean>>() {}.type)
        list.forEach {
            if (sMessageId == it.serverMsgID) {
                return true
            }
        }
        return false
    }

    fun saveGroupAtRead(groupAtBean: GroupAtBean?) {
        if (groupAtBean == null) {
            mmkv.removeValueForKey("group_at_read")
        } else {
            val listString = mmkv.decodeString("group_at_read", "")
            val list: MutableList<GroupAtBean> = if (StringUtils.isEmpty(listString)) {
                mutableListOf()
            } else {
                GsonUtils.fromJson(listString, object : TypeToken<List<GroupAtBean>>() {}.type)
            }
            list.add(groupAtBean)
            mmkv.encode("group_at_read", GsonUtils.toJson(list))
        }
    }

    fun isGroupAnnRead(sMessageId: String?): Boolean {
        val listString = mmkv.decodeString("group_ann_read", "")
        if (StringUtils.isEmpty(listString)) {
            return false
        }
        val list: MutableList<GroupAnnBean> = GsonUtils.fromJson(listString, object : TypeToken<List<GroupAnnBean>>() {}.type)
        list.forEach {
            if (sMessageId == it.serverMsgID) {
                return true
            }
        }
        return false
    }

    fun saveGroupAnnRead(sMessageId: String?) {
        if (sMessageId == null) {
            mmkv.removeValueForKey("group_ann_read")
        } else {
            val listString = mmkv.decodeString("group_ann_read", "")
            val list: MutableList<GroupAnnBean> = if (StringUtils.isEmpty(listString)) {
                mutableListOf()
            } else {
                GsonUtils.fromJson(listString, object : TypeToken<List<GroupAnnBean>>() {}.type)
            }
            list.add(GroupAnnBean(serverMsgID = sMessageId))
            Logs.e(" GsonUtils.toJson(list):" + GsonUtils.toJson(list))
            mmkv.encode("group_ann_read", GsonUtils.toJson(list))
        }
    }


    fun getLoginUser(): String? {
        return mmkv.decodeString("login_user", "")
    }

    fun saveLoginUser(string: String?) {
        if (string == null) {
            mmkv.removeValueForKey("login_user")
        } else {
            mmkv.encode("login_user", string)
        }
    }

    fun getJwtToken(): String? {
        return mmkv.decodeString("jwt_token", "")
    }

    fun saveJwtToken(string: String?) {
        if (string == null) {
            mmkv.removeValueForKey("jwt_token")
        } else {
            mmkv.encode("jwt_token", string)
        }
    }

    fun getJwtRefreshToken(): String? {
        return mmkv.decodeString("jwt_refresh_token", "")
    }

    fun saveJwtRefreshToken(string: String?) {
        if (string == null) {
            mmkv.removeValueForKey("jwt_refresh_token")
        } else {
            mmkv.encode("jwt_refresh_token", string)
        }
    }

    fun getAiRefreshToken(): String? {
        return mmkv.decodeString("ai_refresh_token", "")
    }

    fun saveAiRefreshToken(string: String?) {
        if (string == null) {
            mmkv.removeValueForKey("ai_refresh_token")
        } else {
            mmkv.encode("ai_refresh_token", string)
        }
    }

    fun getMMKVString(key: String?): String? {
        return mmkv.decodeString(key, "")
    }


    fun saveModUser(user: ModUserInfoBean?) {
        val kv = MMKV.mmkvWithID(MKV_APP_INFO)
        if (user == null) {
            kv.encode("modUser", "")
            setIsLogin(false)
        } else {
            kv.encode("modUser", Gson().toJson(user))
            setIsLogin(true)
        }
    }

    fun getModUser(): ModUserInfoBean? {
        val kv = MMKV.mmkvWithID(MKV_APP_INFO)
        val userStr = kv.decodeString("modUser")
        return if (TextUtils.isEmpty(userStr)) {
            null
        } else {
            Gson().fromJson(userStr, ModUserInfoBean::class.java)
        }
    }

    /**
     * 是否已经登录
     */
    fun isLogin(): Boolean {
        val kv = MMKV.mmkvWithID(MKV_APP_INFO)
        return kv.decodeBool("login", false)
    }

    /**
     * 设置是否已经登录
     */
    private fun setIsLogin(isLogin: Boolean) {
        val kv = MMKV.mmkvWithID(MKV_APP_INFO)
        kv.encode("login", isLogin)
    }

    /**
     * 是否是第一次登陆
     */
    fun isFirst(): Boolean {
        val kv = MMKV.mmkvWithID(MKV_APP_INFO)
        return kv.decodeBool("first", true)
    }

    /**
     * 是否是第一次登陆
     */
    fun setFirst(first: Boolean): Boolean {
        val kv = MMKV.mmkvWithID(MKV_APP_INFO)
        return kv.encode("first", first)
    }

    fun saveConversationDraft(content: String?, id: String) {
        if (content == null || TextUtils.isEmpty(content)) {
            mmkv.removeValueForKey("${id}_draft")
        } else {
            mmkv.encode("${id}_draft", content)
        }
    }

    fun getConversationDraft(id: String): String? {
        return mmkv.getString("${id}_draft", "")
    }

    fun setDDTime(time: Long) {
        if (time == 0L) {
            mmkv.removeValueForKey("dd_time")
        } else {
            mmkv.encode("dd_time", time)
        }
    }

    fun getDDTime(): Long {
        return mmkv.decodeLong("dd_time", 0L)
    }

    fun getFontSize(): Float {
        val kv = MMKV.mmkvWithID(MKV_APP_INFO)
        return kv.decodeFloat("fontSize", 15.0f)
    }

    fun setFontSize(fontSize: Float): Boolean {
        val kv = MMKV.mmkvWithID(MKV_APP_INFO)
        return kv.encode("fontSize", fontSize)
    }

    fun getFontScale(): Float {
        val kv = MMKV.mmkvWithID(MKV_APP_INFO)
        return kv.decodeFloat("fontScale", 1.0f)
    }

    fun setFontScale(fontScale: Float): Boolean {
        val kv = MMKV.mmkvWithID(MKV_APP_INFO)
        return kv.encode("fontScale", fontScale)
    }

    fun isToppingExpanded(): Boolean {
        val kv = MMKV.mmkvWithID(MKV_APP_INFO)
        return kv.decodeBool("topping", true)
    }

    fun setToppingExpanded(expand: Boolean): Boolean {
        val kv = MMKV.mmkvWithID(MKV_APP_INFO)
        return kv.encode("topping", expand)
    }

    fun setParam(key: String?, `object`: Any) {
        when (`object`.javaClass.simpleName) {
            "String" -> allData().encode(key, `object` as String?)
            "Integer", "int" -> allData().encode(key, `object` as Int)
            "Boolean", "boolean" -> allData().encode(key, `object` as Boolean)
            "Float", "float" -> allData().encode(key, `object` as Float)
            "Long", "long" -> allData().encode(key, `object` as Long)
        }
    }

    fun getParam(key: String?, defaultObject: Any): Any? {
        when (defaultObject.javaClass.simpleName) {
            "String" -> return allData().getString(key, defaultObject as String)
            "Integer", "int" -> return allData().getInt(key, defaultObject as Int)
            "Boolean", "boolean" -> return allData().getBoolean(key, defaultObject as Boolean)
            "Float", "float" -> return allData().getFloat(key, defaultObject as Float)
            "Long", "long" -> return allData().getLong(key, defaultObject as Long)
        }
        return null
    }


    fun getGenerateNick(): String? {
        return mmkv.decodeString("generateNick", "")
    }

    fun saveGenerateNick(string: String?) {
        if (string == null) {
            mmkv.removeValueForKey("generateNick")
        } else {
            mmkv.encode("generateNick", string)
        }
    }

    fun isMarketInit24(): Boolean {
        val kv = MMKV.mmkvWithID(MKV_APP_INFO)
        return kv.decodeBool("MarketInit24", false)
    }

    fun setMarketInit24(first: Boolean): Boolean {
        val kv = MMKV.mmkvWithID(MKV_APP_INFO)
        return kv.encode("MarketInit24", first)
    }


    fun getMarketInit(): String? {
        return mmkv.decodeString("MarketInit", "")
    }

    fun saveMarketInit(string: String?) {
        if (string == null) {
            mmkv.removeValueForKey("MarketInit")
        } else {
            mmkv.encode("MarketInit", string)
        }
    }

    fun getGuide(): String? {
        return mmkv.decodeString("Guide", "")
    }

    fun saveGuide(string: String?) {
        if (string == null) {
            mmkv.removeValueForKey("Guide")
        } else {
            mmkv.encode("Guide", string)
        }
    }

    /****************************************************************/

    /**
     * 统一“切换”方法，添加/取消收藏。
     * gid的商品是否存在，如果存在则删除，不存在则添加。
     */
    fun toggleShouCang(good: ModTradeGoodDetailBean): Boolean {
        return updateListAndGetResult { list ->
            val index = list.indexOfFirst { it.gid == good.gid }
            if (index != -1) {
                list.removeAt(index)
                false
            } else {
                good.localSaveTime = TimeUtils.getNowString()
                list.add(good)
                true
            }
        }
    }

    private fun <T> updateListAndGetResult(action: (MutableList<ModTradeGoodDetailBean>) -> T): T {
        val list = getShouCangInfoList()
        val result = action(list)
        saveShouCangInfoList(list)
        return result
    }

    /**
     * 通过gid删除
     */
    @RequiresApi(Build.VERSION_CODES.N)
    fun removeShouCangInfoByGid(gid: String) {
        updateList { list ->
            list.removeIf { it.gid == gid }
        }
    }

    /**
     * 是否已收藏
     */
    fun isShouCangCollected(gid: String): Boolean =
        getShouCangInfoList().any { it.gid == gid }

    /**
     * 获取收藏列表
     */
    fun getShouCangInfoList(): MutableList<ModTradeGoodDetailBean> =
        mmkv.decodeString("shouCangInfoList", null)?.let {
            GsonUtils.fromJson(it, object : TypeToken<MutableList<ModTradeGoodDetailBean>>() {}.type)
        } ?: mutableListOf()

    /**
     * 保存收藏列表
     */
    private fun saveShouCangInfoList(list: List<ModTradeGoodDetailBean>) {
        mmkv.encode("shouCangInfoList", GsonUtils.toJson(list))
    }

    /**
     * 统一列表更新
     * 接收列表进行操作，并返回，表示列表是否被修改。
     */
    private fun updateList(action: (MutableList<ModTradeGoodDetailBean>) -> Boolean) {
        val list = getShouCangInfoList()
        if (action(list)) {
            saveShouCangInfoList(list)
        }
    }
    fun getPicPer(): String? {
        return mmkv.decodeString("PicPer", "")
    }

    fun savePicPer(string: String?) {
        if (string == null) {
            mmkv.removeValueForKey("PicPer")
        } else {
            mmkv.encode("PicPer", string)
        }
    }

    fun getSTORAGE(): String? {
        return mmkv.decodeString("STORAGE", "")
    }

    fun saveSTORAGE(string: String?) {
        if (string == null) {
            mmkv.removeValueForKey("STORAGE")
        } else {
            mmkv.encode("STORAGE", string)
        }
    }


}
