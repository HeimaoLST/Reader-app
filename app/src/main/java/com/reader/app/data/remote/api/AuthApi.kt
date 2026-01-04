package com.reader.app.data.remote.api

import retrofit2.http.POST
import retrofit2.http.Body

interface AuthApi {
    // TODO: Define data classes for LoginRequest, LoginResponse
    
    @POST("auth/login")
    suspend fun login(@Body request: com.reader.app.data.remote.dto.LoginRequest): com.reader.app.data.remote.dto.AuthResponse

    @POST("auth/register")
    suspend fun register(@Body request: com.reader.app.data.remote.dto.RegisterRequest): com.reader.app.data.remote.dto.AuthResponse
}
