package com.udacity.asteroidradar

import java.text.SimpleDateFormat
import java.util.*

object Constants {
    const val API_QUERY_DATE_FORMAT = "yyyy-MM-dd"
    const val DEFAULT_END_DATE_DAYS = 7
    const val BASE_URL = "https://api.nasa.gov/"
    const val pictOfDayUrl="planetary/apod"
    const val allAstrosUrl="neo/rest/v1/feed"
    const val API_Key = "0wHEqV3HlMu7RJya6sIBhXiBhf8anWh9k4UUZ8Cg"
    fun getCurrentDate(): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val date = Date()
        return formatter.format(date)
    }
}