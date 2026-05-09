package com.turkcell.data.di

import com.turkcell.core.domain.AuthRepository
import com.turkcell.data.remote.AuthApi
import com.turkcell.data.repository.AuthRepositoryImpl
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory


val dataModule = module {

    single {
        Retrofit.Builder()
            .baseUrl("https://tickets-api.halitkalayci.com")
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    // 2. AuthApi Tanımı
    single<AuthApi> { get<Retrofit>().create(AuthApi::class.java) }

    // 3. AuthRepository Tanımı
    single<AuthRepository> { AuthRepositoryImpl(authApi = get()) }
}
