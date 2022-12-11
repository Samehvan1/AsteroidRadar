package com.udacity.asteroidradar.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.udacity.asteroidradar.Constants
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query

private val retrofit=Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(Constants.BASE_URL)
    .build()

interface AstroApiService {
    @GET(Constants.allAstrosUrl)
    suspend fun getAllAstros():String
    @GET(Constants.pictOfDayUrl)
    suspend fun getPict(@Query("api_key") key:String=Constants.API_Key):String
}
object AstroApi{
    val retro:AstroApiService by lazy { retrofit.create(AstroApiService::class.java)}
}
