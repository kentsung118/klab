package com.kent.lab.performancetest.model.gson

import com.google.gson.annotations.SerializedName

data class SimpleUserGson(
    @SerializedName("id")
    val id: Long,

    @SerializedName("username")
    val username: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("is_active")
    val isActive: Boolean,

    @SerializedName("registration_timestamp")
    val registrationTimestamp: Long
)
