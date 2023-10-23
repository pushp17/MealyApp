package com.eat_healthy.tiffin.utils

import android.util.Log
import retrofit2.HttpException
import retrofit2.Response
import java.io.EOFException
import java.net.HttpURLConnection
import java.net.UnknownHostException
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLHandshakeException

abstract class BaseDataSource {

    protected suspend fun <T> invoke(call: suspend () -> Response<T>): DataState<T?> {
        var response: Response<T>? = null
        try {
            response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null){
                    return DataState.Success(body, 200)
                }else{
                    return DataState.Success(body, 201)
                }
            }
            response.errorBody()?.let {
                when (response.code()) {
                    HttpsURLConnection.HTTP_INTERNAL_ERROR -> {
                        return error(response.code(), "Internal Server Error")
                    }
                    HttpsURLConnection.HTTP_BAD_REQUEST -> {
                        return error(response.code(), "Internal Server Error")
                    }
                    HttpsURLConnection.HTTP_CLIENT_TIMEOUT -> {
                        return error(response.code(), "Timeout , Please check internet connection")
                    }
                    HttpsURLConnection.HTTP_FORBIDDEN -> {
                        return error(response.code(), "Forbidden")
                    }
                    HttpsURLConnection.HTTP_GATEWAY_TIMEOUT -> {
                        return error(response.code(), "Gateway Timeout")
                    }
                    HttpsURLConnection.HTTP_CONFLICT -> {
                        return error(response.code(), "Conflict")
                    }
                    else -> {
                        return error(response.code(), "Unknown Error")
                    }
                }
            }
            return error(response.code(), response.message())
        }
        catch (e: HttpException) {
            e.printStackTrace()
            Log.e("BaseDataSource", "HttpException occurred")
            return error(HttpURLConnection.HTTP_VERSION,e.message ?: e.toString())
        } catch (e: EOFException) {
            e.printStackTrace()
            Log.e("BaseDataSource", "No content found")
            return error(HttpURLConnection.HTTP_NO_CONTENT,e.message ?: e.toString())
        } catch (e: SSLHandshakeException) {
            e.printStackTrace()
            Log.e("BaseDataSource", "Exception occurred", e)
            return error(HttpURLConnection.HTTP_GATEWAY_TIMEOUT,e.message ?: e.toString())
        }
        catch (e: UnknownHostException) {
            e.printStackTrace()
            Log.e("BaseDataSource", "Exception occurred", e)
            return error(HttpURLConnection.HTTP_INTERNAL_ERROR,"Something went wrong" ?: e.toString())
        }
        catch (e: Exception) {
            e.printStackTrace()
            Log.e("BaseDataSource", "Exception occurred")
            return error(HttpURLConnection.HTTP_INTERNAL_ERROR,e.message ?: e.toString())
        }

    }

    /**
     * Wrap the error to be sent into [DataWrapper].
     */
    private fun <T> error(statusCode: Int, message: String): DataState<T> {
        return DataState.Error(statusCode, null,message)
    }

    /**
     * Wrap the error to be sent into [DataWrapper].
     * This method is used to send data along with the error message.
     * This is an overloaded version of method [error].
     */
    private fun <T> error(statusCode: Int, message: String, data: T?): DataState<T> {
        return DataState.Error(statusCode, null,message)
    }

}

