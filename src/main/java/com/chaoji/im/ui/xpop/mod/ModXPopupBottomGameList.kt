package com.chaoji.im.ui.xpop.mod

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.chaoji.base.base.action.ClickAction
import com.chaoji.base.base.action.KeyboardAction
import com.chaoji.common.R
import com.chaoji.im.appContext
import com.chaoji.im.data.model.ModGameAppletsInfo
import com.chaoji.im.glide.GlideApp
import com.chaoji.im.ui.widget.marqueeview.MarqueeView
import com.chaoji.im.ui.xpop.mod.adapter.ModGameListAAdapter
import com.chaoji.other.hjq.shape.drawable.ShapeDrawable
import com.chaoji.other.xpopup.core.BottomPopupView
import com.chaoji.other.xpopup.util.XPopupUtils

@SuppressLint("ViewConstructor")
class ModXPopupBottomGameList(
    context: Context,
    gameAppList: MutableList<ModGameAppletsInfo>,
    gameTips: MutableList<String>,
    var item: (select: ModGameAppletsInfo) -> Unit,
    var play: (select: ModGameAppletsInfo) -> Unit,
    private var lingqu: ((info: String) -> Unit),
    var onDismiss: () -> Unit,
) :
    BottomPopupView(context), ClickAction, KeyboardAction {
    //private val mBinding: ModXpopupBottomGameListBinding = DataBindingUtil.inflate(LayoutInflater.from(context),  R.layout.mod_xpopup_bottom_game_list, this, true)
    private var gameAppListAdapter = ModGameListAAdapter(gameAppList)
    private var bottomView:View? = null
    private var topPic:ImageView? = null
    private var mShapeDrawable: ShapeDrawable? = null
    private var mRadiusSize = 0f
    private var gameTip = gameTips
    override fun getImplLayoutId(): Int = R.layout.mod_xpopup_bottom_game_list
    override fun onCreate() {
        super.onCreate()
        setOnClickListener(R.id.lingqu,R.id.back)

        findViewById<RecyclerView>(R.id.recyclerView)?.run {
            layoutManager = LinearLayoutManager(context)
            adapter = gameAppListAdapter
            gameAppListAdapter.addChildClickViewIds(R.id.play)
            gameAppListAdapter.setOnItemClickListener { adapter, _, position ->
                item.invoke(adapter.data[position] as ModGameAppletsInfo)
                dismiss()
            }

            gameAppListAdapter.setOnItemChildClickListener { adapter, view, position ->
                if (view.id == R.id.play) {
                    play.invoke(adapter.data[position] as ModGameAppletsInfo)
                    dismiss()
                }

            }

        }

        findViewById<MarqueeView<String>>(R.id.marqueeview)?.run {
            startWithList(gameTip)
        }


        topPic = findViewById(R.id.topPic)
        bottomView = findViewById(R.id.bottomView)

        val requestOptions = RequestOptions().transform(RoundedCorners(18)) // 设置圆角半径
        GlideApp.with(appContext)
            .load(R.drawable.mod_game_list_top_bg)
            .apply(requestOptions)
            .into(topPic!!)

//        mRadiusSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15f, resources.displayMetrics)
//
//        val shapeSize =
//            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150f, resources.displayMetrics)
//                .toInt()
//
//        mShapeDrawable = ShapeDrawable()
//
//        mShapeDrawable?.run {
//            setSolidColor(ContextCompat.getColor(context, android.R.color.white))
//            setType(ShapeType.RECTANGLE)
//            setRadius(mRadiusSize,mRadiusSize,0f,0f)
//            setShadowColor(ColorUtils.getColor(R.color.color_0079fb))
//            setRingInnerRadiusSize(5)
//            setRingThicknessSize(5)
//            setShadowOffsetY(-10)
//            setShadowSize(0,0,50,0,1)
//
//        }
//        mShapeDrawable?.intoBackground(bottomView)

    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.lingqu -> {
                lingqu.invoke("去领取")
                dismiss()
            }

            R.id.back ->{
                dismiss()
            }

        }
    }

    override fun dismiss() {
        super.dismiss()
        onDismiss.invoke()
        hideKeyboard(this)
    }


    override fun getMaxHeight(): Int {
        return (XPopupUtils.getScreenHeight(context) * 0.88f).toInt()
    }


}