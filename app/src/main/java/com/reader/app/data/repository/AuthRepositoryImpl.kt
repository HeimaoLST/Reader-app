package com.reader.app.data.repository

import com.reader.app.data.local.TokenManager
import com.reader.app.data.remote.api.AuthApi
import com.reader.app.data.remote.dto.AuthResponse
import com.reader.app.data.remote.dto.LoginRequest
import com.reader.app.data.remote.dto.RegisterRequest
import com.reader.app.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi,
    private val tokenManager: TokenManager
) : AuthRepository {

    override suspend fun login(request: LoginRequest): Result<AuthResponse> {
        return try {
            val response = api.login(request)
            tokenManager.saveAccessToken(response.accessToken)
            tokenManager.saveRefreshToken(response.refreshToken)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(request: RegisterRequest): Result<AuthResponse> {
        return try {
            val response = api.register(request)
            tokenManager.saveAccessToken(response.accessToken)
            tokenManager.saveRefreshToken(response.refreshToken)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun isLoggedIn(): Boolean {
        return tokenManager.getAccessToken() != null
    }

    override fun logout() {
        tokenManager.clearTokens()
    }
}
