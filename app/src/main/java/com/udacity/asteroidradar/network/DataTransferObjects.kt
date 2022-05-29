package com.udacity.asteroidradar.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.api.parseDatabaseAsteroidsJsonResult
import com.udacity.asteroidradar.database.DatabaseAsteroid
import com.udacity.asteroidradar.database.DatabasePicOfTheDay
import com.udacity.asteroidradar.database.toDomainModel
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.PicOfTheDay
import okhttp3.Response
import okhttp3.ResponseBody
import org.json.JSONObject

fun ResponseBody.toDomainModel(): List<Asteroid> {
    return parseAsteroidsJsonResult(JSONObject(this.string()))
}

fun ResponseBody.toDatabaseModel(): Array<DatabaseAsteroid> {
    return parseDatabaseAsteroidsJsonResult(JSONObject(this.string())).toTypedArray()
}

fun NetworkPicOfTheDay.toDatabaseModel(): DatabasePicOfTheDay {
    return DatabasePicOfTheDay(
        url = this.url,
        mediaType = this.mediaType,
        title = this.title
    )
}

@JsonClass(generateAdapter = true)
data class NetworkPicOfTheDay(
    val url: String,
    @Json(name = "media_type") val mediaType: String,
    val title: String
)
