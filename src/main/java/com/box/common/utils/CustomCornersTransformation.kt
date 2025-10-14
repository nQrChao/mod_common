package com.box.common.utils

import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Shader
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest


class CustomCornersTransformation(private val radius: Float, private val cornerType: CornerType) : BitmapTransformation() {
    // 用枚举来定义需要处理的角
    enum class CornerType {
        ALL,
        TOP,
        BOTTOM,
        LEFT,
        RIGHT,
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT
    }

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        // 从 BitmapPool 获取一个干净的 Bitmap，或者创建一个新的
        var bitmap = pool[toTransform.width, toTransform.height, Bitmap.Config.ARGB_8888]
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(toTransform.width, toTransform.height, Bitmap.Config.ARGB_8888)
        }

        val canvas = Canvas(bitmap)
        val paint = Paint()
        // 使用 BitmapShader 填充形状
        paint.setShader(BitmapShader(toTransform, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP))
        paint.isAntiAlias = true

        drawRoundRect(canvas, paint, toTransform.width.toFloat(), toTransform.height.toFloat())

        return bitmap
    }

    private fun drawRoundRect(canvas: Canvas, paint: Paint, width: Float, height: Float) {
        val rectF = RectF(0f, 0f, width, height)
        var radii = FloatArray(8)
        radii = when (cornerType) {
            CornerType.ALL -> floatArrayOf(radius, radius, radius, radius, radius, radius, radius, radius)
            CornerType.TOP -> floatArrayOf(radius, radius, radius, radius, 0f, 0f, 0f, 0f)
            CornerType.BOTTOM -> floatArrayOf(0f, 0f, 0f, 0f, radius, radius, radius, radius)
            CornerType.LEFT -> floatArrayOf(radius, radius, 0f, 0f, radius, radius, 0f, 0f)
            CornerType.RIGHT -> floatArrayOf(0f, 0f, radius, radius, 0f, 0f, radius, radius)
            CornerType.TOP_LEFT -> floatArrayOf(radius, radius, 0f, 0f, 0f, 0f, 0f, 0f)
            CornerType.TOP_RIGHT -> floatArrayOf(0f, 0f, radius, radius, 0f, 0f, 0f, 0f)
            CornerType.BOTTOM_LEFT -> floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, radius, radius)
            CornerType.BOTTOM_RIGHT -> floatArrayOf(0f, 0f, 0f, 0f, radius, radius, 0f, 0f)
            else ->                 // 默认情况，所有角都是直角
                floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)
        }
        val path = Path()
        path.addRoundRect(rectF, radii, Path.Direction.CW)
        canvas.drawPath(path, paint)
    }

    // 必须重写 updateDiskCacheKey, equals 和 hashCode 方法，以确保 Glide 的缓存机制能正常工作
    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        val id = "CustomCornersTransformation(radius=" + radius + ", cornerType=" + cornerType.name + ")"
        messageDigest.update(id.toByteArray(CHARSET))
    }

    override fun equals(o: Any?): Boolean {
        if (o is CustomCornersTransformation) {
            val other = o
            return radius == other.radius && cornerType == other.cornerType
        }
        return false
    }

    override fun hashCode(): Int {
        return "com.your.package.CustomCornersTransformation.$radius$cornerType".hashCode()
    }
}