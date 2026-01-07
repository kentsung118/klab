package com.kent.lab.performancetest.model.ks

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FeedAuthorKs(
    @SerialName("user_id")
    val userId: String,
    @SerialName("display_name")
    val displayName: String,
    @SerialName("avatar_url")
    val avatarUrl: String
)

@Serializable
data class ReactionKs(
    val type: String, // e.g., "like", "love", "haha"
    val count: Int
)

@Serializable
data class CommentKs(
    @SerialName("comment_id")
    val commentId: String,
    val author: FeedAuthorKs,
    val content: String,
    val timestamp: Long
)

@Serializable
data class MediaItemKs(
    @SerialName("media_type")
    val mediaType: String, // "image" or "video"
    val url: String,
    val width: Int,
    val height: Int
)

@Serializable
data class FeedItemKs(
    @SerialName("feed_id")
    val feedId: String,
    val author: FeedAuthorKs,
    val content: String,
    val timestamp: Long,
    val reactions: List<ReactionKs>,
    val comments: List<CommentKs>,
    val media: List<MediaItemKs>
)

// The root of the JSON is a list of FeedItem
typealias VeryComplexFeedKs = List<FeedItemKs>
