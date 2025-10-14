package com.box.common.data.model

import android.graphics.drawable.Drawable
import com.box.common.R
import com.box.other.blankj.utilcode.util.ResourceUtils
import java.io.Serializable

class LocalGameInfo(
    var id: String = "",
    var title: String = "",
    var url: String = "",
    var pic: Drawable = ResourceUtils.getDrawable(R.drawable.image_error_ic),
    ) : Serializable