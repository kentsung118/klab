package com.kent.lab.performancetest.model.ks

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileDetailsKs(
    @SerialName("real_name")
    val realName: String,
    val bio: String?,
    @SerialName("follower_count")
    val followerCount: Int,
    @SerialName("following_count")
    val followingCount: Int
)

@Serializable
data class PostKs(
    @SerialName("post_id")
    val postId: String,
    val title: String,
    @SerialName("like_count")
    val likeCount: Int,
    val comments: List<String>
)

@Serializable
data class ComplexProfileKs(
    val user: SimpleUserKs,
    val details: ProfileDetailsKs,
    @SerialName("recent_posts")
    val recentPosts: List<PostKs>,
    val friends: List<SimpleUserKs>
)
