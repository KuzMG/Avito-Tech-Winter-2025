package com.example.avito.tech.internship.utils

import android.content.Context
import androidx.fragment.app.Fragment
import com.example.avito.tech.avito_tech_winter_2025.AvitoTechWinter2025App
import com.example.avito.tech.avito_tech_winter_2025.di.AppComponent


val Context.appComponent: AppComponent
    get() = when (this) {
        is AvitoTechWinter2025App -> appComponent
        else -> applicationContext.appComponent
    }

val Fragment.appComponent: AppComponent
    get() = context!!.appComponent
