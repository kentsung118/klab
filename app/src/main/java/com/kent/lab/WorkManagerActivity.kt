package com.kent.lab

import android.content.Context
import android.media.MediaScannerConnection
import android.os.Bundle
import android.os.Environment
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.kent.lab.databinding.ActivityWorkBinding
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

class WorkManagerActivity : BaseBindingActivity<ActivityWorkBinding>() {

    override val bindingInflater: (LayoutInflater) -> ActivityWorkBinding
        get() = ActivityWorkBinding::inflate


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.btn1.setOnClickListener {
            Log.d("lala", "work request flag1")
            val videoUrl = "https://cdn.17app.co/go-prod/clip/2bcKp1KrusUrSiO14qdNAZPTwPK_20240129062031.mp4"

            val downloadRequest = OneTimeWorkRequestBuilder<DownloadWorker>()
                .setInputData(workDataOf("DOWNLOAD_URL" to videoUrl))
                .build()

            WorkManager.getInstance(this).enqueue(downloadRequest)
            Log.d("lala", "work request flag2")

        }
    }

    class DownloadWorker(val appContext: Context, workerParams: WorkerParameters) :
        CoroutineWorker(appContext, workerParams) {

        override suspend fun doWork(): Result {
            Log.d("lala", "doWork flag1")

            val url = inputData.getString("DOWNLOAD_URL") ?: return Result.failure()
            Log.d("lala", "doWork flag2")


            try {
                val client = OkHttpClient()
                val request = Request.Builder().url(url).build()
                val response = client.newCall(request).execute()

                Log.d("lala", "doWork flag3")

                if (!response.isSuccessful) return Result.failure()
                Log.d("lala", "doWork flag4")

                val fileName = "${SystemClock.uptimeMillis()}_kent.mp4"
                Log.d("lala", "file name =$fileName")

//                val file = File("$picturesDir/${fileName}_kent.mp4")


                // Save file to disk, update progress, etc.
                // ...
                val body: ResponseBody? = response.body
                Log.d("lala", "doWork flag5")

                body?.let { responseBody ->
                    Log.d("lala", "doWork flag4")
                    val contentLength = responseBody.contentLength()

                    saveFileToGallery(appContext, responseBody.byteStream(), fileName, contentLength)
                    Log.d("lala", "doWork flag5")

                }

                Log.d("lala", "doWork flag flag6")

                return Result.success()
            } catch (e: Exception) {
                Log.d("lala", "doWork flag exception, $e")
                return Result.retry()
            }
        }

        private fun saveFileToGallery(context: Context, inputStream: InputStream, fileName: String, contentLength:Long) {
            val picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
            val appDir = File(picturesDir, "MyAppImages")

            if (!appDir.exists()) {
                appDir.mkdirs()
            }

            val file = File(appDir, fileName)
            var outputStream: FileOutputStream? = null
            var totalBytesRead: Long = 0



            try {
                outputStream = FileOutputStream(file)
                val buffer = ByteArray(8192*10)
                var length: Int
                while (inputStream.read(buffer).also { length = it } != -1) {
                    totalBytesRead += length
                    val progress = (100 * totalBytesRead / contentLength).toInt()
                    Log.d("lala", "progress=$progress")

                    outputStream.write(buffer, 0, length)
                }
                outputStream.flush()

                // Refresh gallery
                MediaScannerConnection.scanFile(context, arrayOf(file.toString()), null, null)
//            } catch (e: IOException) {
//                e.printStackTrace()
            } finally {
                outputStream?.close()
                inputStream.close()
            }
        }
    }
}