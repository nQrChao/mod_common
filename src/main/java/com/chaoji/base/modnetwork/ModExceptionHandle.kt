package com.chaoji.base.modnetwork

import android.net.ParseException
import com.google.gson.JsonParseException
import com.google.gson.stream.MalformedJsonException
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException


object ModExceptionHandle {

    fun handleException(e: Throwable?): ModAppException {
        val ex: ModAppException
        e?.let {
            when (it) {
                is HttpException -> {
                    ex = ModAppException(ModError.NETWORK_ERROR,e)
                    return ex
                }
                is JsonParseException, is JSONException, is ParseException, is MalformedJsonException -> {
                    ex = ModAppException(ModError.PARSE_ERROR,e)
                    return ex
                }
                is ConnectException -> {
                    ex = ModAppException(ModError.NETWORK_ERROR,e)
                    return ex
                }
                is javax.net.ssl.SSLException -> {
                    ex = ModAppException(ModError.SSL_ERROR,e)
                    return ex
                }
                is ConnectTimeoutException -> {
                    ex = ModAppException(ModError.TIMEOUT_ERROR,e)
                    return ex
                }
                is java.net.SocketTimeoutException -> {
                    ex = ModAppException(ModError.TIMEOUT_ERROR,e)
                    return ex
                }
                is java.net.UnknownHostException -> {
                    ex = ModAppException(ModError.TIMEOUT_ERROR,e)
                    return ex
                }
                is ModAppException -> return it

                else -> {
                    ex = ModAppException(ModError.UNKNOWN,e)
                    return ex
                }
            }
        }
        ex = ModAppException(ModError.UNKNOWN,e)
        return ex
    }
}