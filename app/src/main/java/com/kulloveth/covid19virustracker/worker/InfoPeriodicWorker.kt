package com.kulloveth.covid19virustracker.worker

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.kulloveth.covid19virustracker.R
import com.kulloveth.covid19virustracker.data.db.CountryInfoEntity
import com.kulloveth.covid19virustracker.model.*
import com.kulloveth.covid19virustracker.api.StatusApiService
import com.kulloveth.covid19virustracker.data.db.StatusDao
import com.kulloveth.covid19virustracker.data.db.StatusEntity
import com.kulloveth.covid19virustracker.model.Failure
import com.kulloveth.covid19virustracker.model.Success
import org.koin.core.KoinComponent
import org.koin.core.inject

class InfoPeriodicWorker(val context: Context, workerParameters: WorkerParameters) : Worker(context, workerParameters){

    private val TAG = InfoPeriodicWorker::class.java.simpleName
    override fun doWork(): Result {

        makeInfoNotification("Remember to Stay Safe")
        return Result.success()
    }

}