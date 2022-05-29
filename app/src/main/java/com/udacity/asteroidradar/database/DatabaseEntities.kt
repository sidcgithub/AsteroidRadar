package com.udacity.asteroidradar.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.api.convertToString
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.PicOfTheDay
import java.util.*

@Entity
data class DatabaseAsteroid(
    @PrimaryKey val id: Long,
    val codename: String,
    @ColumnInfo(name = "close_approach_date") val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean
)

fun List<DatabaseAsteroid>.toDomainModel(): List<Asteroid> {
    return map { dbAsteroid ->
        Asteroid(
            id = dbAsteroid.id,
            codename = dbAsteroid.codename,
            closeApproachDate = dbAsteroid.closeApproachDate,
            absoluteMagnitude = dbAsteroid.absoluteMagnitude,
            estimatedDiameter = dbAsteroid.estimatedDiameter,
            relativeVelocity = dbAsteroid.relativeVelocity,
            distanceFromEarth = dbAsteroid.distanceFromEarth,
            isPotentiallyHazardous = dbAsteroid.isPotentiallyHazardous
        )
    }
}

@Entity
data class DatabasePicOfTheDay(
    @PrimaryKey val id: Int = 0,
    val url: String,
    val mediaType: String,
    val title: String
)

fun DatabasePicOfTheDay.toDomainModel(): PicOfTheDay {
    return PicOfTheDay(
        url = this.url,
        mediaType = this.mediaType,
        title = this.title
    )
}