package com.aquaguardian.projectaquaguard.core

import com.aquaguardian.projectaquaguard.core.data.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherService {
    @GET("{city}")
    fun getWeatherData(
        @Path("city") cityLocation: String?,
        @Query("unitGroup") unitGroup: String?,
        @Query("metric") metric: String?,
        @Query("key") apiKey: String?,
        @Query("contentType") contentType: String?,
    ): Call<WeatherResponse?>?
}