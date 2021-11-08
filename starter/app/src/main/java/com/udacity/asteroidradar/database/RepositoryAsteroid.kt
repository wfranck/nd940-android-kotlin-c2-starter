package com.udacity.asteroidradar.database

import com.udacity.asteroidradar.api.NasaData
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.utils.DataState
import com.udacity.asteroidradar.utils.Utils
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import org.json.JSONObject

class RepositoryAsteroid(private val databaseAsteroid: AsteroidDatabase) {

    val pictureOfDay = databaseAsteroid.pictureOfTheDayDao.getPictureOfDay()
    val asteroids = databaseAsteroid.asteroidDao.getAsteroids()
    val asteroidsUntil = databaseAsteroid.asteroidDao.getAsteroidsUntil(Utils.getEndOfWeek())
    val asteroidsToday = databaseAsteroid.asteroidDao.getAsteroidsToday(Utils.getTodayFormatted())

    suspend fun fetchPictureOfDay(apiKey: String, dispatcher: CoroutineDispatcher = Dispatchers.IO) {
        withContext(dispatcher) {
            try {
                val result = NasaData.asteroids.getPictureOfDay("planetary/apod?api_key=$apiKey").await()
                if ( result.mediaType == "image" ) {
                    databaseAsteroid.pictureOfTheDayDao.insertPictureOfTheDay(result.asDatabaseModel())
                }
            } catch (throwable: Throwable) {}
        }
    }

    suspend fun deleteOldAsteroids(dispatcher: CoroutineDispatcher = Dispatchers.IO) {
        withContext(dispatcher) {
            databaseAsteroid.asteroidDao.deleteOlds(Utils.getTodayFormatted())
        }
    }

    @ExperimentalCoroutinesApi
    suspend fun refreshAsteroids(apiKey: String, dispatcher: CoroutineDispatcher = Dispatchers.IO): Flow<DataState> {
        return flow {
            emit(DataState.Loading)
            withContext(dispatcher) {
                emit(fetchAsteroids(apiKey))
            }
        }.flowOn(dispatcher)
    }

    suspend fun fetchAsteroids(apiKey: String): DataState {
        return try {
            val result = NasaData.asteroids.getAsteroids(
                "neo/rest/v1/feed?"+
                        "start_date=${Utils.getTodayFormatted()}" +
                        "&end_date=${Utils.getAWeek()}&api_key=$apiKey").await()
            val jsonObject = JSONObject(result)
            val resultParsed = parseAsteroidsJsonResult(jsonObject)
            databaseAsteroid.asteroidDao.insertAsteroids(resultParsed.asDatabaseModel())
            DataState.Success
        } catch (throwable: Throwable) {
            DataState.Error
        }
    }
}