package com.kent.lab.performancetest.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileDetails(
    @SerializedName("real_name")
    val realName: String,

    @SerializedName("bio")
    val bio: String?,

    @SerializedName("follower_count")
    val followerCount: Int,

    @SerializedName("following_count")
    val followingCount: Int
)

@Serializable
data class Post(
    @SerializedName("post_id")
    val postId: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("like_count")
    val likeCount: Int,

    @SerializedName("comments")
    val comments: List<String>
)

@Serializable
data class ComplexProfile(
    @SerializedName("user")
    val user: SimpleUser,

    @SerializedName("details")
    val details: ProfileDetails,

    @SerializedName("recent_posts")
    val recentPosts: List<Post>,

    @SerializedName("friends")
    val friends: List<SimpleUser>
)
