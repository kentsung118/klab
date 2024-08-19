package com.kent.lab

import android.content.Context
import android.media.MediaScannerConnection
import android.os.Bundle
import android.os.Environment
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import androidx.lifecycle.Observer
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.kent.lab.databinding.ActivityWorkBinding
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream


class WorkManagerActivity : BaseBindingActivity<ActivityWorkBinding>() {

    override val bindingInflater: (LayoutInflater) -> ActivityWorkBinding
        get() = ActivityWorkBinding::inflate


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btn2.setOnClickListener {
            val picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
            ///storage/emulated/0/DCIM/75143711_kent.mp4

            val filePath = "$picturesDir/75143711_kent.mp4"
            val file = File(filePath)
            Log.d("lala", "file path=${file.absolutePath}")
            Log.d("lala", "file exists=${file.exists()}")
            Log.d("lala", "file sizw=${file.length()}")
        }

        binding.btn3.setOnClickListener {
            val picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
            val filePath = "$picturesDir/75143711_kent.mp4"
            val file = File(filePath)
            file.delete()
        }

        binding.btn1.setOnClickListener {
            Log.d("lala", "work request flag1")
            val videoUrl = "https://cdn.17app.co/go-prod/clip/2bcKp1KrusUrSiO14qdNAZPTwPK_20240129062031.mp4"

            val workTag = "testTag"
            val downloadRequest = OneTimeWorkRequestBuilder<DownloadWorker>()
                .setInputData(workDataOf("DOWNLOAD_URL" to videoUrl))
                .addTag(workTag)
                .build()

            val workManager = WorkManager.getInstance(this)
            workManager.enqueue(downloadRequest)
//            workManager.getWorkInfosByTag(workTag).

            //從 Main thread 監聽 進度
            workManager.getWorkInfoByIdLiveData(downloadRequest.id).observe(this, object : Observer<WorkInfo?> {
                //获取WorkInfo对象，实时监测任务的状态
                override fun onChanged(workInfo: WorkInfo?) {
                    workInfo?.let {
                        when (it.state) {
                            WorkInfo.State.RUNNING -> {
                                Log.d("lala", "当前进度 = " + it.progress.getInt("Progress", -1));
                            }
                            WorkInfo.State.SUCCEEDED -> {
                                Log.d("lala", "workInfo success value=$workInfo")
                            }
                            else -> {}
                        }
                    }
                }
            })
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

        private fun saveFileToGallery(context: Context, inputStream: InputStream, fileName: String, contentLength: Long) {
            val picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)

            val file = File(picturesDir, fileName)
            var outputStream: FileOutputStream? = null
            var totalBytesRead: Long = 0

            var mProgress = 0

            try {
                outputStream = FileOutputStream(file)
                val buffer = ByteArray(8192 )
                var length: Int
                while (inputStream.read(buffer).also { length = it } != -1) {
                    totalBytesRead += length
                    val progress = (100 * totalBytesRead / contentLength).toInt()
                    if(progress >mProgress ){
                        mProgress = progress
                        val data: Data = Data.Builder().putInt("Progress", progress).build()
                        setProgressAsync(data);
                    }
                    Log.d("lala", "progress=$progress")

                    outputStream.write(buffer, 0, length)
                }
                outputStream.flush()

                // Refresh gallery
                MediaScannerConnection.scanFile(context, arrayOf(file.toString()), null, null)
            } finally {
                outputStream?.close()
                inputStream.close()
            }
        }
    }
}