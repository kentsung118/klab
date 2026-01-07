package com.kent.lab.performancetest.model.ks

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SimpleUserKs(
    val id: Long,
    val username: String,
    val email: String,
    @SerialName("is_active")
    val isActive: Boolean,
    @SerialName("registration_timestamp")
    val registrationTimestamp: Long
)
