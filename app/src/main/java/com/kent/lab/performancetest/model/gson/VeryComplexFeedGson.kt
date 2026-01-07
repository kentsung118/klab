package com.kent.lab.performancetest.model.gson

import com.google.gson.annotations.SerializedName

data class FeedAuthorGson(
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("display_name")
    val displayName: String,
    @SerializedName("avatar_url")
    val avatarUrl: String
)

data class ReactionGson(
    @SerializedName("type")
    val type: String,
    @SerializedName("count")
    val count: Int
)

data class CommentGson(
    @SerializedName("comment_id")
    val commentId: String,
    @SerializedName("author")
    val author: FeedAuthorGson,
    @SerializedName("content")
    val content: String,
    @SerializedName("timestamp")
    val timestamp: Long
)

data class MediaItemGson(
    @SerializedName("media_type")
    val mediaType: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("width")
    val width: Int,
    @SerializedName("height")
    val height: Int
)

data class FeedItemGson(
    @SerializedName("feed_id")
    val feedId: String,
    @SerializedName("author")
    val author: FeedAuthorGson,
    @SerializedName("content")
    val content: String,
    @SerializedName("timestamp")
    val timestamp: Long,
    @SerializedName("reactions")
    val reactions: List<ReactionGson>,
    @SerializedName("comments")
    val comments: List<CommentGson>,
    @SerializedName("media")
    val media: List<MediaItemGson>
)

// The root of the JSON is a list of FeedItem
typealias VeryComplexFeedGson = List<FeedItemGson>
