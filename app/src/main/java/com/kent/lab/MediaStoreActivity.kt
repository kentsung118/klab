package com.kent.lab

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import com.kent.lab.databinding.ActivityMediastoreBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import java.io.File
import java.io.InputStream


class MediaStoreActivity : BaseBindingActivity<ActivityMediastoreBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityMediastoreBinding
        get() = ActivityMediastoreBinding::inflate

    val okHttpClient by lazy {
        OkHttpClient.Builder()
            .retryOnConnectionFailure(false)
            .hostnameVerifier { _, _ -> true }
            .build()
    }

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.btn1.setOnClickListener {
            downloadFile()
        }

        binding.btn2.setOnClickListener {
            Log.d("lala", "btn2")

            queryMediaFiles(this, "new")
        }
    }

    private fun downloadFile() {
        val url = "http://cdn.17app.co/go-prod/clip/2kxdyGQLXElGcYc1XJLDY3Own8j_20240821083853.mp4"
        val context = this

        coroutineScope.launch(Dispatchers.IO) {
            val request = Request.Builder().url(url).build()
            val response = okHttpClient.newCall(request).execute()
            if (!response.isSuccessful) return@launch

            val body: ResponseBody = response.body ?: return@launch
            val contentLength = body.contentLength()
            Log.d("lala", "contentLength=$contentLength")

            insertMp4FileIntoMediaStore(context, body.byteStream(), "", "")
        }

    }

    fun insertMp4FileIntoMediaStore(
        context: Context,
        inputStream: InputStream,
        targetDir: String,
        fileName: String,
    ): Uri? {
        // 设置要插入到 MediaStore 的文件信息
        val values = ContentValues().apply {
            put(MediaStore.Video.Media.DISPLAY_NAME, "new.mp4") // 文件名
            put(MediaStore.Video.Media.MIME_TYPE, "video/mp4") // 文件类型
            put(MediaStore.Video.Media.RELATIVE_PATH, Environment.DIRECTORY_DCIM) // 存储目录
            put(MediaStore.Video.Media.DATE_ADDED, System.currentTimeMillis() / 1000) // 添加时间
            put(MediaStore.Video.Media.DATE_TAKEN, System.currentTimeMillis()) // 拍摄时间
            put(MediaStore.Video.Media.DATE_MODIFIED, System.currentTimeMillis() / 1000) // 修改时间
        }

        val file = File(targetDir, fileName)

        // 通过 ContentResolver 插入到 MediaStore
        val resolver = context.contentResolver
        val collection = MediaStore.Video.Media.EXTERNAL_CONTENT_URI

        Log.d("lala", "insertMp4FileIntoMediaStore flag1")

        // 插入文件并获取 URI
        val uri: Uri? = resolver.insert(collection, values)
        Log.d("lala", "insertMp4FileIntoMediaStore flag2")

        // 如果 URI 不为空，开始写入文件内容
        uri?.let {

            Log.d("lala", "insertMp4FileIntoMediaStore flag3")
            resolver.openOutputStream(uri)?.use { outputStream ->
//                FileInputStream(mp4File).use { inputStream ->
                inputStream.copyTo(outputStream) // 将实际文件数据写入
//                }
            }
        }
        Log.d("lala", "insertMp4FileIntoMediaStore flag4")

        return uri
    }

    fun queryMediaFile(context: Context, fileName: String) {
        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.DATE_MODIFIED,
            MediaStore.Video.Media.SIZE
        )

        // Query only files from the DCIM folder
        val selection = "${MediaStore.Video.Media.DISPLAY_NAME} = ? AND ${MediaStore.Video.Media.RELATIVE_PATH} LIKE ?"
        val selectionArgs = arrayOf(fileName, "%DCIM%")

        // Sort by DATE_MODIFIED in descending order (newest first)
        val sortOrder = "${MediaStore.Video.Media.DATE_MODIFIED} DESC"

        // Querying the MediaStore for video files
        val queryUri: Uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI

        val cursor: Cursor? = context.contentResolver.query(
            queryUri,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )
        if(cursor == null){
            Log.d("lala", "not found")

        }

        cursor?.use {
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            val nameColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
            val dateModifiedColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_MODIFIED)
            Log.d("lala", "flag1")
            while (it.moveToNext()) {
                Log.d("lala", "flag2")

                val id = it.getLong(idColumn)
                val name = it.getString(nameColumn)
                val dateModified = it.getLong(dateModifiedColumn)

                // Use the file's content Uri
                val contentUri: Uri = Uri.withAppendedPath(queryUri, id.toString())

                Log.d("lala", "Found file: $name with Uri: $contentUri and modified date: $dateModified")
            }
        }
    }

    fun queryMediaFiles(context: Context, keyword: String) {
        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.DATE_MODIFIED,
            MediaStore.Video.Media.SIZE
        )

        // Selection to search for files that contain 'keyword' in their name and are in DCIM folder
        val selection = "${MediaStore.Video.Media.DISPLAY_NAME} LIKE ? AND ${MediaStore.Video.Media.RELATIVE_PATH} LIKE ? AND ${MediaStore.Video.Media.MIME_TYPE} = ?"
        val selectionArgs = arrayOf("%$keyword%", "%DCIM%", "video/mp4")

        // Sort by DATE_MODIFIED in descending order
        val sortOrder = "${MediaStore.Video.Media.DATE_MODIFIED} DESC"

        // Querying the MediaStore for video files
        val queryUri: Uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI

        val cursor: Cursor? = context.contentResolver.query(
            queryUri,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )

        cursor?.use {
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            val nameColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
            val dateModifiedColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_MODIFIED)
            val sizeColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)

            while (it.moveToNext()) {
                val id = it.getLong(idColumn)
                val name = it.getString(nameColumn)
                val dateModified = it.getLong(dateModifiedColumn)
                val size = it.getLong(sizeColumn)

                // Use the file's content Uri
                val contentUri: Uri = Uri.withAppendedPath(queryUri, id.toString())

                Log.d("lala", "Found file: $name with Uri: $contentUri and modified date: $dateModified, size = $size")
            }
        } ?: run {
            Log.d("lala", "No files found matching the query.")
        }
    }
}