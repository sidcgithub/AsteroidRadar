package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.PicOfTheDayDao
import com.udacity.asteroidradar.database.toDomainModel
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.PicOfTheDay
import com.udacity.asteroidradar.network.Network
import com.udacity.asteroidradar.network.toDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class AsteroidsRepository(private val database: AsteroidDatabase) {

    fun setAsteroidPeriod(days: Int = 365) = database.asteroidDao.getAsteroids(days).map { it.toDomainModel() }
    val picOfTheDay: LiveData<PicOfTheDay> =
        Transformations.map(database.picOfTheDayDao.getPicData()) {
            it?.toDomainModel()
        }


    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            val asteroids = Network.asteroidData.getAsteroidData().await()
            database.asteroidDao.insertAll(*asteroids.toDatabaseModel())
            val picData = Network.asteroidData.picOfTheDay().await()
            database.picOfTheDayDao.insert(picData.toDatabaseModel())

        }
    }
}