package com.kent.lab.performancetest.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class SimpleUser(
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
