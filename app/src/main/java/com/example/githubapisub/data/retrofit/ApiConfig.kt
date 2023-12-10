package com.example.githubapisub.data.retrofit

import com.example.githubapisub.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    const val BASE_URL = "https://api.github.com/"
    fun getApiService(): ApiService {
        val mySuperScretKey = BuildConfig.KEY
        val authInterceptor = Interceptor { chain ->
            val req = chain.request()
            val requestHeaders = req.newBuilder()
                .addHeader("Authorization", mySuperScretKey)
                .build()
            chain.proceed(requestHeaders)
        }

        val rateLimitInterceptor = Interceptor { chain ->
            val response = chain.proceed(chain.request())
            val limit = response.header("X-RateLimit-Limit")
            val remaining = response.header("X-RateLimit-Remaining")
            val resetTime = response.header("X-RateLimit-Reset")

            println("API Limit: $limit")
            println("Remaining Requests: $remaining")
            println("Reset Time: $resetTime")

            response
        }
        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(rateLimitInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ApiService::class.java)
    }
}