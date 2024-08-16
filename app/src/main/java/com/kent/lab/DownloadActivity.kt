package com.kent.lab

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.work.WorkManager
import com.kent.lab.databinding.ActivityDownloadBinding
import com.kent.lab.databinding.ActivityMainBinding

class DownloadActivity : BaseBindingActivity<ActivityDownloadBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityDownloadBinding
        get() = ActivityDownloadBinding::inflate


    private val url = "https://cdn.17app.co/go-prod/clip/2bcKp1KrusUrSiO14qdNAZPTwPK_20240129062031.mp4"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding.btn1.setOnClickListener {
//            val continuation = WorkManager.getInstance(this)
//                .beginUniqueWork(
//                    Constants.IMAGE_MANIPULATION_WORK_NAME,
//                    ExistingWorkPolicy.REPLACE,
//                    OneTimeWorkRequest.from(CleanupWorker::class.java)
//                ).then(OneTimeWorkRequest.from(WaterColorFilterWorker::class.java))
//                .then(OneTimeWorkRequest.from(GrayScaleFilterWorker::class.java))
//                .then(OneTimeWorkRequest.from(BlurEffectFilterWorker::class.java))
//                .then(
//                    if (save) {
//                        workRequest<SaveImageToGalleryWorker>(tag = Constants.TAG_OUTPUT)
//                    } else /* upload */ {
//                        workRequest<UploadWorker>(tag = Constants.TAG_OUTPUT)
//                    }
//                )

        }

        binding.btn2.setOnClickListener {

        }

    }
}