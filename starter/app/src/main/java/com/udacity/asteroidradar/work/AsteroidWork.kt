package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.database.RepositoryAsteroid
import com.udacity.asteroidradar.database.getDatabase
import kotlinx.coroutines.Dispatchers
import retrofit2.HttpException

class AsteroidWork(appContext: Context, params: WorkerParameters):
        CoroutineWorker(appContext, params) {

        companion object {
            const val WORK_NAME = "AsteroidWork"
        }

    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val repository = RepositoryAsteroid(database)
        return try {
            repository.fetchAsteroids(applicationContext.getString(R.string.api_key))
            repository.fetchPictureOfDay(applicationContext.getString(R.string.api_key))
            repository.deleteOldAsteroids(Dispatchers.Default)
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }
}