package com.reader.app.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UrlDto(
    @SerializedName("id") val id: String,
    @SerializedName("original_url") val originalUrl: String,
    @SerializedName("normalized_url") val normalizedUrl: String?,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String?,
    @SerializedName("domain") val domain: String?,
    @SerializedName("company") val company: String?,
    @SerializedName("content_type") val contentType: String?,
    @SerializedName("parse_status") val parseStatus: String,
    @SerializedName("is_read") val isRead: Boolean,
    @SerializedName("is_favorite") val isFavorite: Boolean,
    @SerializedName("is_archived") val isArchived: Boolean,
    @SerializedName("collected_at") val collectedAt: String,
    @SerializedName("created_at") val createdAt: String
)

data class CreateUrlRequest(
    @SerializedName("url") val url: String,
    @SerializedName("source") val source: String = "android"
)

data class UrlListResponse(
    @SerializedName("data") val data: List<UrlDto>,
    @SerializedName("total") val total: Int,
    @SerializedName("page") val page: Int,
    @SerializedName("limit") val limit: Int
)

data class ReaderViewResponse(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("author") val author: String?,
    @SerializedName("site_name") val siteName: String?,
    @SerializedName("published_at") val publishedAt: String?,
    @SerializedName("reading_time") val readingTime: Int,
    @SerializedName("word_count") val wordCount: Int,
    @SerializedName("content") val content: String,
    @SerializedName("parse_status") val parseStatus: String
)

data class MarkReadRequest(
    @SerializedName("is_read") val isRead: Boolean
)

data class ToggleFavoriteRequest(
    @SerializedName("is_favorite") val isFavorite: Boolean
)

data class ArchiveRequest(
    @SerializedName("is_archived") val isArchived: Boolean
)
