package com.kent.lab

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.kent.lab.databinding.ActivityNullSafeBinding
import com.kent.lab.nullsafe.model.GiftModel
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
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
    val gifts: List<GiftModel>
)

class NullSafeActivity : BaseBindingActivity<ActivityNullSafeBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityNullSafeBinding
        get() = ActivityNullSafeBinding::inflate

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }


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
                // The JSON is an object, so we parse it to a response class first
                val response = gson.fromJson(jsonString, GiftResponse::class.java)
                val giftList = response.gifts
                Log.d("lala", "Successfully parsed ${giftList.size} gifts with Gson.")
            } catch (e: Exception) {
                Log.e("lala", "Failed to parse gifts.json with Gson", e)
            }
        }

        binding.btn4.setOnClickListener {

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