package com.chaoji.im.network

import androidx.lifecycle.MutableLiveData
import com.chaoji.im.data.model.AIChat
import com.chaoji.im.data.model.AiMessage
import com.chaoji.im.data.model.AiResultInfo
import com.chaoji.im.data.model.ApiResponse
import com.chaoji.im.data.model.AppUserInfo
import com.chaoji.im.data.model.AppletsBiZhi
import com.chaoji.im.data.model.AppletsClass
import com.chaoji.im.data.model.AssistantList
import com.chaoji.im.data.model.CheckQrcodeResult
import com.chaoji.im.data.model.FindUserResult
import com.chaoji.im.data.model.ImCheckResultInfo
import com.chaoji.im.data.model.MarketInit
import com.chaoji.im.data.model.NewLoginInfo
import com.chaoji.im.data.model.RefreshAiTokenInfo
import com.chaoji.im.data.model.RefreshTokenInfo
import com.chaoji.im.data.model.UserUseCountResult
import com.chaoji.im.data.model.AppletsData
import com.chaoji.im.data.model.AppletsGoodBeanList
import com.chaoji.im.data.model.AppletsInfo
import com.chaoji.im.data.model.AppletsLeYuan
import com.chaoji.im.data.model.AppletsLunTan
import com.chaoji.im.data.model.AppletsRank
import com.chaoji.im.data.model.AppletsXiaoGame
import com.chaoji.im.data.model.ModCocosExchange
import com.chaoji.im.data.model.ModCollectionGood
import com.chaoji.im.data.model.ModGameIcon
import com.chaoji.im.data.model.ModGameInfo
import com.chaoji.im.data.model.ModGameListInfo
import com.chaoji.im.data.model.ModGameListInfoDialog
import com.chaoji.im.data.model.ModLoginBean
import com.chaoji.im.data.model.ModTradeGoodDetailBean
import com.chaoji.im.data.model.ModUserInfoBean
import com.chaoji.im.data.model.RefundGames
import com.chaoji.im.data.model.ModInitData
import com.chaoji.im.data.model.ModPay
import com.chaoji.im.data.model.ModSplashVo
import com.chaoji.im.data.model.ModTradeGoodBean
import com.chaoji.im.data.model.ModTradeRankBean
import com.chaoji.im.data.model.ModUserRealName
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.HEAD
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

/**
 * 网络请求类
 */
interface ApiServiceXdqy {
    //https://appapi-nstest.xiaodianyouxi.com
    companion object {
        var HTTP_RELEASE_URLS: Array<String> = arrayOf(
            "https://xgameapi.xiaodianyouxi.com",
            "https://appapi-ns2.xiaodianyouxi.com"
        )
        var D_API_URL = "https://appapi-ns1.xiaodianyouxi.com/index.php/"
        var XY_1_URL = "https://mobile.xiaodianyouxi.com/index.php/Index/view/?id=100000012"//注册
        var XY_2_URL = "https://mobile.xiaodianyouxi.com/index.php/Index/view/?id=100000011"//隐私
    }

    @HEAD
    suspend fun checkUrl(@Url url: String): Response<Void>

    @FormUrlEncoded
    @POST("App/index?api=market_init")
    suspend fun marketInit(@Field("data") data: String): ModApiResponse<MarketInit>

    @FormUrlEncoded
    @POST("App/index/api=market_data_appapi")
    suspend fun postDataAppApi(@Field("data") data: String): ModApiResponse<AppletsData>

    @FormUrlEncoded
    @POST("/index.php/App/index")
    suspend fun postIndex(@Field("data") data: String): ModApiResponse<String>

    @FormUrlEncoded
    @POST("App/index/api=market_data_appapi")
    suspend fun postGetVerificationCode(@Field("data") data: String): ModApiResponse<String>

    @FormUrlEncoded
    @POST("App/index?api=login")
    suspend fun postLogin(@Field("data") data: String): ModApiResponse<ModLoginBean>

    @FormUrlEncoded
    @POST("App/index?api=pay_gold")
    suspend fun postModPay(@Field("data") data: String): ModApiResponse<ModPay>

    @FormUrlEncoded
    @POST("App/index?api=cert_add_v2")
    suspend fun postModShiMing(@Field("data") data: String): ModApiResponse<ModPay>

    @FormUrlEncoded
    @POST("/index.php/App/index")
    suspend fun postAuthLogin(@Query(value = "api") api: String, @Field("data") data: String): ModApiResponse<ModUserInfoBean>

    @FormUrlEncoded
    @POST("/index.php/App/index")
    suspend fun postInitLogin(@Query(value = "api") api: String, @Field("data") data: String): ModApiResponse<ModUserInfoBean>

    @FormUrlEncoded
    @POST("App/index?api=login")
    suspend fun postUserInfo(@Field("data") data: String): ModApiResponse<ModUserInfoBean>

    @FormUrlEncoded
    @POST("/index.php/App/index")
    suspend fun postInitData(@Field("data") data: String): ModApiResponse<ModInitData>

    @FormUrlEncoded
    @POST("/index.php/App/index")
    suspend fun postModUserRealName(@Field("data") data: String): ModApiResponse<ModUserRealName>

    @FormUrlEncoded
    @POST("/index.php/App/index")
    suspend fun postSplashBean(@Field("data") data: String): ModApiResponse<ModSplashVo.ModSplashBean>

    @FormUrlEncoded
    @Headers("Domain-Name: TRANSACTION_API_URL")
    @POST("/trade/collect")
    suspend fun collectionGood(@Field("data") data: String): ModApiResponse<ModCollectionGood>

    @FormUrlEncoded
    @Headers("Domain-Name: TRANSACTION_API_URL")
    @POST("/trade/goods_info")
    suspend fun tradeGoodDetail(@Field("data") data: String): ModApiResponse<ModTradeGoodDetailBean>

    @FormUrlEncoded
    @Headers("Domain-Name: TRANSACTION_API_URL")
    @POST("/trade/goods_list")
    suspend fun tradeGoodsList(@Field("data") data: String): ModApiResponse<MutableList<ModTradeGoodDetailBean>>

    @FormUrlEncoded
    @POST("App/index/api=gameinfo_part_base")
    suspend fun postModGameInfo(@Field("data") data: String): ModApiResponse<ModGameInfo>

    @FormUrlEncoded
    @POST("App/index/api=market_tradegame")
    suspend fun postModTradeGameIcon(@Field("data") data: String): ModApiResponse<ModGameIcon>

    @FormUrlEncoded
    @POST("App/index/api=market_data_appapi")
    suspend fun postInfoAppApi(@Field("data") data: String): ModApiResponse<AppletsInfo>

    @FormUrlEncoded
    @POST("App/index/api=market_data_appapi")
    suspend fun postInfoLeYuanAppApi(@Field("data") data: String): ModApiResponse<AppletsLeYuan>

    @FormUrlEncoded
    @POST("App/index/api=market_data_appapi")
    suspend fun postModRankAppApi(@Field("data") data: String): ModApiResponse<AppletsRank>

    @FormUrlEncoded
    @POST("App/index/api=market_data_appapi")
    suspend fun postInfoLunTanAppApi(@Field("data") data: String): ModApiResponse<AppletsLunTan>

    @FormUrlEncoded
    @POST("App/index/api=market_data_appapi")
    suspend fun postBiZhiAppApi(@Field("data") data: String): ModApiResponse<AppletsBiZhi>

    @FormUrlEncoded
    @POST("App/index/api=market_data_appapi")
    suspend fun postInfoGoodListAppApi(@Field("data") data: String): ModApiResponse<AppletsGoodBeanList>

    @FormUrlEncoded
    @POST("App/index/api=market_data_appapi")
    suspend fun postXiaoGameApi(@Field("data") data: String): ModApiResponse<AppletsXiaoGame>

    @FormUrlEncoded
    @POST("App/index/api?market_data_appapi")
    suspend fun postGameInfoAppApi(@Field("data") data: String): ModApiResponse<ModGameListInfo>

    @FormUrlEncoded
    @POST("App/index/api?market_cocos_exchangeflb")
    suspend fun postCocosExchangeApi(@Field("data") data: String): ModApiResponse<ModCocosExchange>

    @FormUrlEncoded
    @POST("App/index/api?market_data_appapi")
    suspend fun postGameInfoDialogAppApi(@Field("data") data: String): ModApiResponse<ModGameListInfoDialog>

    @FormUrlEncoded
    @POST("App/index?api=ad_active")
    suspend fun adActive(@Field("data") data: String): ModApiResponse<Any?>

    @FormUrlEncoded
    @POST("App/index?api=refund_games")
    suspend fun refundGames(@Field("data") data: String): ModApiResponse<RefundGames>

    @FormUrlEncoded
    @POST("App/index")
    suspend fun postClaimData(@Field("data") data: String): ModApiResponse<Any?>

    @POST("chat/user/checkqrcode")
    suspend fun checkQrCode(@Query("scancode") code: String): ApiResponse<CheckQrcodeResult>

    @POST("chat/user/confirmlogin")
    suspend fun scanLogin(
        @Query("scancode") code: String,
        @Query("login") login: String
    ): ApiResponse<ResponseBody>


    @Headers("Domain-Name: SERVER_URL")
    @POST("/api/getCateList")
    suspend fun getCateList(): ModApiResponse<MutableList<AppletsClass>>

    @Headers("Domain-Name: SERVER_URL")
    @POST("/api/getProgramRecommendList")
    suspend fun getProgramRecommendList(): ModApiResponse<MutableList<AppletsInfo>>

    @Headers("Domain-Name: SERVER_URL")
    @POST("api/v1/refreshtoken")
    suspend fun refreshAiToken(@Body requestBody: RequestBody?): com.chaoji.im.network.ApiResponse<RefreshAiTokenInfo>

    @Headers("Domain-Name: SERVER_URL")
    @POST("api/v1/refreshToken")
    suspend fun refreshToken(@Body requestBody: RequestBody?): com.chaoji.im.network.ApiResponse<RefreshTokenInfo>

    @Headers("Domain-Name: SERVER_URL")
    @POST("api/v1/accountslogin")
    suspend fun newLogin(@Body requestBody: RequestBody?): com.chaoji.im.network.ApiResponse<NewLoginInfo>

    @Headers("Domain-Name: SERVER_URL")
    @POST("api/v1/sendbindimcode")
    suspend fun sendbindImcode(@Body requestBody: RequestBody?): com.chaoji.im.network.ApiResponse<Any?>

    @Headers("Domain-Name: SERVER_URL")
    @POST("api/v1/bindimaccounts")
    suspend fun bindimaccounts(@Body requestBody: RequestBody?): com.chaoji.im.network.ApiResponse<NewLoginInfo>

    @Headers("Domain-Name: SERVER_URL")
    @POST("api/v1/newaccountlogin")
    suspend fun newAccountLogin(): com.chaoji.im.network.ApiResponse<NewLoginInfo>

    @Headers("Domain-Name: SERVER_URL")
    @POST("api/v1/checkim")
    suspend fun checkIm(): ImCheckResultInfo

    @Headers("Domain-Name: SERVER_URL")
    @POST("api/v1/userlogout")
    suspend fun userLogout(@Body requestBody: RequestBody?): com.chaoji.im.network.ApiResponse<Any?>

    @Headers("Domain-Name: SERVER_URL")
    @POST("api/v1/modifypassword")
    suspend fun modifyPassword(@Body requestBody: RequestBody?): com.chaoji.im.network.ApiResponse<Any?>

    @Headers("Domain-Name: SERVER_URL")
    @POST("api/v1/accountsRegister")
    suspend fun register(@Body requestBody: RequestBody?): com.chaoji.im.network.ApiResponse<Any?>

    @Headers("Domain-Name: SERVER_URL")
    @POST("api/v1/sendsmscode")
    suspend fun getVerificationCode(@Body requestBody: RequestBody?): com.chaoji.im.network.ApiResponse<Any?>

    @Headers("Domain-Name: SERVER_URL")
    @GET("api/vi/usermemberinfo")
    suspend fun usermemberinfo(): MutableLiveData<UserUseCountResult>

    @Headers("Domain-Name: SERVER_URL")
    @GET("api/v1/getuserusecount")
    suspend fun getUserUseCount(): com.chaoji.im.network.ApiResponse<UserUseCountResult>

    @POST("api/msg/mark_msgs_as_read")
    suspend fun markReadByConID(@Body requestBody: RequestBody): ApiResponse<Any?>

    @POST("chat/user/finduser")
    suspend fun findUser(@Body requestBody: RequestBody): ApiResponse<FindUserResult>

    @GET("api/v1/loginbytoken")
    @Headers("Domain-Name: SERVER_URL")
    suspend fun aiLogin(
        @Query("code") code: String,
        @Query("deviceID") deviceId: String,
        @Query("device") device: Int
    ): com.chaoji.im.network.ApiResponse<AiResultInfo>

    @GET("chat/account/logincode")
    @Headers("Domain-Name: IM_SERVER_URL")
    suspend fun imLogin(
        @Query("code") code: String,
        @Query("platform") platform: Int
    ): ApiResponse<AppUserInfo>

    @GET("api/chat/assistant")
    @Headers("Domain-Name: SERVER_URL")
    suspend fun assistant(): com.chaoji.im.network.ApiResponse<MutableList<AssistantList>>

    @GET("api/chat/chatagentpconfig")
    @Headers("Domain-Name: SERVER_URL")
    suspend fun aiConfig(): com.chaoji.im.network.ApiResponse<String>

    @POST("api/chat")
    @Headers("Domain-Name: SERVER_URL")
    suspend fun createChat(@Body requestBody: RequestBody): com.chaoji.im.network.ApiResponse<AIChat>

    @GET("api/chat/{id}/chatMessage")
    @Headers("Domain-Name: SERVER_URL")
    suspend fun chatMessage(@Path("id") id: String): com.chaoji.im.network.ApiResponse<List<AiMessage>>

    @DELETE("api/chat/{id}/chatMessage")
    @Headers("Domain-Name: SERVER_URL")
    suspend fun delChatMessage(@Path("id") id: String): com.chaoji.im.network.ApiResponse<Any?>

    @DELETE("api/chat")
    @Headers("Domain-Name: SERVER_URL")
    suspend fun delAllChatMessage(): com.chaoji.im.network.ApiResponse<Any?>

    @GET("api/chat")
    @Headers("Domain-Name: SERVER_URL")
    suspend fun aiChatList(): com.chaoji.im.network.ApiResponse<MutableList<AIChat>>

    @POST("api/chat")
    @Headers("Domain-Name: SERVER_URL")
    suspend fun aiChatCreate(@Body requestBody: RequestBody): com.chaoji.im.network.ApiResponse<AIChat>

    @PUT("api/chat/{id}")
    @Headers("Domain-Name: SERVER_URL")
    suspend fun aiChatName(
        @Path("id") id: String,
        @Body requestBody: RequestBody
    ): com.chaoji.im.network.ApiResponse<Any?>

    @DELETE("api/chat/{id}")
    @Headers("Domain-Name: SERVER_URL")
    suspend fun aiChatDel(@Path("id") id: String): com.chaoji.im.network.ApiResponse<Any?>

    @DELETE("api/chat")
    @Headers("Domain-Name: SERVER_URL")
    suspend fun aiChatDelAll(): com.chaoji.im.network.ApiResponse<Any?>

    @GET("api/chat/{id}/chatMessage")
    @Headers("Domain-Name: SERVER_URL")
    suspend fun aiChatMessage(@Path("id") id: String): com.chaoji.im.network.ApiResponse<MutableList<AiMessage>>

}