package com.reader.app.data.remote

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        // TODO: Inject TokenManager and add Bearer token
        val request = chain.request().newBuilder()
            // .addHeader("Authorization", "Bearer $token")
            .build()
        return chain.proceed(request)
    }
}
