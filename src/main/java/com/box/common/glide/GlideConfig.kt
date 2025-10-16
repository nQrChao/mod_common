package com.box.common.glide

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import com.box.base.network.SSLSocketClient
import com.box.com.R
import okhttp3.OkHttpClient
import java.io.File
import java.io.InputStream
import java.util.concurrent.TimeUnit
import javax.net.ssl.X509TrustManager

@GlideModule
class GlideConfig : AppGlideModule() {

    companion object {

        /** 本地图片缓存文件最大值 */
        private const val IMAGE_DISK_CACHE_MAX_SIZE: Int = 1500 * 1024 * 1024
    }

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        // 读写外部缓存目录不需要申请存储权限
        val diskCacheFile = File(context.cacheDir, "glide")
        // 如果这个路径是一个文件
        if (diskCacheFile.exists() && diskCacheFile.isFile) {
            // 执行删除操作
            diskCacheFile.delete()
        }
        // 如果这个路径不存在
        if (!diskCacheFile.exists()) {
            // 创建多级目录
            diskCacheFile.mkdirs()
        }
        builder.setDiskCache {
            DiskLruCacheWrapper.create(diskCacheFile, IMAGE_DISK_CACHE_MAX_SIZE.toLong())
        }
        val calculator: MemorySizeCalculator = MemorySizeCalculator.Builder(context).build()
        val defaultMemoryCacheSize: Int = calculator.memoryCacheSize
        val defaultBitmapPoolSize: Int = calculator.bitmapPoolSize
        val customMemoryCacheSize: Long = (2 * defaultMemoryCacheSize).toLong()
        val customBitmapPoolSize: Long = (2 * defaultBitmapPoolSize).toLong()
        builder.setMemoryCache(LruResourceCache(customMemoryCacheSize))
        builder.setBitmapPool(LruBitmapPool(customBitmapPoolSize))
        builder.setDefaultRequestOptions(
            RequestOptions()
                // 设置默认加载中占位图
                .placeholder(R.drawable.image_loading_ic)
                // 设置默认加载出错占位图
                .error(R.drawable.image_error_ic)
        )
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        val builder: OkHttpClient.Builder = OkHttpClient.Builder()
            .retryOnConnectionFailure(false)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .sslSocketFactory(SSLSocketClient.getSSLSocketFactory(), SSLSocketClient.getTrustManager()[0] as X509TrustManager)
            .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
        registry.replace(GlideUrl::class.java, InputStream::class.java, OkHttpLoader.Factory(builder.build()))
        //registry.replace(GlideUrl::class.java, InputStream::class.java, OkHttpLoader.Factory(EasyConfig.getInstance().client))
    }

    override fun isManifestParsingEnabled(): Boolean {
        return false
    }
}