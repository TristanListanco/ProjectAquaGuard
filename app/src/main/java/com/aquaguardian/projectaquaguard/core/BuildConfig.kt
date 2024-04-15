package com.aquaguardian.projectaquaguard.core

class BuildConfig{
    companion object{
        /**
         * Bypasses the Authentication Screen entirely.
         * The app will still work without authenticated user, but it will be unusable.
         * Generally used for Testing Certain UI parts during development.
         */
        const val BYPASS_AUTH_SCREEN_UI = true

        /**
         * Bypasses the Welcome Screen and will directly open Authentication Screen of Main Home Screen
         */
        const val BYPASS_WELCOME_SCREEN_UI = false

        /**
         * Shows or Hides the special thanks list of members in the About View
         */
        const val SHOW_SPECIAL_THANKS_ABOUT_VIEW = false

        /**
         * API URL in @see WeatherWidget
         */
        const val WEATHER_API_BASE_URL = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/"

        /**
         * Authentication Key of @see WeatherWidget
         */
        const val WEATHER_API_AUTH_KEY = "4J9LPXQYR8P3SFFQHM9422J7R"

        /**
         * Enables the Data Debugger for each Tanks.
         * Has no effects when enabled in release mode of the app.
         * Even when app is hijacked / decompiled to use this feature,
         * only admin user accounts can successfully push data to the servers.
         */
        const val ENABLE_DATA_DEBUGGER = true
    }
}