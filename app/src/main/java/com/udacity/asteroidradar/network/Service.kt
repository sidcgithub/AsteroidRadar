package com.udacity.asteroidradar.network

import androidx.lifecycle.LiveData
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.Constants
import kotlinx.coroutines.Deferred
import okhttp3.Response
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.text.SimpleDateFormat
import java.util.*

interface AsteroidService {
    companion object Days {
        var calendar = Calendar.getInstance()
        val currentTime = calendar.time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        val today = dateFormat.format(currentTime)
        val x = calendar.add(Calendar.DAY_OF_YEAR, 7)
        val sevendays = dateFormat.format(calendar.time)
    }

    @GET("neo/rest/v1/feed")
    fun getAsteroidData(
        @Query("start_date") startDate: String = today,
        @Query("end_date") endDate: String = sevendays,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY
    ): Deferred<ResponseBody>

    @GET("planetary/apod")
    fun picOfTheDay(@Query("api_key") apiKey: String = BuildConfig.API_KEY): Deferred<NetworkPicOfTheDay>
}

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

const val BASE_URL = "https://api.nasa.gov/"

object Network {
    // Configure retrofit to parse JSON and use coroutines
    private val retrofit = Retrofit.Builder()
        .baseUrl(
            BASE_URL
        )
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val asteroidData = retrofit.create(AsteroidService::class.java)
}
