package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.database.DatabaseAsteroid
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

enum class Period(days: Int = Int.MAX_VALUE) {
    DAY(1),
    WEEK(7),
    ALL
}

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database = getDatabase(application)
    private val repository = AsteroidsRepository(database)

    init {
        viewModelScope.launch {
            try {
                repository.refreshAsteroids()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun setAsteroidPeriod(period: Period): Flow<List<Asteroid>> {
        return when (period) {
            Period.DAY -> repository.setAsteroidPeriod(1)
            Period.WEEK -> repository.setAsteroidPeriod(7)
            Period.ALL -> repository.setAsteroidPeriod()
        }
    }


    val picOfTheDay = repository.picOfTheDay

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}