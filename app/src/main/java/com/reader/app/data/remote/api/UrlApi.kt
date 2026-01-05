package com.reader.app.data.remote.api

import com.reader.app.data.remote.dto.*
import retrofit2.http.*

interface UrlApi {
    @POST("urls")
    suspend fun createUrl(@Body request: CreateUrlRequest): UrlDto

    @GET("urls")
    suspend fun getUrls(
        @Query("page") page: Int? = null,
        @Query("limit") limit: Int? = null,
        @Query("is_read") isRead: Boolean? = null,
        @Query("is_favorite") isFavorite: Boolean? = null
    ): UrlListResponse

    @GET("urls/{id}")
    suspend fun getUrl(@Path("id") id: String): UrlDto

    @PUT("urls/{id}")
    suspend fun updateUrl(@Path("id") id: String, @Body request: Map<String, String>): UrlDto

    @POST("urls/{id}/read")
    suspend fun markRead(@Path("id") id: String, @Body request: MarkReadRequest)

    @POST("urls/{id}/favorite")
    suspend fun toggleFavorite(@Path("id") id: String, @Body request: ToggleFavoriteRequest)

    @POST("urls/{id}/archive")
    suspend fun archive(@Path("id") id: String, @Body request: ArchiveRequest)

    @DELETE("urls/{id}")
    suspend fun deleteUrl(@Path("id") id: String)

    @GET("urls/{id}/reader")
    suspend fun getReaderView(@Path("id") id: String): ReaderViewResponse

    @POST("urls/{id}/reparse")
    suspend fun reparse(@Path("id") id: String)
}
