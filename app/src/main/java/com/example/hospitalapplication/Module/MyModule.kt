package com.example.hospitalapplication.Module

import com.example.hospitalapplication.network.ApiCalls
import com.example.hospitalapplication.utils.Const.BASE_URL
import com.example.hospitalapplication.utils.MySharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MyModule {
    @Singleton
    @Provides
    fun getRetrofit(): Retrofit {
        val client = OkHttpClient.Builder()
            .connectTimeout(50, java.util.concurrent.TimeUnit.SECONDS)
            .writeTimeout(150, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(50, java.util.concurrent.TimeUnit.SECONDS)
            .callTimeout(50, java.util.concurrent.TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    val originalRequest = chain.request()
                    val originalUrl = originalRequest.url
                    val url = originalUrl.newBuilder().build()
                    val requestBuilder = originalRequest.newBuilder().url(url)
                        .addHeader("Accept", "application/json")
                        .addHeader("Authorization", "Bearer ${MySharedPreferences.getUserToken()}"
                        )
                    val request = requestBuilder.build()
                    val response = chain.proceed(request)
                    response.code//status code
                    return response
                }
            })
            .build()

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .baseUrl(BASE_URL)
            .build()
    }

@Singleton
    @Provides
    fun getApiCalls(retrofit: Retrofit): ApiCalls {
        return retrofit.create(ApiCalls::class.java)
    }
}