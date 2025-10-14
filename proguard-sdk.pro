-keep class com.box.base.** { *; }
-keepclassmembers class com.box.base.** { *; }
-keep class com.box.common.data.** { *; }
-keepclassmembers class com.box.common.data.** { *; }
-keep class com.box.common.ui.fragment.** { *; }
-keepclassmembers class com.box.common.ui.fragment.** { *; }
-keep class com.box.common.event.** { *; }
-keepclassmembers class com.box.common.event.** { *; }
-keep class com.box.common.sdk.ImSDK { *; }
-keepclassmembers class com.box.common.sdk.ImSDK { *; }
-keepnames class * implements com.box.base.ext.BaseViewModelExtKt
-keepnames class * implements com.box.base.base.viewmodel.BaseViewModel
-keepnames class * implements com.box.base.base.activity.BaseVmDbActivity
-keep class com.box.imcore.** { *; }
-keepclassmembers class com.box.imcore.** { *; }
-keep class com.box.other.** { *; }
-keepclassmembers class com.box.other.** { *; }
-dontwarn com.box.other.**
-keep class org.webrtc.** { *; }
-dontwarn org.webrtc.**
-keep class com.alibaba.** { *; }
-dontwarn com.alibaba.**
-keep class com.amap.** { *; }
-dontwarn com.amap.**

# 保留 BaseRecyclerViewAdapterHelper 库
-keep class com.chad.library.adapter.base.** { *; }
-keepclassmembers class com.chad.library.adapter.base.** { *; }

# 如果你的 LocalGamesAdapter 使用了特定的 ViewHolder 类，也保留它们
-keep class com.zqhy.app.youpackage.view.adapter.LocalGamesAdapter$*ViewHolder { *; }
-keepclassmembers class com.zqhy.app.youpackage.view.adapter.LocalGamesAdapter$*ViewHolder { *; }

# 保留你的 ApiService 接口定义 (假设 apiService 是这个接口的一个实例)
-keep interface com.box.common.network.ApiService { *; }
# 保留你的 BaseResponse 类
-keep class com.box.base.network.BaseResponse { *; }
-keepclassmembers class com.box.base.network.BaseResponse { *; }

# 保留 marketInit 方法的签名
-keepclassmembers interface com.box.common.network.ApiService {
        *;
}
# 保留 AppletsApiResponse 类
-keep class com.box.common.network.AppletsApiResponse { *; }
-keepclassmembers class com.box.common.network.AppletsApiResponse { *; }

# 保留 MarketInit 类及其成员
-keep class com.box.common.data.model.MarketInit { *; }
-keepclassmembers class com.box.common.data.model.MarketInit { *; }
#-keep interface com.zqhy.app.network.IApiService { *; } # 如果适用，也为你的其他 ApiService 添加

# 保留你的 ApiService 接口中 marketInit 的特定方法签名
#-keep interface com.zqhy.app.network.IApiService {}

# 同样，确保 marketInit 的返回类型被保留。如果它使用了泛型，也保留它们。
-keep class com.box.common.data.model.MarketInit { *; }
-keepclassmembers class com.box.common.data.model.MarketInit { *; }

-keep class com.amap.api.maps.**{*;}
-keep class com.autonavi.**{*;}
-keep class com.amap.api.trace.**{*;}

-keep class com.amap.api.location.**{*;}
-keep class com.amap.api.fence.**{*;}
-keep class com.autonavi.aps.amapapi.model.**{*;}

-keep class com.amap.api.services.**{*;}

-keep class com.amap.api.maps2d.**{*;}
-keep class com.amap.api.mapcore2d.**{*;}

-keep class com.amap.api.navi.**{*;}
-keep class com.autonavi.**{*;}
-keep class com.tencent.** { *; }
-dontwarn dalvik.**
-dontwarn com.tencent.**
-keep class com.umeng.** {*;}
-dontwarn com.umeng.**
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn org.conscrypt.**
-keep class com.bumptech.glide.integration.okhttp3.OkHttpGlideModule
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule {
 <init>(...);
}
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-keep class com.bumptech.glide.load.data.ParcelFileDescriptorRewinder$InternalRewinder {
  *** rewind();
}
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Exceptions
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keep class com.google.gson.stream.** { *; }
-keepattributes EnclosingMethod
#-keep class com.chaoji.other.blankj.subutil.** { *; }
#-keepclassmembers class com.chaoji.other.blankj.subutil.** { *; }
-dontwarn com.box.other.blankj.subutil.**

-dontwarn com.box.other.blankj.utilcode.**
-keepclassmembers class * {
    @com.box.other.blankj.utilcode.util.BusUtils$Bus <methods>;
}
-keep public class * extends com.box.other.blankj.utilcode.util.ApiUtils$BaseApi
-keep @com.box.other.blankj.utilcode.util.ApiUtils$Api class *
-keep class com.shuyu.gsyvideoplayer.video.** { *; }
-dontwarn com.shuyu.gsyvideoplayer.video.**
-keep class com.shuyu.gsyvideoplayer.video.base.** { *; }
-dontwarn com.shuyu.gsyvideoplayer.video.base.**
-keep class com.shuyu.gsyvideoplayer.utils.** { *; }
-dontwarn com.shuyu.gsyvideoplayer.utils.**
-keep class com.shuyu.gsyvideoplayer.player.** {*;}
-dontwarn com.shuyu.gsyvideoplayer.player.**
-keep class tv.danmaku.ijk.** { *; }
-dontwarn tv.danmaku.ijk.**
-keep class androidx.media3.** {*;}
-keep interface androidx.media3.**

-keep class com.shuyu.alipay.** {*;}
-keep interface com.shuyu.alipay.**

-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, java.lang.Boolean);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
-optimizationpasses 5
-allowaccessmodification
-dontpreverify
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose
#-dontshrink  // 移除或注释掉这行
-dump class_files.txt
-printseeds seeds.txt
-printusage unused.txt
-printmapping mapping.txt
-keepattributes SourceFile,LineNumberTable
-keepattributes Signature
-keepattributes *Annotation*
-keepclasseswithmembernames class * {
    native <methods>;
}
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class * implements android.os.Parcelable {
public static final android.os.Parcelable$Creator *;
}
-keepclassmembers class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator CREATOR;
}

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-keepclassmembers class **.R$* {
    public static <fields>;
}

-keepattributes *JavascriptInterface*
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

-keep class kotlinx.coroutines.** { *; }
-keepnames class kotlinx.coroutines.** { *; }
-keepclassmembers class kotlinx.coroutines.** { *; }

-keep class kotlin.reflect.** { *; }
-keep class kotlin.Metadata { *; }
-keep class kotlin.coroutines.CoroutineContext { *; }
-keepclassmembers class ** {
 @androidx.annotation.Keep <methods>;
}

-keep class androidx.lifecycle.** { *; }
-keep class androidx.arch.core.** { *; }
-keep class java.** { *; }
-keep class java.time.** { *; }
-keep class javax.** { *; }
-keep class org.** { *; }
-keep class android.** { *; }
-dontwarn javax.annotation.**
-dontwarn javax.inject.**

# A resource is loaded with a relative path so the package of this class must be preserved.
-adaptresourcefilenames okhttp3/internal/publicsuffix/PublicSuffixDatabase.gz
# Animal Sniffer compileOnly dependency to ensure APIs are compatible with older versions of Java.
-dontwarn org.codehaus.mojo.animal_sniffer.*
# OkHttp platform used only on JVM and when Conscrypt and other security providers are available.
-dontwarn okhttp3.internal.platform.**

# Only uses Java 9+ APIs when available.
-dontwarn java.util.concurrent.Flow
-dontwarn javax.net.ssl.*
-dontwarn sun.security.ssl.*

# Optional native socket library.
-dontwarn com.squareup.okhttp.internal.jni.**
# Android 4.0+ APIs
-dontwarn com.squareup.okhttp.internal.huc.**
# For internal use by Kotlin metadata compiler.
-dontwarn kotlin.jvm.JvmOverloads
-keepattributes *Annotation*
-keepattributes EnclosingMethod
-keepattributes InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable
-keep class com.android.volley.** { *; }
-keep interface com.android.volley.** { *; }
-keep class org.apache.http.** { *; }
-keep interface org.apache.http.** { *; }
-keep class org.apache.http.impl.** { *; }
-keep class org.apache.http.entity.** { *; }
-keep class org.apache.http.protocol.** { *; }
-keep class org.apache.http.params.** { *; }
-keep class org.apache.http.client.** { *; }
-keep class org.apache.http.client.methods.** { *; }
-keep class org.apache.http.conn.** { *; }
-keep class org.apache.http.conn.scheme.** { *; }
-keep class org.apache.http.conn.ssl.** { *; }
-keep class org.apache.http.util.** { *; }
-keep class org.apache.http.message.** { *; }
-keep class org.apache.http.auth.** { *; }
-keep class org.apache.http.auth.params.** { *; }
-keep class org.apache.http.cookie.** { *; }
-keep class org.apache.http.impl.client.** { *; }
-keep class org.apache.http.impl.conn.** { *; }
-keep class org.apache.http.impl.auth.** { *; }
-keep class org.apache.http.impl.cookie.** { *; }
-keep class org.apache.http.impl.entity.** { *; }
-keep class org.apache.http.impl.util.** { *; }
-keep class org.apache.http.impl.client.cache.** { *; }
-keep class org.apache.http.client.cache.** { *; }
-keep class com.tencent.mm.opensdk.** { *; }
-dontwarn com.tencent.mm.opensdk.**

#--------- 集成保活 start ---------
-keep class com.huawei.android.hms.agent.**{*;}
-dontwarn com.huawei.android.hms.agent.**
-keep class com.xiaomi.mipush.sdk.**{*;}
-keep class com.xiaomi.push.**{*;}
-dontwarn com.xiaomi.mipush.sdk.**
-keep class com.meizu.cloud.pushsdk.**{*;}
-dontwarn com.meizu.cloud.pushsdk.**
-keep class com.vivo.push.**{*;}
-dontwarn com.vivo.push.**
-keep class com.hihonor.push.**{*;}
-dontwarn com.hihonor.push.**
-keep class com.taobao.accs.**{*;}
-keep class anet.channel.**{*;}
-keep class org.android.agoo.**{*;}
-keep class com.taobao.tlog.**{*;}
-dontwarn com.taobao.accs.**
-dontwarn anet.channel.**
-dontwarn org.android.agoo.**
-dontwarn com.taobao.tlog.**
#--------- 集成保活 end ---------
# Huawei HNS SDK
-dontwarn com.huawei.hms.ads.**
-keep class com.huawei.hms.ads.** {*; }
-keep interface com.huawei.hms.ads.** {*; }

# Honor MCS SDK
-dontwarn com.hihonor.ads.identifier.**
-keeppackagenames com.hihonor.ads.identifier
-keeppackagenames com.hihonor.cloudservice.oaid
-keep class com.hihonor.ads.identifier.AdvertisingIdClient*{*;}