package com.chaoji.im.ui.xpop.pay

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chaoji.base.base.action.ClickAction
import com.chaoji.base.base.action.KeyboardAction
import com.chaoji.im.ui.layout.SettingBar
import com.chaoji.im.data.model.PayInfo
import com.chaoji.common.R
import com.chaoji.im.ui.xpop.pay.AdapterXPopupPay.Companion.ITEM_CHOICE
import com.chaoji.other.xpopup.core.BottomPopupView
import com.chaoji.other.xpopup.core.CenterPopupView
import com.chaoji.other.xpopup.util.XPopupUtils

@SuppressLint("ViewConstructor")
class XPopupCenterPay(context: Context, private var cancel: (() -> Unit)?, private var sure: ((payInfo: PayInfo) -> Unit)?) :
    CenterPopupView(context), ClickAction, KeyboardAction {
    override fun getImplLayoutId(): Int = R.layout.pay_center_dialog

    private val payList = mutableListOf(
        PayInfo(icon = R.drawable.pay_wechat, name = "微信支付", type = 1),
        PayInfo(icon = R.drawable.pay_alipay, name = "支付宝支付", type = 2)
    )
    private lateinit var payRv: RecyclerView
    private lateinit var infoLL: LinearLayout
    private lateinit var payType: SettingBar
    private lateinit var payInfo: SettingBar
    private lateinit var choicePay: PayInfo
    var payAdapter = AdapterXPopupPay(payList)




    @SuppressLint("SetTextI18n")
    override fun onCreate() {
        super.onCreate()
        infoLL = findViewById(R.id.infoLL)!!
        payType = findViewById(R.id.payType)!!
        payInfo = findViewById(R.id.payInfo)!!
        payRv = findViewById(R.id.payRecyclerView)!!

//        payRv.run {
//            val x = (resources.displayMetrics.density * 2).toInt()
//            addItemDecoration(SpacingItemDecorator(x))
//        }
        payRv.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        payRv.adapter = payAdapter

        setOnClickListener(R.id.cancel, R.id.confirm, R.id.payType, R.id.payInfo)

        payAdapter.setOnItemClickListener { adapter, view, position ->
            val payInfo = adapter.data[position] as PayInfo
            payInfo.isChoice = !payInfo.isChoice
            choicePay = payInfo
            adapter.data.forEachIndexed { index, any ->
                any as PayInfo
                any.isChoice = index == position
                payAdapter.notifyItemChanged(index, ITEM_CHOICE)
            }
            showPayType(false)
            payType.setRightText(choicePay.name)
//            payAdapter.data[position].isChoice = payInfo.isChoice
//            payAdapter.data[position] = payInfo
//            payAdapter.notifyItemChanged(position,ITEM_CHOICE)
        }

        payList[0].isChoice = true
        choicePay = payList[0]
        payType.setRightText(choicePay.name)
        payAdapter.notifyItemChanged(0, ITEM_CHOICE)
        //payAdapter.setList(payList)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.payType -> {
                showPayType(true)
            }

            R.id.payInfo -> {

            }

            R.id.cancel -> {
                cancel?.invoke()
                dismiss()
            }

            R.id.confirm -> {
                sure?.invoke(choicePay)
                dismiss()
            }


        }
    }

    override fun dismiss() {
        super.dismiss()
        hideKeyboard(this)
    }

    private fun showPayType(boolean: Boolean) {
        if (boolean) {
            fadeInPayRv(payRv)
        } else {
            fadeOutPayRv(payRv)
        }
    }

    private fun fadeInPayRv(view: View, duration: Long = 220) {
        infoLL.visibility = View.GONE
        view.apply {
            alpha = 0f
            visibility = View.VISIBLE
            animate()
                .alpha(1f)
                .setDuration(duration)
                .setListener(null)
        }
    }

    private fun fadeOutPayRv(view: View,duration: Long = 220) {
        view.animate()
            .alpha(0f)
            .setDuration(duration)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    view.visibility = View.GONE
                    infoLL.visibility = View.VISIBLE
                }
            })
    }

    override fun getPopupWidth(): Int {
        return super.getPopupWidth()
    }

    override fun getPopupHeight(): Int {
        return super.getPopupHeight()
    }

    override fun getMaxWidth(): Int {
        return (XPopupUtils.getScreenWidth(context) * 0.4f).toInt()
    }

    override fun getMaxHeight(): Int {
        return (XPopupUtils.getScreenHeight(context) * 0.9f).toInt()
    }

}