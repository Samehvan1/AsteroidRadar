package com.udacity.asteroidradar.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.squareup.moshi.Moshi
import com.udacity.asteroidradar.*
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.network.AstroApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AstroRepo(private val database: AsteroidDatabase) {

    val asteroidList: LiveData<List<Asteroid>>
        get() = database.astroDao.getAll()

    val todayAsteroidList: LiveData<List<Asteroid>>
        get() = database.astroDao.getTodayAsteroid(Constants.getCurrentDate())

    val pictureOfDay: LiveData<PictureOfDay>
        get() = database.pictDao.get()


    suspend fun refreshAsteroidList() {
        withContext(Dispatchers.IO) {
            try {
                val asteroid = AstroApi.retro.getAllAstros()
                val json = JSONObject(asteroid)
                val data = parseAsteroidsJsonResult(json)
                database.astroDao.updateData(data)
                Log.e("astrorepo line 34", "")

            } catch (e: Exception) {
                Log.e("astrorepo line 36", "")
            }
        }
    }

    suspend fun getPictureOfTheDate() {
        withContext(Dispatchers.IO) {
            try {
                val response = AstroApi.retro.getPict(Constants.API_Key)
                val pic = Moshi.Builder()
                    .build()
                    .adapter(PictureOfDay::class.java)
                    .fromJson(response)
                    ?: PictureOfDay(-1, "image", "", "")
                database.pictDao.updateData(pic)
                Log.d("astrorepo line 55", "")

            } catch (e: Exception) {
                Log.d("astrorepo line 58", "")

            }
        }
    }
}