package com.hyejeanmoon.workmanagerdemo

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.work.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var txtId: TextView

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txtId = findViewById(R.id.txtId)

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .setRequiresStorageNotLow(true)
            .setRequiresCharging(true)
            .build()

        val inputData = Data.Builder().putString("data", "MainActivity").build()

        val oneTimeWorkRequest =
            OneTimeWorkRequestBuilder<CustomWorker>().setConstraints(constraints)
                .setBackoffCriteria(
                    BackoffPolicy.LINEAR,
                    OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
                    TimeUnit.MILLISECONDS
                )
                .setInputData(inputData)
                .build()

        val periodicWorkRequest =
            PeriodicWorkRequestBuilder<CustomWorker>(1000, TimeUnit.MILLISECONDS).setConstraints(
                constraints
            ).setBackoffCriteria(
                BackoffPolicy.LINEAR,
                PeriodicWorkRequest.MIN_BACKOFF_MILLIS,
                TimeUnit.MILLISECONDS
            ).build()

        var count = 0

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(oneTimeWorkRequest.id).observe(this,
            Observer {
                if (it.state == WorkInfo.State.RUNNING) {
                    count++
                    txtId.text = "Work status is changed! $count"
                    Toast.makeText(this, "Work status is changed!", Toast.LENGTH_LONG).show()
                }
            })

        WorkManager.getInstance(this).enqueue(oneTimeWorkRequest)
    }
}
