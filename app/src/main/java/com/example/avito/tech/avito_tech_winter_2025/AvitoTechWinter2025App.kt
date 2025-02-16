package com.example.avito.tech.avito_tech_winter_2025

import android.app.Application
import androidx.activity.result.contract.ActivityResultContracts
import com.example.avito.tech.avito_tech_winter_2025.di.AppComponent
import com.example.avito.tech.avito_tech_winter_2025.di.DaggerAppComponent
import com.example.avito.tech.avito_tech_winter_2025.notification.NotificationHelper

class AvitoTechWinter2025App : Application() {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder().application(this).build()
    }

    override fun onCreate() {
        super.onCreate()
        NotificationHelper.createNotificationChannel(this)
    }
}