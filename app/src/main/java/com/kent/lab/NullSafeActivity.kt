package com.kent.lab

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import com.kent.lab.databinding.ActivityNullSafeBinding
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class User(
    val name: String,
    val age: Int,
    val email: String? = null,
    val country: String = "Taiwan"
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
                    "email": null,
                    "country": "JP"
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
    }

}