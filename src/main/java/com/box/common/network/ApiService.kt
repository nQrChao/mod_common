package com.box.common.network

import com.box.common.data.AndroidStatusRequest
import com.box.common.data.RegisterRequest
import com.box.common.data.model.MarketInit
import com.box.common.data.model.ModDataBean
import com.box.common.data.model.ModInitBean
import com.box.common.data.model.ModUserInfo
import com.box.common.data.model.ModUserRealName
import com.box.common.data.model.ProtocolInit
import retrofit2.Response
import retrofit2.http.Body
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
        var D_API_URL = "https://4319g.yize01.com"
        var XY_1_URL = "https://mobile.xiaodianyouxi.com/index.php/Index/view/?id=100000012"//注册
        var XY_2_URL = "https://mobile.xiaodianyouxi.com/index.php/Index/view/?id=100000011"//隐私
    }

    @HEAD
    suspend fun checkUrl(@Url url: String): Response<Void>

    @FormUrlEncoded
    @POST("/api/no/common/game/getGameList")
    suspend fun getData(@Field("data") data: String): ModApiResponse<Any?>

    @FormUrlEncoded
    @POST("App/index?api=market_init")
    suspend fun getDataWithMsg(@Field("data") data: String): ModApiResponse<Any>


    /**
     * 安卓获取应用初始化信息
     * 对应的 URL: [POST] /api/no/common/control/initialization
     */
    @POST("api/no/common/control/initialization")
    suspend fun postInitializationInfo(@Body requestBody: AndroidStatusRequest): ModApiResponse<ModInitBean>

    /**
     * 获取安卓状态 (切换控制接口)
     * 对应的 URL: [POST] /api/no/common/control/getAndroidStatus
     */
    @POST("/api/no/common/control/getAndroidStatus")
    suspend fun getAndroidStatus(@Body requestBody: RegisterRequest): ModApiResponse<Any>

    /**
     * 用户登录
     * 对应的 URL: [POST] /api/no/common/user/login
     * 发送 application/json 格式的数据
     */
    @POST("/api/no/common/user/login")
    suspend fun postUserLogin(@Body requestBody: RegisterRequest): ModApiResponse<ModUserInfo>

    /**
     * 用户注册
     * 对应的 URL: [POST] /api/no/common/user/register
     * 发送 application/json 格式的数据
     */
    @POST("/api/no/common/user/register")
    suspend fun postUserRegister(@Body requestBody: RegisterRequest): ModApiResponse<ModUserInfo>

    /**
     * 获取游戏列表
     * 对应的 URL: https://4319g.yize01.com/api/no/common/game/getGameList?pageNum=1&pageSize=10&type=1
     */
    @GET("api/no/common/game/getGameList")
    suspend fun getModGameRankList(
        @Query("pageNum") pageNum: Int,
        @Query("pageSize") pageSize: Int,
        @Query("type") type: Int
    ): ModApiResponse<MutableList<ModDataBean>>

    /**
     * 根据ID获取新闻详情
     * 对应的 URL: https://4319g.yize01.com/api/no/common/news/getDetailById?id=3
     */
    @GET("api/no/common/news/getDetailById")
    suspend fun getNewsDetailById(@Query("id") id: Int): ModApiResponse<ModDataBean>

    /**
     * 获取新闻列表
     * 对应的 URL: https://4319g.yize01.com/api/no/common/news/list?pageNum=1&pageSize=10
     */
    @GET("api/no/common/news/list")
    suspend fun getNewsList(
        @Query("pageNum") pageNum: Int,
        @Query("pageSize") pageSize: Int
    ): ModApiResponse<MutableList<ModDataBean>>

    /**
     * 随机获取角色名称
     * 对应的 URL: https://4319g.yize01.com/api/no/common/role/randName?roleType=1&length=3
     */
    @GET("api/no/common/role/randName")
    suspend fun getRandomName(
        @Query("roleType") roleType: Int,
        @Query("length") length: Int
    ): ModApiResponse<ModDataBean>

    /**
     * 获取角色类型
     * 对应的 URL: https://4319g.yize01.com/api/no/common/role/type
     */
    @GET("api/no/common/role/type")
    suspend fun getRoleType(): ModApiResponse<MutableList<ModDataBean>>


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
    suspend fun postAuthLogin(
        @Query(value = "api") api: String,
        @Field("data") data: String
    ): ModApiResponse<ModUserInfo>

    @FormUrlEncoded
    @POST("App/index?api=login")
    suspend fun postUserInfo(@Field("data") data: String): ModApiResponse<ModUserInfo>

    @FormUrlEncoded
    @POST("/index.php/App/index")
    suspend fun postModUserRealName(@Field("data") data: String): ModApiResponse<ModUserRealName>

}