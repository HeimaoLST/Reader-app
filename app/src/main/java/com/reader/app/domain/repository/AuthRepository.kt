package com.reader.app.domain.repository

import com.reader.app.data.remote.dto.AuthResponse
import com.reader.app.data.remote.dto.LoginRequest
import com.reader.app.data.remote.dto.RegisterRequest
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(request: LoginRequest): Result<AuthResponse>
    suspend fun register(request: RegisterRequest): Result<AuthResponse>
    fun isLoggedIn(): Boolean
    fun logout()
}
