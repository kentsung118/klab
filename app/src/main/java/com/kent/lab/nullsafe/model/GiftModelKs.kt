package com.kent.lab.nullsafe.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GiftResponseKs(
    @SerialName("gifts")
    val gifts: List<GiftModelKs>
)

@Serializable
data class GiftModelKs(
    @SerialName("giftID") val giftID: String,
    @SerialName("point") val point: Int,
    @SerialName("eventPoint") val eventPoint: Int,
    @SerialName("isEventPointEnabled") val isEventPointEnabled: Boolean,
    @SerialName("name") val name: String?,
    @SerialName("icon") val icon: String?,
    @SerialName("picture") val picture: String?,
    // Note: 'soundTrack' was private in original, assuming it's not in JSON or maps to soundTrackURL
    @SerialName("soundTrackURL") val soundTrackURL: String?,
    @SerialName("sequence") val sequence: Int = 0,
    @SerialName("archiveFileName") val archiveFileName: String?,
    @SerialName("width") val width: Int,
    @SerialName("height") val height: Int,
    @SerialName("frameDuration") val frameDuration: Int,
    @SerialName("numOfImages") val numOfImages: Int,
    @SerialName("leaderboardIcon") val leaderboardIcon: String?,
    @SerialName("isHidden") val isHidden: Int,
    @SerialName("labelType") val labelType: String,
    @SerialName("regionMode") val regionMode: Int,
    @SerialName("comboTime") val comboTime: Long = 0L,
    @SerialName("isCombo") val isCombo: Boolean = false,
    @SerialName("regions") val regions: ArrayList<String>?,
    @SerialName("webpURL") val webpURL: String?,
    // Note: 'webpFilename' was private in original
    @SerialName("webpMD5") val webpMD5: String?,
    @SerialName("zipPngURL") val zipPngURL: String?,
    // Note: 'zipPNGFilename' was private in original
    @SerialName("zipPngMD5") val zipPngMD5: String?,
    @SerialName("pngZipFileSize") val pngZipFileSize: Long?,
    @SerialName("textureMD5") val textureMD5: String?,
    @SerialName("textureURL") val textureURL: String?,
    // Note: 'textureFilename' was private in original
    @SerialName("vffURL") val vffURL: String?,
    @SerialName("vffMD5") val vffMD5: String?,
    // Note: 'vffFileName' was private in original
    @SerialName("durationMs") val durationMs: Int,
    @SerialName("replayTimes") val replayTimes: Int = 0,
    @SerialName("extID") val extID: String = "",
    // @SerialName("tagInfo") val tagInfo: TagInfo?, // Commented out as TagInfo not provided
    @SerialName("levelUpGroupID") val levelUpGroupID: String?,
    @SerialName("levelUpThreshold") val levelUpThreshold: Int,
    @SerialName("level") val level: Int,
    @SerialName("cumulativeNumber") val cumulativeNumber: Long,
    @SerialName("type") val type: Int, // Represents GiftType value
    @SerialName("isValidLuckyDraw") val isValidLuckyDraw: Boolean = false,
    @SerialName("textureType") val textureType: Int, // Represents TextureType value
    @SerialName("isRedEnvelope") val isRedEnvelope: Boolean = false,
    @SerialName("isBonusBox") val isBonusBox: Boolean = false,
    @SerialName("sentAt") val sentAt: Long,
    @SerialName("redEnvelopeType") val redEnvelopeType: Int = 0,
    @SerialName("validStartTime") val validStartTime: Long,
    @SerialName("validEndTime") val validEndTime: Long,
    // @SerialName("limitGiftModel") val limitGiftModel: LimitGiftModel?, // Commented out as LimitGiftModel not provided
    @SerialName("isWhiteListGift") val isWhiteListGift: Boolean = false,
    // @SerialName("collaborationMissionTagInfo") val collaborationMissionTagInfo: TagInfo?, // Commented out as TagInfo not provided
    @SerialName("localGenerateAnimationName") val localGenerateAnimationName: String = "",
    @SerialName("localGenerateGiftName") val localGenerateGiftName: String = ""
)
