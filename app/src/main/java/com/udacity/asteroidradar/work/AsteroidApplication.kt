package com.udacity.asteroidradar.work

import android.app.Application
import android.os.Build
import androidx.work.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AsteroidApplication : Application() {
    private val applicationScope = CoroutineScope(Dispatchers.Default)
    override fun onCreate() {
        super.onCreate()
        initMe()
    }

    private fun initMe() {
        applicationScope.launch { setupDailyWorker() }
    }

    private fun setupDailyWorker() {
        val constrains = Constraints.Builder().setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(true)
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    setRequiresDeviceIdle(true)
                }
            }.build()
        val repeatRequest = PeriodicWorkRequestBuilder<RefreshDataWork>(
            1, TimeUnit.DAYS
        ).setConstraints(constrains).build()
        WorkManager.getInstance().enqueueUniquePeriodicWork(
            RefreshDataWork.Work_Name, ExistingPeriodicWorkPolicy.KEEP, repeatRequest
        )
    }

}