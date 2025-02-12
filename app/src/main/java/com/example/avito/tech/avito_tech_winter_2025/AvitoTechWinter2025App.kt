package com.example.avito.tech.avito_tech_winter_2025

import android.app.Application
import com.example.avito.tech.avito_tech_winter_2025.di.AppComponent
import com.example.avito.tech.avito_tech_winter_2025.di.DaggerAppComponent

class AvitoTechWinter2025App : Application() {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder().application(this).build()
    }
}