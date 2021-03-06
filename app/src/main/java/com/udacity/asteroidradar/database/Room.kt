package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AsteroidDao {
    @Query("select * from databaseasteroid  where DATETIME(close_approach_date) <= DATETIME('now', cast(:days as text) ||' day') and DATETIME(close_approach_date) >= DATETIME('now') order by DATE(close_approach_date)")
    fun getAsteroids(days: Int = 365): Flow<List<DatabaseAsteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids: DatabaseAsteroid)
}

@Database(entities = [DatabaseAsteroid::class, DatabasePicOfTheDay::class], version = 1)
abstract class AsteroidDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
    abstract val picOfTheDayDao: PicOfTheDayDao
}

private lateinit var INSTANCE: AsteroidDatabase

fun getDatabase(context: Context): AsteroidDatabase {
    synchronized(AsteroidDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                AsteroidDatabase::class.java,
                "asteroids"
            ).build()
        }
    }
    return INSTANCE
}

@Dao
interface PicOfTheDayDao {
    @Query("select * from databasepicoftheday")
    fun getPicData(): LiveData<DatabasePicOfTheDay>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(pic: DatabasePicOfTheDay)
}

