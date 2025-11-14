package com.box.common

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.SystemClock
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.box.common.data.model.ModUserInfo
import com.box.common.glide.GlideApp
import com.box.common.ui.layout.SettingBar
import com.box.common.ui.view.ClearEditText
import com.box.common.ui.view.SwitchButton
import com.box.common.ui.widget.ViewLinearLayoutCommonTitleContent
import com.box.common.utils.CustomCornersTransformation
import com.box.other.blankj.utilcode.util.Logs
import com.box.other.blankj.utilcode.util.ResourceUtils
import com.box.other.blankj.utilcode.util.SizeUtils
import com.box.other.blankj.utilcode.util.StringUtils
import com.box.other.blankj.utilcode.util.TimeUtils
import com.box.other.hjq.titlebar.TitleBar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.box.com.R as RC

object ConstantBindingAdapter {

    /**
     * 这是一个功能强大的、统一的图片加载 BindingAdapter，用于替换所有 loadRound...、loadImage... 等重复方法。
     *
     * 它支持:
     * - 加载 URL, 本地资源ID, Drawable, 或 Uri
     * - 设置任意圆角 (单位: dp)
     * - 设置是否为圆形
     * - 自定义占位图和错误图
     * - 加载失败或来源为空时，自动隐藏 View
     * - 只设置特定角落为圆角 (如 "top", "bottom")
     */
    @BindingAdapter(
        value = [
            "imageLoader", // 优先级最高的加载源 (Any? 类型)
            "imageUrl",    // 字符串URL (如果 imageLoader 未设置)
            "imageRes",    // 资源ID (Int) (如果 imageLoader 和 imageUrl 未设置)
            "imageUri",    // Uri (如果前三者未设置)
            "placeholder", // 占位图 (Int或Drawable)
            "error",       // 错误图 (Int或Drawable)
            "cornerRadius",// 圆角半径 (Float, 单位dp)
            "isCircle",    // 是否为圆形 (Boolean)
            "hideOnNullOrError", // 来源为空或加载失败时是否隐藏 (Boolean)
            "cornerType",  // 圆角类型: "all", "top", "bottom", "left", "right"
            "dontAnimate"  // 是否关闭动画 (Boolean)
        ],
        requireAll = false // 所有参数都是可选的
    )
    @JvmStatic
    fun setAdvancedImageLoader(
        view: ImageView,
        imageLoader: Any?,
        imageUrl: String?,
        imageRes: Int?,
        imageUri: Uri?,
        placeholder: Any?,
        error: Any?,
        cornerRadius: Float?,
        isCircle: Boolean?,
        hideOnNullOrError: Boolean?,
        cornerType: String?,
        dontAnimate: Boolean?
    ) {
        val context = view.context

        // 确定加载目标 (优先级: imageLoader > imageUrl > imageRes > imageUri)
        val loadTarget: Any? = when {
            imageLoader != null -> imageLoader
            !imageUrl.isNullOrEmpty() -> imageUrl
            imageRes != null && imageRes != 0 -> imageRes
            imageUri != null -> imageUri
            else -> null // 没有任何东西可加载
        }

        // 确定占位图和错误图
        val effectivePlaceholder = when (placeholder) {
            is Drawable -> placeholder
            is Int -> ContextCompat.getDrawable(context, placeholder)
            else -> ContextCompat.getDrawable(context, RC.drawable.image_loading_ic) // 默认
        }
        val effectiveError = when (error) {
            is Drawable -> error
            is Int -> ContextCompat.getDrawable(context, error)
            else -> ContextCompat.getDrawable(context, RC.drawable.status_error_ic) // 默认
        }

        // 处理加载目标为空的情况
        if (loadTarget == null) {
            if (hideOnNullOrError == true) {
                view.visibility = View.GONE
            } else {
                view.visibility = View.VISIBLE
                view.setImageDrawable(effectiveError)
            }
            return
        }

        //  目标不为空，开始使用 Glide
        view.visibility = View.VISIBLE
        val glideRequest = GlideApp.with(view)
            .load(loadTarget)
            .placeholder(effectivePlaceholder)
            .error(effectiveError)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)

        //  应用加载失败的隐藏逻辑
        if (hideOnNullOrError == true) {
            glideRequest.listener(object : com.bumptech.glide.request.RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: com.bumptech.glide.load.engine.GlideException?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    view.visibility = View.GONE
                    return false // 返回 false 让 error() 处理器接管
                }
                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable>?,
                    dataSource: com.bumptech.glide.load.DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false // 返回 false 让 Glide 正常设置图片
                }
            })
        }

        //  应用变换 (圆角, 圆形等)
        val radiusInPixels = SizeUtils.dp2px(cornerRadius ?: 0f)

        when {
            isCircle == true -> {
                glideRequest.transform(CircleCrop())
            }
            radiusInPixels > 0 -> {
                val cornerEnum = when (cornerType) {
                    "top" -> CustomCornersTransformation.CornerType.TOP
                    "bottom" -> CustomCornersTransformation.CornerType.BOTTOM
                    "left" -> CustomCornersTransformation.CornerType.LEFT
                    "right" -> CustomCornersTransformation.CornerType.RIGHT
                    else -> CustomCornersTransformation.CornerType.ALL
                }

                if (cornerEnum == CustomCornersTransformation.CornerType.ALL) {
                    glideRequest.transform(CenterCrop(), RoundedCorners(radiusInPixels))
                } else {
                    glideRequest.transform(CenterCrop(), CustomCornersTransformation(radiusInPixels.toFloat(), cornerEnum))
                }
            }
            else -> {
                // 默认进行 CenterCrop
                glideRequest.transform(CenterCrop())
            }
        }

        // 处理动画
        if (dontAnimate == true) {
            glideRequest.dontAnimate()
        }

        // 加载到视图
        glideRequest.into(view)
    }



    @BindingAdapter(value = ["checkChange"])
    @JvmStatic
    fun checkChange(checkbox: CheckBox, listener: CompoundButton.OnCheckedChangeListener) {
        checkbox.setOnCheckedChangeListener(listener)
    }

    @BindingAdapter(value = ["showView"])
    @JvmStatic
    fun showView(view: View, boolean: Boolean) {
        if (boolean) {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.GONE
        }
    }

    @BindingAdapter(value = ["circleImageUrl"])
    @JvmStatic
    fun circleImageUrl(view: ImageView, url: String) {
        GlideApp.with(view.context.applicationContext)
            .load(url)
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .transition(DrawableTransitionOptions.withCrossFade(500))
            .into(view)
    }


    @BindingAdapter(value = ["editable"])
    @JvmStatic
    fun editable(view: ViewLinearLayoutCommonTitleContent, boolean: Boolean?) {
        boolean?.let {
            view.editable(boolean)
        }
    }

    @BindingAdapter(value = ["common_edit_content"])
    @JvmStatic
    fun commonEditContent(view: ViewLinearLayoutCommonTitleContent, content: String?) {
        content?.let {
            view.content = content
        }
    }


    fun scale(img: ImageView, sourceW: Int, sourceH: Int, baseDPW: Int) {
        var sourceW = sourceW
        var sourceH = sourceH
        val pictureWidth: Int = SizeUtils.dp2px(baseDPW.toFloat())


        val trulyWidth: Int
        val trulyHeight: Int
        if (sourceW == 0) {
            sourceW = 100
        }
        if (sourceH == 0) {
            sourceH = 100
        }

        val useW = sourceW > sourceH

        if (useW) {
            if (pictureWidth > sourceW) {
                trulyWidth = sourceW
                trulyHeight = sourceH
            } else {
                trulyWidth = pictureWidth
                trulyHeight = trulyWidth * sourceH / sourceW
            }
        } else {
            if (pictureWidth > sourceH) {
                trulyWidth = sourceW
                trulyHeight = sourceH
            } else {
                trulyHeight = pictureWidth
                trulyWidth = trulyHeight * sourceW / sourceH
            }
        }
        val params = img.layoutParams
        params.width = trulyWidth
        params.height = trulyHeight
        img.layoutParams = params
    }



    @BindingAdapter("tint")
    @JvmStatic
    fun setImageViewTint(imageView: ImageView, colorStateList: ColorStateList?) {
        imageView.imageTintList = colorStateList
    }

    @BindingAdapter("tint")
    @JvmStatic
    fun setImageViewTint(imageView: ImageView, color: Int) {
        imageView.imageTintList = ColorStateList.valueOf(color)
    }

    @BindingAdapter(value = ["loadResNameImage"])
    @JvmStatic
    fun loadResNameImage(view: ImageView, resName: String?) {
        if (resName.isNullOrEmpty()) {
            view.setImageResource(RC.drawable.status_error_ic)
        } else {
            val radiusInDp = 10
            val density = appContext.resources.displayMetrics.density
            val radiusInPixels = (radiusInDp * density + 0.5f).toInt()
            GlideApp.with(appContext)
                .load(ResourceUtils.getDrawableIdByName(resName))
                .transform(CenterCrop(), RoundedCorners(radiusInPixels))
                .error(RC.drawable.status_error_ic)
                .into(view)
        }
    }



    @BindingAdapter(value = ["loadResImage"])
    @JvmStatic
    fun loadResImage(view: ImageView, resId: Int) {
        GlideApp.with(appContext)
            .load(resId)
            //.transform(RoundedCorners(ResourceUtils.getDimenIdByName("idp_15")))
            .error(RC.drawable.status_error_ic)
            .into(view)
    }

    @BindingAdapter(value = ["setDText"])
    @JvmStatic
    fun setDText(view: TextView, text: String?) {
        view.text = text
    }

    @BindingAdapter(value = ["vmRightText"])
    @JvmStatic
    fun vmRightText(view: SettingBar, text: String?) {
        view.setRightText(text)
    }

    @BindingAdapter(value = ["vmTitleText"])
    @JvmStatic
    fun vmTitleText(view: TitleBar, text: String?) {
        view.title = text
    }

    @BindingAdapter(value = ["switchButtonState"])
    @JvmStatic
    fun switchButtonState(view: SwitchButton, boolean: Boolean?) {
        view.setChecked(boolean ?: false)
    }

    @BindingAdapter("setFormattedTimestamp")
    @JvmStatic
    fun setFormattedTimestamp(view: TextView, timestamp: String?) {
        if (StringUtils.isEmpty(timestamp)) {
            view.text = ""
            return
        }
        Logs.e("setFormattedTimestamp:$timestamp")
        val timeLong = timestamp?.toLongOrNull()?.times(1000)
        val pattern = "yyyy-MM-dd HH:mm:ss"
        view.text = timeLong?.let { TimeUtils.millis2String(it, pattern) }
    }

    @BindingAdapter("setFormattedTimestamp")
    @JvmStatic
    fun setFormattedTimestamp(view: TextView, timestamp: Long?) {
        val timeLong = timestamp?.times(1000)
        val pattern = "yyyy-MM-dd HH:mm:ss"
        view.text = timeLong?.let { TimeUtils.millis2String(it, pattern) }
    }

    /**
     * 设置交易笔数文本，并将数字部分标为红色。
     *
     * 用法:
     * <TextView
     * ...
     * app:formattedTradeCount="@{tradeRankBean.tradecount}"
     * ... />
     *
     * @param view TextView控件
     * @param count 交易笔数 (Int类型)
     */
    @BindingAdapter("formattedTradeCount")
    @JvmStatic
    fun setFormattedTradeCount(view: TextView, count: String?) {
        if (count == null) {
            view.text = "" // 或者设置一个默认文本
            return
        }
        val prefix = "最近"
        val suffix = "笔成交"
        val countStr = count.toString()
        val fullText = "$prefix$countStr$suffix"

        val spannableString = SpannableString(fullText)

        val startIndex = prefix.length
        val endIndex = startIndex + countStr.length

        val redColor = ContextCompat.getColor(view.context, RC.color.red)
        spannableString.setSpan(
            ForegroundColorSpan(redColor),
            startIndex,
            endIndex,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        view.text = spannableString
    }




    /**
     * 为任何 TextView 或其子类（包括你的 DrawableTextView）设置 drawableEndCompat 属性。
     *
     * @param view 目标视图
     * @param drawable 要设置的 Drawable 对象，注意这里是可空的 Drawable?
     * 因为你的表达式可能会返回 null。
     */
    @BindingAdapter("diyDrawableEndCompat")
    @JvmStatic
    fun setDrawableEnd(view: TextView, drawable: Drawable?) {
        // 获取视图上已有的其他 Compound Drawables
        val drawables = view.compoundDrawablesRelative
        // 只更新 end 位置的 drawable，保持其他位置不变
        view.setCompoundDrawablesRelativeWithIntrinsicBounds(
            drawables[0], // start
            drawables[1], // top
            drawable,     // end
            drawables[3]  // bottom
        )
    }

    @BindingAdapter(value = ["ceHint"])
    @JvmStatic
    fun setHint(view: ClearEditText, hint: String?) {
        view.hint = hint
    }

    @BindingAdapter(value = ["textValue"])
    @JvmStatic
    fun setTextValue(view: ClearEditText, value: String?) {
        // 避免无限循环：只有在 View 当前显示的值与新值不同时才更新
        if (view.getTextValue() != value) {
            view.setTextValue(value)
        }
    }

    @InverseBindingAdapter(attribute = "textValue", event = "textValueAttrChanged")
    @JvmStatic
    fun getTextValue(view: ClearEditText): String {
        return view.getTextValue()
    }

    @BindingAdapter(value = ["textValueAttrChanged"])
    @JvmStatic
    fun setTextWatcher(view: ClearEditText, listener: InverseBindingListener?) {
        if (listener != null) {
            // 假设您的 ClearEditText 有一个自定义的监听器或使用标准的 TextWatcher
            // 您需要在这里设置一个监听器，并在文本变化时调用 listener.onChange()
            view.setTextChangeListener {
                listener.onChange()
            }
        }
    }

    @BindingAdapter("ratingText")
    @JvmStatic
    fun setRatingText(textView: TextView, score: Float?) {
        score?.let {
            textView.text = it.toString()
        }
    }

    @BindingAdapter("loadImagePickMediaUri")
    @JvmStatic
    fun loadImagePickMediaUri(imageView: ImageView, uri: Uri?) {
        if (uri != null) {
            GlideApp.with(imageView.context)
                .load(uri)
                .transform(CenterCrop(), RoundedCorners(15))
                .placeholder(RC.drawable.loading_spinner_rotate) // 加载中的占位图
                .error(RC.drawable.image_error_ic)       // 加载失败的占位图
                .into(imageView)
        } else {
            imageView.setImageResource(RC.drawable.mod_add_pic_2)
        }
    }

    @BindingAdapter(value = ["setDynamicBackground"])
    @JvmStatic
    fun setDynamicBackground(view: View, position: Int) {
        // 定义一个 drawable 资源数组，使用 intArrayOf()
        val drawableResources = intArrayOf(
            RC.drawable.bg_item_color_1,
            RC.drawable.bg_item_color_2,
            RC.drawable.bg_item_color_3,
            RC.drawable.bg_item_color_4,
        )
        // 根据 position 对数组长度取模，得到对应的 drawable 索引
        val drawableResId = drawableResources[position % drawableResources.size]
        // 设置背景
        view.setBackgroundResource(drawableResId)
    }


    /**
     * 智能加载用户头像
     * 优先级:
     * 1. ModUserInfo.avatar (网络URL)
     * 2. ModUserInfo.localAvatarResName (本地随机头像)
     * 3. 默认占位图
     */
    @BindingAdapter(value = ["userAvatarInfo"])
    @JvmStatic
    fun setUserAvatarInfo(imageView: ImageView, userInfo: ModUserInfo?) {
        // 默认的占位图
        val placeholder = RC.drawable.mod_user_icon1
        if (userInfo == null) {
            imageView.setImageResource(placeholder)
            return
        }
        // 优先级 1: 检查网络头像 URL
        if (userInfo.avatar.isNotEmpty()) {
            Glide.with(imageView.context)
                .load(userInfo.avatar)
                .placeholder(placeholder)
                .error(placeholder)
                .circleCrop() // (如果你想要圆形头像)
                .into(imageView)
        }
        // 优先级 2: 检查本地随机头像
        else if (userInfo.localAvatarResName != null) {
            val resId = imageView.context.resources.getIdentifier(
                userInfo.localAvatarResName,
                "drawable",
                imageView.context.packageName
            )

            if (resId != 0) {
                imageView.setImageResource(resId)
            } else {
                // 如果资源名错误，显示默认
                imageView.setImageResource(placeholder)
            }
        }
        // 优先级 3: 默认
        else {
            imageView.setImageResource(placeholder)
        }
    }


}