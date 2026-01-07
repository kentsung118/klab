package com.kent.lab

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kent.lab.performancetest.model.gson.ComplexProfileGson
import com.kent.lab.performancetest.model.gson.SimpleUserGson
import com.kent.lab.performancetest.model.gson.VeryComplexFeedGson
import com.kent.lab.performancetest.model.ks.ComplexProfileKs
import com.kent.lab.performancetest.model.ks.SimpleUserKs
import com.kent.lab.performancetest.model.ks.VeryComplexFeedKs
import kotlinx.serialization.json.Json
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import kotlin.system.measureTimeMillis

class SerializationPerformanceTest {

    private lateinit var simpleUserJsonString: String
    private lateinit var complexProfileJsonString: String
    private lateinit var veryComplexFeedJsonString: String

    private val gson = Gson()
    private val kJson = Json {
        ignoreUnknownKeys = true
    }

    companion object {
        private const val ITERATIONS = 1_000 // 執行 1000 次來計算平均效能
    }

    @Before
    fun setup() {
        // 從 test resources 讀取 JSON 檔案
        // 這是 JUnit 測試中讀取資源的標準方式
        simpleUserJsonString = readResourceFile("simple_user.json")
        complexProfileJsonString = readResourceFile("complex_profile.json")
        veryComplexFeedJsonString = readResourceFile("very_complex_feed.json")
    }

    private fun readResourceFile(fileName: String): String {
        return this.javaClass.classLoader
            ?.getResourceAsStream(fileName)
            ?.bufferedReader()
            .use { it?.readText() } ?: throw IllegalStateException("Cannot read resource file: $fileName")
    }

    // --- Simple User Tests ---
    @Test
    fun testGson_SimpleUser_Performance() {
        val parsedOnce = gson.fromJson(simpleUserJsonString, SimpleUserGson::class.java)
        assertNotNull(parsedOnce)

        val time = measureTimeMillis {
            for (i in 1..ITERATIONS) {
                gson.fromJson(simpleUserJsonString, SimpleUserGson::class.java)
            }
        }
        println("Performance - Gson - SimpleUser ($ITERATIONS iterations): ${time}ms")
    }

    @Test
    fun testKotlinxSerialization_SimpleUser_Performance() {
        val parsedOnce = kJson.decodeFromString<SimpleUserKs>(simpleUserJsonString)
        assertNotNull(parsedOnce)

        val time = measureTimeMillis {
            for (i in 1..ITERATIONS) {
                kJson.decodeFromString<SimpleUserKs>(simpleUserJsonString)
            }
        }
        println("Performance - Kotlinx - SimpleUser ($ITERATIONS iterations): ${time}ms")
    }

    // --- Complex Profile Tests ---
    @Test
    fun testGson_ComplexProfile_Performance() {
        val parsedOnce = gson.fromJson(complexProfileJsonString, ComplexProfileGson::class.java)
        assertNotNull(parsedOnce)
        assert(parsedOnce.recentPosts.isNotEmpty())

        val time = measureTimeMillis {
            for (i in 1..ITERATIONS) {
                gson.fromJson(complexProfileJsonString, ComplexProfileGson::class.java)
            }
        }
        println("Performance - Gson - ComplexProfile ($ITERATIONS iterations): ${time}ms")
    }

    @Test
    fun testKotlinxSerialization_ComplexProfile_Performance() {
        val parsedOnce = kJson.decodeFromString<ComplexProfileKs>(complexProfileJsonString)
        assertNotNull(parsedOnce)
        assert(parsedOnce.recentPosts.isNotEmpty())

        val time = measureTimeMillis {
            for (i in 1..ITERATIONS) {
                kJson.decodeFromString<ComplexProfileKs>(complexProfileJsonString)
            }
        }
        println("Performance - Kotlinx - ComplexProfile ($ITERATIONS iterations): ${time}ms")
    }

    // --- Very Complex Feed Tests ---
    @Test
    fun testGson_VeryComplexFeed_Performance() {
        val listType = object : TypeToken<VeryComplexFeedGson>() {}.type
        val parsedOnce: VeryComplexFeedGson = gson.fromJson(veryComplexFeedJsonString, listType)
        assertNotNull(parsedOnce)
        assert(parsedOnce.isNotEmpty())

        val time = measureTimeMillis {
            for (i in 1..ITERATIONS) {
                gson.fromJson<VeryComplexFeedGson>(veryComplexFeedJsonString, listType)
            }
        }
        println("Performance - Gson - VeryComplexFeed ($ITERATIONS iterations): ${time}ms")
    }

    @Test
    fun testKotlinxSerialization_VeryComplexFeed_Performance() {
        val parsedOnce = kJson.decodeFromString<VeryComplexFeedKs>(veryComplexFeedJsonString)
        assertNotNull(parsedOnce)
        assert(parsedOnce.isNotEmpty())

        val time = measureTimeMillis {
            for (i in 1..ITERATIONS) {
                kJson.decodeFromString<VeryComplexFeedKs>(veryComplexFeedJsonString)
            }
        }
        println("Performance - Kotlinx - VeryComplexFeed ($ITERATIONS iterations): ${time}ms")
    }
}
