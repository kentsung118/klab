package com.kent.lab.performancetest.model.gson

import com.google.gson.annotations.SerializedName

data class ProfileDetailsGson(
    @SerializedName("real_name")
    val realName: String,

    @SerializedName("bio")
    val bio: String?,

    @SerializedName("follower_count")
    val followerCount: Int,

    @SerializedName("following_count")
    val followingCount: Int
)

data class PostGson(
    @SerializedName("post_id")
    val postId: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("like_count")
    val likeCount: Int,

    @SerializedName("comments")
    val comments: List<String>
)

data class ComplexProfileGson(
    @SerializedName("user")
    val user: SimpleUserGson,

    @SerializedName("details")
    val details: ProfileDetailsGson,

    @SerializedName("recent_posts")
    val recentPosts: List<PostGson>,

    @SerializedName("friends")
    val friends: List<SimpleUserGson>
)
