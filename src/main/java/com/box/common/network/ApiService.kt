package com.box.common.network

import com.box.common.data.model.MarketInit
import com.box.common.data.model.ProtocolInit

import com.box.common.data.model.ModUserInfoBean
import com.box.common.data.model.ModUserRealName

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.HEAD
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Url

/**
 * 网络请求类
 */
interface ApiService {
    companion object {
        var HTTP_RELEASE_URLS: Array<String> = arrayOf(
            "https://4319g.yize01.com/",
            "https://4319g.yize01.com/"
        )
        var D_API_URL = "https://4319g.yize01.com/api"
        var XY_1_URL = "https://mobile.xiaodianyouxi.com/index.php/Index/view/?id=100000012"//注册
        var XY_2_URL = "https://mobile.xiaodianyouxi.com/index.php/Index/view/?id=100000011"//隐私
    }

    @HEAD
    suspend fun checkUrl(@Url url: String): Response<Void>

    @FormUrlEncoded
    @POST("App/index?api=market_init")
    suspend fun getData(@Field("data") data: String): ModApiResponse<Any?>
    @FormUrlEncoded
    @POST("App/index?api=market_init")
    suspend fun getDataWithMsg(@Field("data") data: String): ModApiResponse<Any>
    @FormUrlEncoded
    @Headers("Domain-Name: XDQY_API_URL")
    @POST("/index/init")
    suspend fun refreshToken(@Field("data") data: String): ModApiResponse<String>

    @FormUrlEncoded
    @Headers("Domain-Name: XDQY_API_URL")
    @POST("/index/init")
    suspend fun postXdqyInit(@Field("data") data: String): ModApiResponse<MutableList<String>>

    @FormUrlEncoded
    @POST("App/index?api=market_init")
    suspend fun marketInit(@Field("data") data: String): ModApiResponse<MarketInit>

    @FormUrlEncoded
    @POST("App/index/api=market_data_appapi")
    suspend fun postDataAppApi(@Field("data") data: String): ModApiResponse<ProtocolInit>

    @FormUrlEncoded
    @POST("/index.php/App/index")
    suspend fun postAuthLogin(@Query(value = "api") api: String, @Field("data") data: String): ModApiResponse<ModUserInfoBean>

    @FormUrlEncoded
    @POST("App/index?api=login")
    suspend fun postUserInfo(@Field("data") data: String): ModApiResponse<ModUserInfoBean>

    @FormUrlEncoded
    @POST("/index.php/App/index")
    suspend fun postModUserRealName(@Field("data") data: String): ModApiResponse<ModUserRealName>

}