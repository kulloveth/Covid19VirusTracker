package com.kulloveth.covid19virustracker.data

import androidx.work.*
import com.kulloveth.covid19virustracker.App
import com.kulloveth.covid19virustracker.data.db.NewsDao
import com.kulloveth.covid19virustracker.data.db.StatusDao
import com.kulloveth.covid19virustracker.worker.InfoPeriodicWorker
import com.kulloveth.covid19virustracker.worker.NewsPeriodicWorker
import com.kulloveth.covid19virustracker.worker.StatusPeriodicWorker
import java.util.concurrent.TimeUnit

private const val TAG_SYNC_DATA = "TAG_SYNC_DATA"
private const val NEWS_TAG_SYNC_DATA = "NEWS_SYNC_DATA"
private const val INFO_SYNC_DATA = "INFO_SYNC_DATA"

open class Repository(private val statusDao: StatusDao,private val newsDao: NewsDao){
    // Create Network constraint
    val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .setRequiresBatteryNotLow(true)
        .build()
    val workManager = WorkManager.getInstance(App.getContext())

    //build and enqeue status work
    fun fetchStatus() {
        val periodicSyncDataWork =
            PeriodicWorkRequest.Builder(StatusPeriodicWorker::class.java, 1, TimeUnit.HOURS)
                .addTag(TAG_SYNC_DATA)
                .setConstraints(constraints)
                // setting a backoff on case the work needs to retry
                .setBackoffCriteria(BackoffPolicy.LINEAR, PeriodicWorkRequest.MIN_BACKOFF_MILLIS, TimeUnit.MILLISECONDS)
                .build()
        workManager.enqueueUniquePeriodicWork("Status", ExistingPeriodicWorkPolicy.KEEP,  //Existing Periodic Work policy
            periodicSyncDataWork //work request
        )
    }


    //build and engueue news work
    fun fetchNews() {
        val periodicSynDataWork =
            PeriodicWorkRequest.Builder(NewsPeriodicWorker::class.java, 15, TimeUnit.MINUTES)
                .addTag(NEWS_TAG_SYNC_DATA)
                .setConstraints(constraints)
                // setting a backoff on case the work needs to retry
                .setBackoffCriteria(
                    BackoffPolicy.LINEAR,
                    PeriodicWorkRequest.MIN_BACKOFF_MILLIS,
                    TimeUnit.MILLISECONDS
                )
                .build()
        workManager.enqueueUniquePeriodicWork(
           "News",
            ExistingPeriodicWorkPolicy.KEEP,  //Existing Periodic Work policy
            periodicSynDataWork //work request
        )
    }

    fun infoNotify() {
        val periodicSyncDataWork =
            PeriodicWorkRequest.Builder(InfoPeriodicWorker::class.java, 15, TimeUnit.MINUTES)
                .addTag(INFO_SYNC_DATA)
                .setConstraints(constraints)
                // setting a backoff on case the work needs to retry
                .setBackoffCriteria(
                    BackoffPolicy.LINEAR,
                    PeriodicWorkRequest.MIN_BACKOFF_MILLIS,
                    TimeUnit.MILLISECONDS
                )
                .build()
        workManager.enqueueUniquePeriodicWork(
            "INFO", ExistingPeriodicWorkPolicy.KEEP,  //Existing Periodic Work policy
            periodicSyncDataWork //work request
        )
    }

    suspend fun fetchStatusFromRoom()=
        statusDao.getNewStatus()

    suspend fun fetchNewsFromRoom()=
        newsDao.getNewCovidNews()

}