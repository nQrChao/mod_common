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
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.box.common.glide.GlideApp
import com.box.common.ui.layout.SettingBar
import com.box.common.ui.view.SwitchButton
import com.box.common.ui.widget.ViewLinearLayoutCommonTitleContent
import com.box.common.utils.CustomCornersTransformation
import com.box.other.blankj.utilcode.util.Logs
import com.box.other.blankj.utilcode.util.ResourceUtils
import com.box.other.blankj.utilcode.util.SizeUtils
import com.box.other.blankj.utilcode.util.StringUtils
import com.box.other.blankj.utilcode.util.TimeUtils
import com.box.other.hjq.titlebar.TitleBar
import com.box.com.R as RC

object CustomBindAdapter {
    @BindingAdapter(value = ["checkChange"])
    @JvmStatic
    fun checkChange(checkbox: CheckBox, listener: CompoundButton.OnCheckedChangeListener) {
        checkbox.setOnCheckedChangeListener(listener)
    }

    @BindingAdapter(value = ["isShowView"])
    @JvmStatic
    fun showView(view: View, boolean: Boolean) {
        if (boolean) {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.GONE
        }
    }

    @BindingAdapter(value = ["noRepeatClick"])
    @JvmStatic
    fun setOnClick(view: View, clickListener: () -> Unit) {
        val mHits = LongArray(2)
        view.setOnClickListener {
            System.arraycopy(mHits, 1, mHits, 0, mHits.size - 1)
            mHits[mHits.size - 1] = SystemClock.uptimeMillis()
            if (mHits[0] < SystemClock.uptimeMillis() - 1500) {
                clickListener.invoke()
            }
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


        val _trulyWidth: Int
        val _trulyHeight: Int
        if (sourceW == 0) {
            sourceW = 100
        }
        if (sourceH == 0) {
            sourceH = 100
        }

        val useW = sourceW > sourceH

        if (useW) {
            if (pictureWidth > sourceW) {
                _trulyWidth = sourceW
                _trulyHeight = sourceH
            } else {
                _trulyWidth = pictureWidth
                _trulyHeight = _trulyWidth * sourceH / sourceW
            }
        } else {
            if (pictureWidth > sourceH) {
                _trulyWidth = sourceW
                _trulyHeight = sourceH
            } else {
                _trulyHeight = pictureWidth
                _trulyWidth = _trulyHeight * sourceW / sourceH
            }
        }
        val params = img.layoutParams
        params.width = _trulyWidth
        params.height = _trulyHeight
        img.layoutParams = params
    }


    @BindingAdapter(value = ["loadRoundImage"])
    @JvmStatic
    fun loadRoundImage(view: ImageView, url: String) {
        if (url == "add") {
            GlideApp.with(appContext)
                .load(com.box.com.R.drawable.add_white_ic)
                .transform(RoundedCorners(ResourceUtils.getDimenIdByName("idp_15")))
                .error(com.box.com.R.drawable.status_error_ic)
                .into(view)
        } else {
            GlideApp.with(appContext)
                .load(url)
                .transform(RoundedCorners(ResourceUtils.getDimenIdByName("idp_15")))
                .error(com.box.com.R.drawable.status_error_ic)
                .into(view)
        }
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


    @BindingAdapter(value = ["loadImage1"])
    @JvmStatic
    fun loadImage1(view: ImageView, url: String) {
        if (url == "add") {
            GlideApp.with(appContext)
                .load(com.box.com.R.drawable.add_white_ic)
                .error(com.box.com.R.drawable.status_error_ic)
                .into(view)
        } else {
            GlideApp.with(appContext)
                .load(url)
                .error(com.box.com.R.drawable.status_error_ic)
                .into(view)
        }
    }

    @BindingAdapter(value = ["loadRoundSizeGifImage"])
    @JvmStatic
    fun loadRoundSizeGifImage(view: ImageView, url: String) {
        val requestOptions = RequestOptions().transform(RoundedCorners(15)) // 设置圆角半径
        GlideApp.with(appContext)
            .load(url)
            .apply(requestOptions)
            .error(com.box.com.R.drawable.status_error_ic)
            .into(view)
    }

    @BindingAdapter(value = ["loadRound10Image"])
    @JvmStatic
    fun loadRound10Image(view: ImageView, resId: Int?) {
        val radiusInDp = 5
        val density = appContext.resources.displayMetrics.density
        val radiusInPixels = (radiusInDp * density + 0.5f).toInt()

        GlideApp.with(appContext)
            .load(resId)
            .transform(CenterCrop(), RoundedCorners(radiusInPixels))
            .error(com.box.com.R.drawable.status_error_ic)
            .into(view)
    }

    @BindingAdapter(value = ["loadRound10Image"])
    @JvmStatic
    fun loadRound10Image(view: ImageView, url: String?) {
        if (url.isNullOrEmpty()) {
            view.setImageResource(com.box.com.R.drawable.status_error_ic)
        } else {
            val radiusInDp = 5
            val density = appContext.resources.displayMetrics.density
            val radiusInPixels = (radiusInDp * density + 0.5f).toInt()

            GlideApp.with(appContext)
                .load(url)
                .transform(CenterCrop(), RoundedCorners(radiusInPixels))
                .error(com.box.com.R.drawable.status_error_ic)
                .into(view)
        }
    }

    @BindingAdapter(value = ["loadRound11Image"])
    @JvmStatic
    fun loadRound11Image(view: ImageView, drawable: Drawable?) {
        val radiusInDp = 5
        val density = appContext.resources.displayMetrics.density
        val radiusInPixels = (radiusInDp * density + 0.5f).toInt()

        GlideApp.with(appContext)
            .load(drawable)
            .transform(CenterCrop(), RoundedCorners(radiusInPixels))
            .error(com.box.com.R.drawable.image_loading_ic)
            .into(view)
    }

    @BindingAdapter(value = ["loadRound11Image"])
    @JvmStatic
    fun loadRound11Image(view: ImageView, resId: Int?) {
        val radiusInDp = 5
        val density = appContext.resources.displayMetrics.density
        val radiusInPixels = (radiusInDp * density + 0.5f).toInt()

        GlideApp.with(appContext)
            .load(resId)
            .transform(CenterCrop(), RoundedCorners(radiusInPixels))
            .error(com.box.com.R.drawable.image_loading_ic)
            .into(view)
    }

    @BindingAdapter(value = ["loadRound11Image"])
    @JvmStatic
    fun loadRound11Image(view: ImageView, url: String?) {
        if (url.isNullOrEmpty()) {
            view.setImageResource(com.box.com.R.drawable.status_error_ic)
        } else {
            val radiusInDp = 10
            val density = appContext.resources.displayMetrics.density
            val radiusInPixels = (radiusInDp * density + 0.5f).toInt()

            GlideApp.with(appContext)
                .load(url)
                .transform(CenterCrop(), RoundedCorners(radiusInPixels))
                .error(com.box.com.R.drawable.status_error_ic)
                .into(view)
        }
    }

    @BindingAdapter(value = ["loadResNameImage"])
    @JvmStatic
    fun loadResNameImage(view: ImageView, resName: String?) {
        if (resName.isNullOrEmpty()) {
            view.setImageResource(com.box.com.R.drawable.status_error_ic)
        } else {
            val radiusInDp = 10
            val density = appContext.resources.displayMetrics.density
            val radiusInPixels = (radiusInDp * density + 0.5f).toInt()
            GlideApp.with(appContext)
                .load(ResourceUtils.getDrawableIdByName(resName))
                .transform(CenterCrop(), RoundedCorners(radiusInPixels))
                .error(com.box.com.R.drawable.status_error_ic)
                .into(view)
        }
    }

    @BindingAdapter(value = ["loadRound999Image"])
    @JvmStatic
    fun loadRound999Image(view: ImageView, resId: Int?) {
        val radiusInDp = 999
        val density = appContext.resources.displayMetrics.density
        val radiusInPixels = (radiusInDp * density + 0.5f).toInt()

        GlideApp.with(appContext)
            .load(resId)
            .transform(CenterCrop(), RoundedCorners(radiusInPixels))
            .error(com.box.com.R.drawable.image_loading_ic)
            .into(view)
    }

    @BindingAdapter(value = ["loadRound999Image"])
    @JvmStatic
    fun loadRound999Image(view: ImageView, url: String?) {
        if (url.isNullOrEmpty()) {
            view.setImageResource(com.box.com.R.drawable.status_error_ic)
        } else {
            val radiusInDp = 999
            val density = appContext.resources.displayMetrics.density
            val radiusInPixels = (radiusInDp * density + 0.5f).toInt()

            GlideApp.with(appContext)
                .load(url)
                .transform(CenterCrop(), RoundedCorners(radiusInPixels))
                .error(com.box.com.R.drawable.status_error_ic)
                .into(view)
        }
    }


    @BindingAdapter(value = ["loadRound11UrlImage"])
    @JvmStatic
    fun loadRound11UrlImage(view: ImageView, resId: Int?) {
        val radiusInDp = 5
        val density = appContext.resources.displayMetrics.density
        val radiusInPixels = (radiusInDp * density + 0.5f).toInt()
        val options = RequestOptions()
            .override(100, 100)
            .encodeQuality(60)
            .encodeFormat(Bitmap.CompressFormat.WEBP)
            .transform(CenterCrop(), RoundedCorners(radiusInPixels)) // <--- 修改点1: 变换移到这里
        GlideApp.with(appContext)
            .load(resId)
            .apply(options) // options中已包含所有样式
            .error(com.box.com.R.drawable.image_loading_ic)
            .into(view)
    }

    @BindingAdapter(value = ["loadRound11UrlImage"])
    @JvmStatic
    fun loadRound11UrlImage(view: ImageView, url: String?) {
        if (url.isNullOrEmpty()) {
            view.setImageResource(com.box.com.R.drawable.status_error_ic)
        } else {
            val thumbUrl = "https://images.cqxiayou.com/imagethumb.php?thumb=w75h75&imgurl=$url"
            val radiusInDp = 5
            val density = appContext.resources.displayMetrics.density
            val radiusInPixels = (radiusInDp * density + 0.5f).toInt()
            val options = RequestOptions()
                .transform(CenterCrop(), RoundedCorners(radiusInPixels))
            GlideApp.with(appContext)
                .load(thumbUrl)
                .apply(options)
                .error(com.box.com.R.drawable.image_loading_ic)
                .into(view)
        }
    }


    @BindingAdapter(value = ["loadRound12Image"])
    @JvmStatic
    fun loadRound12Image(view: ImageView, resId: Int?) {
        val radiusInDp = 5
        val density = appContext.resources.displayMetrics.density
        val radiusInPixels = (radiusInDp * density + 0.5f).toInt()

        GlideApp.with(appContext)
            .load(resId)
            .transform(CenterCrop(), RoundedCorners(radiusInPixels))
            .error(com.box.com.R.drawable.image_loading_ic)
            .into(view)
    }

    @BindingAdapter(value = ["loadRound12Image"])
    @JvmStatic
    fun loadRound12Image(view: ImageView, url: String?) {
        if (url.isNullOrEmpty()) {
            view.visibility = View.GONE
        } else {
            val radiusInDp = 10
            val density = appContext.resources.displayMetrics.density
            val radiusInPixels = (radiusInDp * density + 0.5f).toInt()

            GlideApp.with(appContext)
                .load(url)
                .transform(CenterCrop(), RoundedCorners(radiusInPixels))
                .error(com.box.com.R.drawable.status_error_ic)
                .into(view)
        }
    }

    @BindingAdapter(value = ["loadImage"])
    @JvmStatic
    fun loadImage(view: ImageView, url: String) {
        GlideApp.with(appContext)
            .load(url)
            .error(com.box.com.R.drawable.image_loading_ic)
            .into(view)
    }

    @BindingAdapter(value = ["loadRound0Image"])
    @JvmStatic
    fun loadRound0Image(view: ImageView, resId: Int?) {
        GlideApp.with(appContext)
            .load(resId)
            .transform(CenterCrop())
            .error(com.box.com.R.drawable.image_loading_ic)
            .into(view)
    }

    @BindingAdapter(value = ["loadRound0Image"])
    @JvmStatic
    fun loadRound0Image(view: ImageView, url: String?) {
        if (url.isNullOrEmpty()) {
            view.visibility = View.GONE
        } else {
            val thumbUrl = "https://images.cqxiayou.com/imagethumb.php?thumb=w75h75&imgurl=$url"
            GlideApp.with(appContext)
                .load(thumbUrl)
                .transform(CenterCrop())
                .error(com.box.com.R.drawable.status_error_ic)
                .into(view)
        }
    }


    @BindingAdapter(value = ["loadRoundSizeImage"])
    @JvmStatic
    fun loadRoundSizeImage(view: ImageView, resId: Int?) {
        val radiusInDp = 15
        val density = appContext.resources.displayMetrics.density
        val radiusInPixels = (radiusInDp * density + 0.5f).toInt()

        GlideApp.with(appContext)
            .load(resId)
            .transform(CenterCrop(), RoundedCorners(radiusInPixels))
            .error(com.box.com.R.drawable.status_error_ic)
            .into(view)
    }

    @BindingAdapter(value = ["loadRoundSizeImage"])
    @JvmStatic
    fun loadRoundSizeImage(view: ImageView, url: String?) {
        if (url.isNullOrEmpty()) {
            view.setImageResource(com.box.com.R.drawable.status_error_ic)
        } else {
            val radiusInDp = 15
            val density = appContext.resources.displayMetrics.density
            val radiusInPixels = (radiusInDp * density + 0.5f).toInt()

            GlideApp.with(appContext)
                .load(url)
                .transform(CenterCrop(), RoundedCorners(radiusInPixels))
                .error(com.box.com.R.drawable.status_error_ic)
                .into(view)
        }
    }

    @BindingAdapter(value = ["loadRoundSizeImageTopRounded"])
    @JvmStatic
    fun loadRoundSizeImageTopRounded(view: ImageView, resId: Int?) {

        val radiusInDp = 15
        val density = appContext.resources.displayMetrics.density
        val radiusInPixels = (radiusInDp * density + 0.5f).toInt()

        GlideApp.with(appContext)
            .load(resId)
            .transform(CenterCrop(), CustomCornersTransformation(radiusInPixels.toFloat(), CustomCornersTransformation.CornerType.TOP))
            .error(com.box.com.R.drawable.status_error_ic)
            .into(view)

    }

    @BindingAdapter(value = ["loadRoundSizeImageTopRounded"])
    @JvmStatic
    fun loadRoundSizeImageTopRounded(view: ImageView, url: String?) {
        if (url.isNullOrEmpty()) {
            view.setImageResource(com.box.com.R.drawable.status_error_ic)
        } else {
            val radiusInDp = 15
            val density = appContext.resources.displayMetrics.density
            val radiusInPixels = (radiusInDp * density + 0.5f).toInt()

            GlideApp.with(appContext)
                .load(url)
                .transform(CenterCrop(), CustomCornersTransformation(radiusInPixels.toFloat(), CustomCornersTransformation.CornerType.TOP))
                .error(com.box.com.R.drawable.status_error_ic)
                .into(view)

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
            imageView.setImageResource(com.box.com.R.drawable.mod_goods_plus)
        }
    }

    @BindingAdapter(value = ["loadRoundSizeImageGone"])
    @JvmStatic
    fun loadRoundSizeImageGone(view: ImageView, url: String?) {
        if (url.isNullOrEmpty()) {
            view.visibility = View.GONE
        } else {
            view.visibility = View.VISIBLE
            GlideApp.with(appContext)
                .load(url)
                .transform(CenterCrop(), RoundedCorners(15))
                .error(com.box.com.R.drawable.status_error_ic)
                .into(view)
        }
    }

    @BindingAdapter(value = ["loadRoundSizeImage"])
    @JvmStatic
    fun loadRoundSizeImage(view: ImageView, drawable: Drawable) {
        val requestOptions = RequestOptions().transform(RoundedCorners(15)) // 设置圆角半径
        GlideApp.with(appContext)
            .load(drawable)
            .apply(requestOptions)
            .error(com.box.com.R.drawable.status_error_ic)
            .into(view)
    }

    @BindingAdapter(value = ["loadResImage"])
    @JvmStatic
    fun loadResImage(view: ImageView, resId: Int) {
        GlideApp.with(appContext)
            .load(resId)
            //.transform(RoundedCorners(ResourceUtils.getDimenIdByName("idp_15")))
            .error(com.box.com.R.drawable.status_error_ic)
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


    @BindingAdapter(
        value = [
            "imageUrl",
            "placeholder",
            "error",
            "cornerRadius",
            "isCircle",
            "dontAnimate"
        ],
        requireAll = false // 表示并非所有属性都是必需的
    )
    @JvmStatic
    fun loadImage(
        view: ImageView,
        url: String?,
        placeholder: Drawable?,
        error: Drawable?,
        cornerRadius: Float?,
        isCircle: Boolean?,
        dontAnimate: Boolean?
    ) {
        if (url.isNullOrEmpty()) {
            view.setImageDrawable(error ?: placeholder)
            return
        }
        val glideRequest = GlideApp.with(view)
            .load(url)
            .placeholder(com.box.com.R.drawable.image_loading_ic)
            .error(error ?: com.box.com.R.drawable.status_error_ic)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)

        when {
            isCircle == true -> {
                glideRequest.transform(CircleCrop())
            }

            cornerRadius != null && cornerRadius > 0f -> {
                val radiusInPixels = (cornerRadius * view.context.resources.displayMetrics.density + 0.5f).toInt()
                glideRequest.transform(CenterCrop(), RoundedCorners(radiusInPixels))
            }

            else -> {
                glideRequest.transform(CenterCrop())
            }
        }
        if (dontAnimate == true) {
            glideRequest.dontAnimate()
        }

        glideRequest.into(view)
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


    @BindingAdapter("ratingText")
    @JvmStatic
    fun setRatingText(textView: TextView, score: Float?) {
        score?.let {
            textView.text = it.toString()
        }
    }

    @BindingAdapter("ratingScore", "starPosition", requireAll = true)
    @JvmStatic
    fun setStarImage(imageView: ImageView, score: Float?, starPosition: Int) {
        score?.let {
            when {
                it >= starPosition -> {
                    imageView.setImageResource(RC.drawable.mod_game_detail_xing_all)
                }

                it >= starPosition - 0.5 -> {
                    imageView.setImageResource(RC.drawable.mod_game_detail_xing_ban)
                }

                else -> {
                    imageView.setImageResource(RC.drawable.mod_game_detail_xing_kong)
                }
            }
        }
    }


}