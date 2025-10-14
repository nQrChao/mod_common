package com.chaoji.im.ui.xpop

import android.annotation.SuppressLint
import android.content.Context
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chaoji.other.blankj.utilcode.util.ColorUtils
import com.chaoji.other.blankj.utilcode.util.Logs
import com.chaoji.other.blankj.utilcode.util.StringUtils
import com.chaoji.other.blankj.utilcode.util.TimeUtils
import com.chaoji.base.state.ResultState
import com.chaoji.im.data.model.AIChat
import com.chaoji.im.sdk.ImSDK
import com.chaoji.common.R
import com.chaoji.im.sdk.eventViewModel
import com.chaoji.im.ui.adapter.AiListAdapter
import com.chaoji.other.xpopup.XPopup
import com.chaoji.other.xpopup.core.DrawerPopupView
import java.text.SimpleDateFormat
import java.util.Collections


@SuppressLint("ViewConstructor")
class XPopupDrawerAiList(context: Context, private var create: (() -> Unit)?, private var dismiss: (() -> Unit)?) :
    DrawerPopupView(context) {
    override fun getImplLayoutId(): Int = R.layout.xpopup_ai_drawer

    lateinit var mViewModel: XPopupDrawerAiListModel
    lateinit var searchView: EditText
    lateinit var clearView: TextView
    lateinit var newChatView: TextView
    var aiList = mutableListOf<AIChat>()
    lateinit var recyclerView: RecyclerView
    lateinit var aiListAdapter: AiListAdapter

    private fun initObserve() {
        mViewModel.aiChatList.observe(this) {
            aiListAdapter.setList(it)
        }

        mViewModel.aiChatCreateResult.observe(this) {
            when (it) {
                is ResultState.Success -> {
                    Logs.e("创建aiChat成功")
                }

                is ResultState.Error -> {
                    Logs.e("创建aiChat失败")
                }

                else -> {}
            }
            mViewModel.chatList()
        }

        mViewModel.aiChatNameResult.observe(this) {
            when (it) {
                is ResultState.Success -> {
                    Logs.e("修改aiChat名称成功")
                }

                is ResultState.Error -> {
                    Logs.e("修改aiChat名称失败")
                }

                else -> {}
            }
            mViewModel.chatList()
        }

        mViewModel.aiChatDelResult.observe(this) {
            when (it) {
                is ResultState.Success -> {
                    Logs.e("删除aiChat成功")
                }

                is ResultState.Error -> {
                    Logs.e("删除aiChat失败")
                }

                else -> {}
            }
            mViewModel.chatList()
        }

        mViewModel.aiChatDelAllResult.observe(this) {
            when (it) {
                is ResultState.Success -> {
                    Logs.e("删除所有aiChat成功")
                }

                is ResultState.Error -> {
                    Logs.e("删除所有aiChat失败")
                }

                else -> {}
            }
            mViewModel.chatList()
        }

        mViewModel.aiChatListResult.observe(this) {
            when (it) {
                is ResultState.Success -> {
                    if (it.data != null) {
                        val ageComparator: Comparator<AIChat> = Comparator.comparingLong(AIChat::time)
                        val lastItem: AIChat = Collections.max(it.data, ageComparator)
                        mViewModel.aiChatList.value = resData(lastItem, it.data)
                    }
                }

                is ResultState.Error -> {}
                else -> {}
            }
        }

        mViewModel.aiChatMessageResult.observe(this) {
            when (it) {
                is ResultState.Success -> {
                    eventViewModel.updateAiChatFragment.value = mViewModel.chatMessage.value
                    ImSDK.eventViewModelInstance.setMainCurrentItem.value = 0
                    dismiss()

                }

                is ResultState.Error -> {}
                else -> {}
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        create?.invoke()
        mViewModel = ViewModelProvider((context as FragmentActivity))[XPopupDrawerAiListModel::class.java]

        clearView = findViewById(R.id.clear)
        searchView = findViewById(R.id.search_edit)
        newChatView = findViewById(R.id.new_btn)
        recyclerView = findViewById(R.id.recyclerView)

        aiListAdapter = AiListAdapter(aiList)
        aiListAdapter.addChildClickViewIds(R.id.del)
        aiListAdapter.addChildClickViewIds(R.id.edit)
        aiListAdapter.setOnItemClickListener { adapter, view, position ->
            val entity: AIChat = adapter.data[position] as AIChat
            mViewModel.chatMessage.value = entity
            aiListAdapter.setList(resData(entity, adapter.data as MutableList<AIChat>))
            mViewModel.chatMessage(entity.id)
        }

        aiListAdapter.setOnItemChildClickListener { adapter, view, position ->
            val entity: AIChat = adapter.data[position] as AIChat
            if (view.id == R.id.del) {
                XPopup.Builder(context)
                    .isDestroyOnDismiss(true)
                    .hasStatusBar(true)
                    .animationDuration(10)
                    .navigationBarColor(ColorUtils.getColor(R.color.xpop_shadow_color))
                    .isLightStatusBar(true)
                    .hasNavigationBar(true)
                    .asConfirm(
                        "提示", "是否清除该对话记录？",
                        "取消", "确定",
                        {
                            mViewModel.chatDel(entity.id)
                        }, null, false, R.layout.xpopup_confirm
                    ).show()

            } else if (view.id == R.id.edit) {
                XPopup.Builder(context)
                    .isDestroyOnDismiss(true)
                    .hasStatusBar(true)
                    .isLightStatusBar(true)
                    .hasNavigationBar(true)
                    .asCustom(XPopupCenterCommonEditText(context, 18, "标题", "", "输入新标题名称", null) { it ->
                        if (!StringUtils.isEmpty(it)) {
                            mViewModel.chatName(entity.id, it)
                        }
                    })
                    .show()
            }
        }

        recyclerView.run {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = aiListAdapter
        }

        clearView.setOnClickListener {
            XPopup.Builder(context)
                .isDestroyOnDismiss(true)
                .hasStatusBar(true)
                .animationDuration(10)
                .navigationBarColor(ColorUtils.getColor(R.color.xpop_shadow_color))
                .isLightStatusBar(true)
                .hasNavigationBar(true)
                .asConfirm(
                    "提示", "是否清除所有AI对话记录？",
                    "取消", "确定",
                    {
                        mViewModel.chatDelAll()
                    }, null, false, R.layout.xpopup_confirm
                ).show()
        }

        newChatView.setOnClickListener {
            XPopup.Builder(context)
                .isDestroyOnDismiss(true)
                .hasStatusBar(true)
                .isLightStatusBar(true)
                .hasNavigationBar(true)
                .asCustom(XPopupCenterCommonEditText(context, 18, "标题", "", "输入标题名称", null) { it ->
                    if (!StringUtils.isEmpty(it)) {
                        mViewModel.chatCreate(it)
                    }
                })
                .show()
        }

        initObserve()
        mViewModel.chatList()
    }


    private fun resData(lastItem: AIChat, list: MutableList<AIChat>): MutableList<AIChat> {
        if (list.isNotEmpty()) {
            for (i in list) {
                i.time = TimeUtils.string2Millis(i.last_response_at, SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")) / 1000
            }
            for (i in list) {
                i.select = lastItem.id == i.id
            }
        }
        return list
    }


    override fun getInternalFragmentNames(): List<String> {
        return mutableListOf()
    }

    override fun onShow() {
        super.onShow()
    }

    override fun onDismiss() {
        super.onDismiss()
        dismiss?.invoke()
    }
}