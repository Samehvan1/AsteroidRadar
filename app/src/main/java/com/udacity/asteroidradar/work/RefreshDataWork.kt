package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.getInstance
import com.udacity.asteroidradar.repository.AstroRepo
import retrofit2.HttpException

class RefreshDataWork (appcontext : Context,params:WorkerParameters):CoroutineWorker(appcontext,params){
    companion object{
       const val Work_Name="Refreshdatawork"
    }
    override suspend fun doWork(): Result {
        val database= getInstance(applicationContext)
        val repo=AstroRepo(database)
        return try {
            repo.refreshAsteroidList()
            repo.getPictureOfTheDate()
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }
}