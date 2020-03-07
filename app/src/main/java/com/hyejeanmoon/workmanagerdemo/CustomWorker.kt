package com.hyejeanmoon.workmanagerdemo

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class CustomWorker(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {
    override fun doWork(): Result {
        Log.d("CustomWorker", "Worker is active in " + inputData.getString("data"))
        return Result.success()
    }
}