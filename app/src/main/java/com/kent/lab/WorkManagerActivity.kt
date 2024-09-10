package com.kent.lab

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.FileProvider
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
import java.util.UUID


class WorkManagerActivity : BaseBindingActivity<ActivityWorkBinding>() {

    override val bindingInflater: (LayoutInflater) -> ActivityWorkBinding
        get() = ActivityWorkBinding::inflate

    val workManager = WorkManager.getInstance(this)
    var workId : UUID? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.btn5.setOnClickListener {
            workId?.let {
                Log.d("lala", " workManager.cancelWorkById($it)")
                workManager.cancelWorkById(it)
            }
        }

        binding.btn4.setOnClickListener {
            shareToIG()
        }

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
//            val videoUrl = "https://cdn.17app.co/go-prod/clip/2bcKp1KrusUrSiO14qdNAZPTwPK_20240129062031.mp4"
            val videoUrl = "https://cdn.17app.co/go-prod/clip/2lBQqE4v3MWFUMRCxXnED20W6fI_20240826054810.mp4"

            val workTag = "testTag"
            val downloadRequest = OneTimeWorkRequestBuilder<DownloadWorker>()
                .setInputData(workDataOf("DOWNLOAD_URL" to videoUrl))
                .addTag(workTag)
//                .setExpedited()
                .build()
            workId = downloadRequest.id
            Log.d("lala", "workId=$workId")

//            workManager.cancelWorkById()

            workManager.enqueue(downloadRequest)
//            workManager.getWorkInfosByTagLiveData(workTag).observe(this, object :Observer<List<WorkInfo>>{
//                override fun onChanged(value: List<WorkInfo>) {
//                    Log.d("lala", "value size=${value.size}");
//                    value[0].let {
//                        when (it.state) {
//                            WorkInfo.State.RUNNING -> {
//                                Log.d("lala", "当前进度 = " + it.progress.getInt("Progress", -1));
//                            }
//                            WorkInfo.State.SUCCEEDED -> {
//                                Log.d("lala", "workInfo success value=$it")
//                            }
//                            else -> {}
//                        }
//                    }
//                }
//            } )

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
            Log.d("lala", "doWork start")
            val url = inputData.getString("DOWNLOAD_URL") ?: return Result.failure()
            try {
                val client = OkHttpClient()
                val request = Request.Builder().url(url).build()
                val response = client.newCall(request).execute()
                if (!response.isSuccessful) return Result.failure()
                val fileName = "${SystemClock.uptimeMillis()}_kent.mp4"
                Log.d("lala", "file name =$fileName")
                // Save file to disk, update progress, etc.
                // ...
                val body: ResponseBody? = response.body
                body?.let { responseBody ->
                    val contentLength = responseBody.contentLength()
                    saveFileToGallery(appContext, responseBody.byteStream(), fileName, contentLength)
                }
                Log.d("lala", "doWork success")
                sendNotification(appContext)
                return Result.success()
            } catch (e: Exception) {
                Log.d("lala", "doWork flag exception, $e")
                return Result.retry()
            }
        }

        private fun saveFileToGallery(context: Context, inputStream: InputStream, fileName: String, contentLength: Long) {
            val picturesDir = "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)}/media17"

            val dir = File(picturesDir)
            if(!dir.exists()){
                dir.mkdir()
            }

            val file = File(picturesDir, fileName)
            var outputStream: FileOutputStream? = null
            var totalBytesRead: Long = 0

            var mProgress = 0

            try {
                outputStream = FileOutputStream(file)
                val buffer = ByteArray(8192 )
                var length: Int
                while (inputStream.read(buffer).also { length = it } != -1) {
                    if(isStopped){
                        return
                    }
                    totalBytesRead += length
                    val progress = (100 * totalBytesRead / contentLength).toInt()
                    if(progress >mProgress ){
                        mProgress = progress
                        val data: Data = Data.Builder().putInt("Progress", progress).build()
                        setProgressAsync(data);
                        Log.d("lala", "progress=$progress")
                    }

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

        fun sendNotification(context: Context) {
            val channelId = "my_channel_id"
            val channelName = "My Channel"
            val notificationId = 1

            // 创建通知频道（仅在 API 26+ 必须）
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(channelId, channelName, importance).apply {
                    description = "Channel description"
                }

                // 注册频道到系统
                val notificationManager: NotificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }

            // 创建通知的 PendingIntent（点击通知时的动作）
//            val intent = Intent(context, MyActivity::class.java).apply {
//                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            }
//            val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

            // 创建通知
            val builder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_launcher_background) // 必须的: 设置小图标
                .setContentTitle("My notification")         // 标题
                .setContentText("Hello World!")             // 内容
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .setContentIntent(pendingIntent)            // 设置 PendingIntent
                .setAutoCancel(true)                        // 点击后自动取消通知

            // 发布通知
            with(NotificationManagerCompat.from(context)) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return
                }
                notify(notificationId, builder.build())
            }
        }
    }

    //https://developers.facebook.com/docs/instagram-platform/sharing-to-stories
    fun shareToIG (){
        Log.d("lala", "packageName=$packageName")

        // 1. 准备要分享的媒体文件的 URI
        val videoFile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "9423593_kent.mp4")
        Log.d("lala", "videoFile=$videoFile")
        Log.d("lala", "videoFile.size=${videoFile.length()}")


        val videoUri: Uri = FileProvider.getUriForFile(
            this,
            "$packageName.fileprovider", // 替换为你应用的 fileprovider 的 authority
            videoFile
        )

        // 2. 创建并设置 Intent
        val intent = Intent("com.instagram.share.ADD_TO_STORY").apply {
            setDataAndType(videoUri, "video/mp4") // 设置数据类型为视频
            putExtra("source_application", packageName) // 传递来源应用包名
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION // 授予 Instagram 读取 URI 的权限
        }

        grantUriPermission(
            "com.instagram.android", videoUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);

        // 3. 检查 Instagram 是否已安装并启动 Intent
//        if (intent.resolveActivity(packageManager) != null) {
            Log.d("lala", "share to IG")
            startActivity(intent)
//        } else {
//            Log.d("lala", "提示用户安装 Instagram或者提示错误信息")
//            // 提示用户安装 Instagram
//            // 或者提示错误信息
//        }
    }
}