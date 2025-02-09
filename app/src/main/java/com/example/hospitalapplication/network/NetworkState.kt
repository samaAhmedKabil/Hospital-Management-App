package com.example.hospitalapplication.network

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import java.io.IOException
import java.net.SocketTimeoutException

class NetworkState constructor(val status: Status, val msg: Any? = null, val data: Any? = null) {

        enum class Status {
            RUNNING, FAILED, SUCCESS
        }

        companion object {
            fun getLoaded(dataSuccess: Any?): NetworkState {
                return NetworkState(Status.SUCCESS, data = dataSuccess)
            }

            var LOADING: NetworkState = NetworkState(Status.RUNNING)

            @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
            fun getErrorMessage(throwable: Throwable): NetworkState {
                return when (throwable) {
                    is IOException -> {
                        NetworkState(Status.FAILED, "No Connection ")
                    }
                    is SocketTimeoutException -> {
                        NetworkState(Status.FAILED, "Bad Connection")
                    }
                    is HttpException -> {

                        NetworkState(Status.FAILED, "HttpException  ")
                    }
                    else -> {
                        NetworkState(Status.FAILED, "Error")
                    }
                }
            }

            fun getErrorMessage(massage: String): NetworkState {
                return NetworkState(Status.FAILED, massage)

            }

        }
    }