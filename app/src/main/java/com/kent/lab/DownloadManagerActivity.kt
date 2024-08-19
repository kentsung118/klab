package com.kent.lab

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.app.DownloadManager.Query
import android.app.DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import com.kent.lab.databinding.ActivityDownloadBinding


// reference to  https://cloud.tencent.com/developer/article/1598941
class DownloadManagerActivity : BaseBindingActivity<ActivityDownloadBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityDownloadBinding
        get() = ActivityDownloadBinding::inflate


    private val videoUrl = "https://cdn.17app.co/go-prod/clip/2bcKp1KrusUrSiO14qdNAZPTwPK_20240129062031.mp4"

    val TAG = "lala"
    val downloadManager by lazy { getSystemService(DOWNLOAD_SERVICE) as DownloadManager }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.btn2.setOnClickListener {
            shouldQuery = true
            Log.d("lala", "flag1")
            val request: DownloadManager.Request = DownloadManager.Request(
                Uri.parse(videoUrl)
            )
            Log.d("lala", "flag2")

            val now= SystemClock.uptimeMillis()

            request.setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI
            )
                .setNotificationVisibility(VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setAllowedOverRoaming(false) // 缺省是true
                .setTitle("title") // 用于信息查看
                .setDescription("下载 video") // 用于信息查看
                .setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DCIM, "$now.mp4"
                )
            Log.d("lala", "flag3")

            val mDownloadId = downloadManager.enqueue(request) // 加入下载队列
            Log.d("lala", "flag4")

            startQuery(mDownloadId)
        }

    }

    private fun removeDownload(downloadId: Long): Int {
        return downloadManager.remove(downloadId)
    }

    var handler: Handler = Handler(Looper.getMainLooper());

    val step: Long = 500L
    var runnable: QueryRunnable = QueryRunnable()
    var shouldQuery = false

    inner class QueryRunnable : Runnable {
        var DownID: Long = 0

        override fun run() {
            Log.d("lala", "QueryRunnable run flag1")

            queryState(DownID)
            Log.d("lala", "QueryRunnable run flag2")
            if(shouldQuery){
                handler.postDelayed(runnable, step)
            }
        }
    };

    private fun startQuery(downloadId: Long) {
        if (downloadId != 0L) {
            runnable.DownID = downloadId
            handler.postDelayed(runnable, step)
        }
    };

    private fun stopQuery() {
//        handler.removeCallbacks(runnable)
//        handler.removeCallbacksAndMessages(null)
        shouldQuery = false
    }


    @SuppressLint("Range")
    private fun queryState(downID: Long) {
        // 关键：通过ID向下载管理查询下载情况，返回一个cursor
        val c: Cursor = downloadManager.query(Query().setFilterById(downID))
        if (c == null) {
            Toast.makeText(this, "Download not found!", Toast.LENGTH_LONG).show()
        } else { // 以下是从游标中进行信息提取
            if (!c.moveToFirst()) {
                c.close()
                return
            }
            Log.d(TAG, "Column_id : " + c.getLong(c.getColumnIndex(DownloadManager.COLUMN_ID)))
            Log.d(TAG, ("Column_bytes_downloaded so far : " + c.getLong(c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))))
            Log.d(TAG, ("Column last modified timestamp : " + c.getLong(c.getColumnIndex(DownloadManager.COLUMN_LAST_MODIFIED_TIMESTAMP))))
            Log.d(TAG, ("Column local uri : " + c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI))))
            Log.d(TAG, ("Column statue : " + c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS))))
            Log.d(TAG, ("Column reason : " + c.getInt(c.getColumnIndex(DownloadManager.COLUMN_REASON))))
            val st = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS))
            Toast.makeText(this, statusMessage(st), Toast.LENGTH_LONG).show()
            //Log.i(TAG, statusMessage(st));
            c.close()

            if(st == 8  || st == 16){
                Log.d("lala", "stopQuery")
                stopQuery()
            }
        }
    }

    private fun statusMessage(st: Int): String {
        when (st) {
            DownloadManager.STATUS_FAILED -> return "Download failed"
            DownloadManager.STATUS_PAUSED -> return "Download paused"
            DownloadManager.STATUS_PENDING -> return "Download pending"
            DownloadManager.STATUS_RUNNING -> return "Download in progress!"
            DownloadManager.STATUS_SUCCESSFUL -> return "Download finished"
            else -> return "Unknown Information"
        }
    }

    // 监听下载结束，启用BroadcastReceiver
    var receiver: BroadcastReceiver = object : BroadcastReceiver() {

        @SuppressLint("Range")
        override fun onReceive(context: Context, intent: Intent) {
            val dm = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            val action = intent.action
            if ((DownloadManager.ACTION_DOWNLOAD_COMPLETE == action)) {
                val downloadId = intent.getLongExtra(
                    DownloadManager.EXTRA_DOWNLOAD_ID, 0
                )
                // 查询
                val query = Query()
                query.setFilterById(downloadId)
                val c = dm.query(query)
                if (c.moveToFirst()) {
                    val columnIndex = c
                        .getColumnIndex(DownloadManager.COLUMN_STATUS)
                    if (DownloadManager.STATUS_SUCCESSFUL == c
                            .getInt(columnIndex)
                    ) {
                        val uriString = c
                            .getString(
                                c
                                    .getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)
                            )


                        //removeDownload(downloadId);
//                        Toast.makeText(
//                            DownloadActivity.this,
//                            "get file complete: $uriString", Toast.LENGTH_LONG
//                        ).show()
                        // Uri.parse(uriString);
                    }
                } // endif
            } // endif
        } // onReceive
    } // end class receiver

    // 监听下载结束，启用BroadcastReceiver
    var receiver2: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val downloadId = intent.getLongExtra(
                DownloadManager.EXTRA_DOWNLOAD_ID, 0
            )

            val action = intent.action
            if ((DownloadManager.ACTION_NOTIFICATION_CLICKED == action)) {
//                lookDownload()
            }
        }
    } //
}