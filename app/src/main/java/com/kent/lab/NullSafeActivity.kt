package com.kent.lab

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.kent.lab.databinding.ActivityNullSafeBinding
import com.kent.lab.nullsafe.model.GiftModel
import com.kent.lab.nullsafe.model.GiftResponseKs // Import the new Kotlinx.serialization GiftResponse
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.io.IOException

@Serializable
data class User(
    val name: String,
    val age: Int,
    val email: String? = null,
    val country: String = "Taiwan"
)

/**
 * Wrapper class to match the root structure of gifts.json, which is an object containing a "gifts" array.
 */
data class GiftResponse(
    @SerializedName("gifts")
    val gifts: List<GiftModel>
)

class NullSafeActivity : BaseBindingActivity<ActivityNullSafeBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityNullSafeBinding
        get() = ActivityNullSafeBinding::inflate

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        prettyPrint = false
        isLenient = true
        encodeDefaults = false
        allowStructuredMapKeys = true
//        explicitNulls = false
    }


    @OptIn(ExperimentalSerializationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btn1.setOnClickListener {
            Log.d("lala", "btn1 click")
        }

        binding.btn2.setOnClickListener {
            Log.d("lala", "btn2 click")
            // 測試 default value: country 欄位缺失
            // 測試 null safe: email 欄位給 null
            val jsonString = """
                {
                    "name": "John",
                    "age": 30,
                    "email": null
                }
            """.trimIndent()

            try {
                val user = json.decodeFromString<User>(jsonString)
                Log.d("lala", "Parsed user: $user")
                // 預期輸出: Parsed user: User(name=John, age=30, email=null, country=Taiwan)
            } catch (e: Exception) {
                Log.e("lala", "Failed to parse user", e)
            }
        }

        binding.btn3.setOnClickListener {
            Log.d("lala", "btn3 click - Start parsing with Gson")
            val jsonString = readJsonFromAssets("gifts.json")
            if (jsonString == null) {
                Log.e("lala", "Failed to read gifts.json from assets.")
                return@setOnClickListener
            }

            try {
                val gson = Gson()
                Log.d("lala", "btn3 flag1")
                // The JSON is an object, so we parse it to a response class first
                val response = gson.fromJson(jsonString, GiftResponse::class.java)
                val giftList = response.gifts
                Log.d("lala", "Successfully parsed ${giftList.size} gifts with Gson.")
            }
            catch (e: Exception) {
                Log.e("lala", "Failed to parse gifts.json with Gson", e)
            }
        }

        binding.btn4.setOnClickListener {
            Log.d("lala", "btn4 click - Start parsing with Kotlinx.serialization from Stream")
            try {
                // Open the InputStream from assets and use it directly
                assets.open("gifts.json").use { inputStream ->
                    Log.d("lala", "btn4 flag1: InputStream opened")
//                    val response = json.decodeFromString<GiftResponseKs>(jsonString)
                    val response = json.decodeFromStream<GiftResponseKs>(inputStream)
                    val giftList = response.gifts
                    Log.d("lala", "Successfully parsed ${giftList.size} gifts with Kotlinx.serialization from Stream.")
                }
            } catch (e: Exception) {
                // This will catch both IOException and SerializationException
                Log.e("lala", "Failed to parse gifts.json with Kotlinx.serialization from Stream", e)
            }
        }
    }

    private fun readJsonFromAssets(fileName: String): String? {
        return try {
            assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

}