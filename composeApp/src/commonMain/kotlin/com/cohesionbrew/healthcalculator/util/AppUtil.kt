package com.cohesionbrew.healthcalculator.util

interface AppUtil {
    fun getAppName():String
    fun shareApp()
    fun openFeedbackMail()
    fun getAppVersionInfo(): String
}