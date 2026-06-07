package com.turkcell.data.repository

import com.turkcell.core.domain.auth.AuthRepository
import com.turkcell.core.domain.auth.AuthSession
import com.turkcell.core.domain.auth.User
import com.turkcell.core.domain.auth.UserRole
import com.turkcell.data.dto.CredentialsDto
import com.turkcell.data.dto.LogoutRequestDto
import com.turkcell.data.local.TokenStore
import com.turkcell.data.remote.AuthApi
import com.turkcell.data.util.runCatchingApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val tokenStore: TokenStore
) : AuthRepository {

    override val isLoggedIn: Flow<Boolean> = tokenStore.accessToken.map { it != null }

    override val currentRole: Flow<UserRole> = tokenStore.userRole

    override suspend fun login(
        email: String,
        password: String
    ): Result<AuthSession> = runCatchingApi {
        authApi.login(CredentialsDto(email = email, password = password))
    }.onSuccess {
        tokenStore.save(it.accessToken, it.refreshToken, it.user.role) // ← role eklendi
    }.map { tokenPairDto ->
        AuthSession(
            user = User(
                tokenPairDto.user.id,
                tokenPairDto.user.email,
                UserRole.fromApi(tokenPairDto.user.role)
            ),
            accessToken = tokenPairDto.accessToken,
            refreshToken = tokenPairDto.refreshToken
        )
    }

    override suspend fun register(
        email: String,
        password: String
    ): Result<AuthSession> = runCatchingApi {
        authApi.register(CredentialsDto(email = email, password = password))
    }.onSuccess {
        tokenStore.save(it.accessToken, it.refreshToken, it.user.role) // ← düzeltildi
    }.map { i ->
        AuthSession(
            user = User(
                i.user.id,
                i.user.email,
                UserRole.fromApi(i.user.role)
            ),
            accessToken = i.accessToken,
            refreshToken = i.refreshToken
        )
    }

    override suspend fun logout(): Result<Unit> {
        val refreshToken = tokenStore.refreshTokenBlocking()
        if (refreshToken != null) {
            runCatchingApi { authApi.logout(LogoutRequestDto(refreshToken)) }
        }
        tokenStore.clear()
        return Result.success(Unit)
    }
}