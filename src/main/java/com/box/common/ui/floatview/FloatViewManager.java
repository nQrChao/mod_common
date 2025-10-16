package com.box.common.ui.floatview;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.widget.ImageView;

import com.box.com.R;
import com.box.common.glide.GlideApp;
import com.box.common.utils.floattoast.XToast;
import com.box.common.utils.floattoast.draggable.SpringScaleDraggable;
import com.box.other.blankj.utilcode.util.SizeUtils;
import com.box.other.hjq.toast.Toaster;

public class FloatViewManager {

    private static XToast mGameFloat;
    private static XToast mAppFloat;

    /**
     * 显示应用浮窗。
     * 如果浮窗已创建但被隐藏，则重新显示它。
     * 如果浮窗未创建，则创建并显示它。
     *
     * @param application Application
     */
    public static void showAppFloat(Activity activity) {
        // 如果 mAppFloat 不为 null，说明它正在显示中，直接返回
        if (mAppFloat != null) {
            return;
        }
        // 这确保了对象在整个生命周期中只被创建一次。
        mAppFloat = new XToast<>(activity)
                .setContentView(R.layout.app_floatview) // 设置自定义布局
                .setGravity(Gravity.START | Gravity.TOP) // 设置初始位置在右上角
                .setYOffset(SizeUtils.dp2px(100f)) // Y 轴偏移，单位 px
                .setDraggable(new SpringScaleDraggable(0.6f, 3000L)) // 设置为可拖拽
                .setOnClickListener((toast, view) -> {
                    Toaster.show("你点击了图标");
                });
        mAppFloat.show();
        Toaster.show(" mAppFloat.show()");
    }

    /**
     * 显示游戏浮窗。
     * 逻辑同 showAppFloat。
     *
     * @param application Application
     */
    public static void showGameFloat(Application application) {
        if (mGameFloat != null) {
            return;
        }
        // 懒加载，仅在对象为 null 时创建一次
        mGameFloat = new XToast<>(application)
                .setContentView(R.layout.item_floatview) // 设置自定义布局
                .setGravity(Gravity.END | Gravity.TOP) // 设置初始位置在右上角
                .setYOffset(SizeUtils.dp2px(-80f)) // Y 轴偏移，单位 px
                .setDraggable(new SpringScaleDraggable(0.6f, 4000L)) // 设置为可拖拽
                .setOnClickListener((toast, view) -> {
                    Toaster.show("你点击了图标");
                });

        // 如果浮窗当前未显示，则调用 show() 方法显示它
        mGameFloat.show();
    }

    public static void setAppFloatImage(Context context, Drawable drawable) {
        if (mAppFloat != null) {
            ImageView imageView = (ImageView) mAppFloat.findViewById(R.id.float_image);
            GlideApp.with(context)
                    .load(drawable)
                    .into(imageView);
        }
    }

    public static void setGameFloatImage(Context context, Drawable drawable) {
        if (mGameFloat != null) {
            ImageView imageView = (ImageView) mGameFloat.findViewById(R.id.float_image);
            GlideApp.with(context)
                    .load(drawable)
                    .into(imageView);
        }
    }

    /**
     * 外部调用隐藏游戏浮窗
     */
    public static void hideGameFloat() {
        if (mGameFloat != null && mGameFloat.isShowing()) {
            mGameFloat.cancel();
            mGameFloat = null;
        }
    }

    /**
     * 外部调用隐藏应用浮窗
     */
    public static void hideAppFloat() {
        if (mAppFloat != null && mAppFloat.isShowing()) {
            mAppFloat.cancel();
            mAppFloat = null;
        }
    }

    /**
     * 销毁所有浮窗实例，用于完全释放资源
     */
    public static void destroyFloat() {
        hideGameFloat(); // 先隐藏
        mGameFloat = null; // 再置空

        hideAppFloat(); // 先隐藏
        mAppFloat = null; // 再置空
    }
}