package com.turkcell.data.network

import com.turkcell.data.local.TokenStore
import okhttp3.Interceptor
import okhttp3.Response

//Her isteğe otomatik token ekle
class AuthInterceptor(private val tokenStore: TokenStore) : Interceptor
{
    private val authPaths = setOf(
        "/auth/login",
        "/auth/register",
        "/auth/refresh"
    )


    override fun intercept(chain: Interceptor.Chain): Response {
        //chain.request() ile zincire gelen orijinal isteği alıyor. encodedPath ise URL'in sadece path kısmını veriyor
        // — örneğin https://api.example.com/auth/login?foo=bar için
        // /auth/login döner. Query string ve host dahil değil, bu yüzden authPaths ile karşılaştırma sağlıklı oluyor.
        val original = chain.request()
        val path = original.url.encodedPath

        if(path in authPaths) return chain.proceed(original)

        val token = tokenStore.accessTokenBlocking() ?: return chain.proceed(original)

        val authedRequest = original
            .newBuilder() // original isteğin klonunu yaratır..
            .header("Authorization", "Bearer $token")
            .build()
        return chain.proceed(authedRequest)
    }

}