package com.aquaguardian.projectaquaguard.core.vms

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aquaguardian.projectaquaguard.core.BuildConfig.Companion.WEATHER_API_BASE_URL
import com.aquaguardian.projectaquaguard.core.WeatherService
import com.aquaguardian.projectaquaguard.core.data.WeatherResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainVMService : ViewModel() {

    private val _weatherData = MutableStateFlow<WeatherResponse?>(null)
    val weatherData: StateFlow<WeatherResponse?> get() = _weatherData

    fun clearWeatherData(){
        _weatherData.value = null
    }

    private val _isWeatherServiceLoading = MutableStateFlow(true)
    val isWeatherServiceLoading: StateFlow<Boolean> get() = _isWeatherServiceLoading

    fun updateWeatherServiceLoading(bool: Boolean){
        _isWeatherServiceLoading.value = bool
    }

    private val _weatherServiceError = MutableStateFlow("")
    val weatherServiceError: StateFlow<String> get() = _weatherServiceError

    fun writeWeatherServiceError(err: String){
        _weatherServiceError.value = err
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()


    private val retrofit = Retrofit.Builder()
        .baseUrl(WEATHER_API_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    private val weatherApiService = retrofit.create(WeatherService::class.java)
    fun fetchWeatherData(apiKey: String, address: String) {
        _isWeatherServiceLoading.value = true
        viewModelScope.launch {
            weatherApiService.getWeatherData(
                cityLocation = address,
                unitGroup = "metric",
                metric = "days",
                apiKey = apiKey,
                contentType = "json"
            )
                ?.enqueue(object : Callback<WeatherResponse?> {
                    override fun onResponse(
                        call: Call<WeatherResponse?>,
                        response: Response<WeatherResponse?>
                    ) {
                        if (response.isSuccessful) {
                            _weatherData.value = response.body()
                            _isWeatherServiceLoading.value = false
                        }

                    }

                    override fun onFailure(call: Call<WeatherResponse?>, t: Throwable) {
                        _weatherServiceError.value = t.toString()
                        _isWeatherServiceLoading.value = false
                    }
                })
        }

    }
}
