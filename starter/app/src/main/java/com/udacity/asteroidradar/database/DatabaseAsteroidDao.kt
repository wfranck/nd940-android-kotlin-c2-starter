package com.udacity.asteroidradar.database

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Database(entities = [  TableAsteroids::class, TablePictureOfDay::class], version = 1)
abstract class AsteroidDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
    abstract val pictureOfTheDayDao: PictureOfTheDayDao

}
@Volatile
private lateinit var INSTANCE: AsteroidDatabase

fun getDatabase(context: Context): AsteroidDatabase {
    synchronized(AsteroidDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
            AsteroidDatabase::class.java,
            "asteroids").build()
        }
    }
    return INSTANCE
}


@Dao
interface AsteroidDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAsteroids(asteroids: List<TableAsteroids>)

    @Query("SELECT * FROM asteroids ORDER BY date(closeApproachDate) DESC")
    fun getAsteroids(): Flow<List<TableAsteroids>>

    @Query("SELECT * FROM asteroids WHERE closeApproachDate <= :untilDate ORDER BY date(closeApproachDate) DESC")
    fun getAsteroidsUntil(untilDate: String): Flow<List<TableAsteroids>>

    @Query("SELECT * FROM asteroids WHERE closeApproachDate LIKE :today")
    fun getAsteroidsToday(today: String): Flow<List<TableAsteroids>>

    @Query("DELETE FROM asteroids WHERE closeApproachDate < date(:today)")
    fun deleteOlds(today: String)
}

@Dao
interface PictureOfTheDayDao {
    @Query("SELECT * FROM pic_of_day")
    fun getPictureOfDay(): Flow<TablePictureOfDay>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPictureOfTheDay(pictureOfDay: TablePictureOfDay)
}
