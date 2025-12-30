package com.kent.lab.nullsafe.model

import android.content.res.Resources
import android.os.Environment
import android.os.Parcelable

import kotlinx.parcelize.Parcelize
import java.io.File
import java.util.Calendar

/**
 * Created by POPO on 2015/11/19.
 */
@Parcelize
class GiftModel : Parcelable, Cloneable {
    enum class GiftType(val value: Int) {
        Normal(0),
        LuckyBag(1),
        Slot(2),
        Dummy(3),
        Poke(4),
        Poll(5),
        MusicProducer(6),
        Texture(7),
        Army(8),
        Elite(9),
        Goods(10),
        LevelUp(11),
        HandDrawSticker(12),
        GameGift(13),
        LayoutGift(14),
        Concert(15),
        PM(16),
        NonRealtime(17),
        LimitGift(18),
        InteractiveGame(19),
        LimitLuckyBag(20)
    }

    // backend define new type value
    // Face: 0 -> 1
    // Gesture: 1 -> 2
    enum class TextureType(val value: Int) {
        FACE(1),
        GESTURE(2);

        companion object {
            fun fromValue(value: Int): TextureType {
                return values().find { it.value == value } ?: FACE
            }
        }
    }

//    var meta: GiftMetaModel? = null // Gift's meta, added in client side 1

    enum class RegionCodeType {
        NONE, ALLREGION, INCLUDE, EXCLUDE
    }

    var giftID: String = ""
    var point = 0

    var eventPoint = 0

    var isEventPointEnabled = false

    var name: String? = null
    var icon: String? = null
    var picture: String? = null
    private var soundTrack: String? = null
    var soundTrackURL: String? = null
    var sequence = 0 // should be check

    var archiveFileName: String? = null
    var width = 0
    var height = 0
    var frameDuration = 0
    var numOfImages = 0
    var leaderboardIcon: String? = null
    var isHidden = 0
    var labelType = ""

    var regionMode = 0
    var comboTime: Long = 0 // should be check, not in gift API : in client side
    var isCombo = false // should be check, not in gift API : in client side
    var regions: ArrayList<String>? = null
    var webpURL: String? = null
    private var webpFilename: String? = null // added in client side
    var webpMD5: String? = null
    var zipPngURL: String? = null
    private var zipPNGFilename: String? = null // added in client side
    var zipPngMD5: String? = null
    var pngZipFileSize: Long? = null
    var textureMD5: String? = null
    var textureURL: String? = null
    private var textureFilename: String? = null // added in client side
    var vffURL: String? = null
    var vffMD5: String? = null
    private var vffFileName: String? = null // added in client side

    var durationMs = 5000
    var replayTimes = 1 // added in client side
    var extID = "" // for surprise bag
//    var tagInfo: TagInfo? = null
    var levelUpGroupID: String? = null // 升級禮
    var levelUpThreshold = 0 // 升級禮
    var level = 0 // 升級禮
    var cumulativeNumber: Long = 0 // 升級禮
    private var type = 0
    var isValidLuckyDraw = false // 全民大抽獎
    private var textureType = 0 // 手勢濾鏡
    var isRedEnvelope = false // Client side
    var isBonusBox = false // Client side
    var sentAt: Long = 0 // Client side
    var redEnvelopeType = 0 // Client side

    var validStartTime = 0L // Gift start time
    var validEndTime = 0L // Gift End time
//    var limitGiftModel: LimitGiftModel? = null // Client side
    var isWhiteListGift = false
//    var collaborationMissionTagInfo: TagInfo? = null // Client side
    var localGenerateAnimationName: String = "" // For Display animation in ".animations/" folder. Generate by client. Not in Gift API
    var localGenerateGiftName: String = "" // For Display animation in ".gifts/" folder. Generate by client. Not in Gift API






    // 主要是 EngagementRewardCore 在記錄 Sent Gift Event 的 ID 使用
    // 規則是：如果送出的禮物是 Level Up 的子袋， Event 要改算在母袋裏
    // 詳見 Spec：https://docs.google.com/document/d/1jPDRvYNT_XXADCzjVLdkgqeLRGuiFJyqMvcNv2pfucI/edit?disco=AAABZguWs74
    val engageGiftId: String
        get() = if (isLevelUpGift) levelUpGroupID.orEmpty() else giftID

    val isLevelUpGift
        get() = !levelUpGroupID.isNullOrEmpty()

    /**
     * "tagInfo": {
     * "anchorColor": "#006666",
     * "fontColor": "#FFFFFF",
     * "tagColor": "#00CCFF",
     * "token": {
     * "key": "gift_tag_new",
     * "params": []
     * }
     * },
     */





    data class VffTagInfoHolder(
        val tag: String = "",
        val source: String = "",
    )

    companion object {
        const val NON_LIMIT_GIFT = 0
        const val LIMIT_GIFT_STATE_NORMAL = 1
        const val LIMIT_GIFT_STATE_TIME_OUT = 2
        const val LIMIT_GIFT_STATE_SOLD_OUT = 3
        const val LIMIT_GIFT_STATE_NO_QUOTA = 4

        fun isTextureGift(gift: GiftModel?): Boolean {
            return !gift?.textureURL.isNullOrEmpty()
        }
    }
}