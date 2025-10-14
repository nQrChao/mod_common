package com.chaoji.im.ui.xpop

import android.annotation.SuppressLint
import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chaoji.base.base.action.ClickAction
import com.chaoji.common.R
import com.chaoji.im.impl.CommonSelect
import com.chaoji.other.xpopup.core.AttachPopupView

@SuppressLint("ViewConstructor")
class XPopupAttachMessageOperationPop(context: Context, var commonSelect:MutableList<CommonSelect>, var call:(select:CommonSelect)->Unit) :
    AttachPopupView(context), ClickAction {
    override fun getImplLayoutId(): Int = R.layout.xpopup_message_operation

    private val operationAdapter=MessageOperationAdapter(commonSelect)

    override fun onCreate() {
        super.onCreate()

        findViewById<RecyclerView>(R.id.rc)?.run {
            layoutManager=GridLayoutManager(context,if (commonSelect.size>=4) 4 else commonSelect.size)
            adapter=operationAdapter

        }
        operationAdapter.setOnItemClickListener { adapter, view, position ->
            call.invoke(adapter.data[position] as CommonSelect)
            dismiss()
        }
    }


}