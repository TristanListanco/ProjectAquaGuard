package com.aquaguardian.projectaquaguard.core.data

import com.google.gson.annotations.SerializedName
data class WeatherResponse(
    @SerializedName("queryCost") val queryCost: Int,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("resolvedAddress") val resolvedAddress: String,
    @SerializedName("address") val address: String,
    @SerializedName("timezone") val timezone: String,
    @SerializedName("tzoffset") val tzoffset: Int,
    @SerializedName("days") val days: List<Day>,
    @SerializedName("stations") val stations: Map<String, Station>
)

data class Day(
    @SerializedName("datetime") val datetime: String,
    @SerializedName("datetimeEpoch") val datetimeEpoch: Long,
    @SerializedName("tempmax") val tempmax: Double,
    @SerializedName("tempmin") val tempmin: Double,
    @SerializedName("temp") val temp: Double,
    @SerializedName("feelslikemax") val feelslikemax: Double,
    @SerializedName("feelslikemin") val feelslikemin: Double,
    @SerializedName("feelslike") val feelslike: Double,
    @SerializedName("dew") val dew: Double,
    @SerializedName("humidity") val humidity: Double,
    @SerializedName("precip") val precip: Double,
    @SerializedName("precipprob") val precipprob: Double,
    @SerializedName("precipcover") val precipcover: Double,
    @SerializedName("preciptype") val preciptype: List<String>?,
    @SerializedName("snow") val snow: Double,
    @SerializedName("snowdepth") val snowdepth: Double,
    @SerializedName("windgust") val windgust: Double,
    @SerializedName("windspeed") val windspeed: Double,
    @SerializedName("winddir") val winddir: Double,
    @SerializedName("pressure") val pressure: Double,
    @SerializedName("cloudcover") val cloudcover: Double,
    @SerializedName("visibility") val visibility: Double,
    @SerializedName("solarradiation") val solarradiation: Double,
    @SerializedName("solarenergy") val solarenergy: Double,
    @SerializedName("uvindex") val uvindex: Int,
    @SerializedName("severerisk") val severerisk: Int,
    @SerializedName("sunrise") val sunrise: String,
    @SerializedName("sunriseEpoch") val sunriseEpoch: Long,
    @SerializedName("sunset") val sunset: String,
    @SerializedName("sunsetEpoch") val sunsetEpoch: Long,
    @SerializedName("moonphase") val moonphase: Double,
    @SerializedName("conditions") val conditions: String,
    @SerializedName("description") val description: String,
    @SerializedName("icon") val icon: String,
    @SerializedName("stations") val stations: List<String>?,
    @SerializedName("source") val source: String
)

data class Station(
    @SerializedName("distance") val distance: Int,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("useCount") val useCount: Int,
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("quality") val quality: Int,
    @SerializedName("contribution") val contribution: Int
)
