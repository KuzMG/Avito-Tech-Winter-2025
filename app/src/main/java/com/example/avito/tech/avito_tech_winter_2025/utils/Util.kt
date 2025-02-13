package com.example.avito.tech.internship.utils

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import com.example.avito.tech.avito_tech_winter_2025.AvitoTechWinter2025App
import com.example.avito.tech.avito_tech_winter_2025.di.AppComponent
import java.util.ArrayList


val Context.appComponent: AppComponent
    get() = when (this) {
        is AvitoTechWinter2025App -> appComponent
        else -> applicationContext.appComponent
    }

val Fragment.appComponent: AppComponent
    get() = context!!.appComponent


inline fun <reified T : Parcelable>Bundle.getParcelableArrayListCompat(key:String): ArrayList<T>? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelableArrayList(key, T::class.java)
    } else {
        getParcelableArrayList<T>(key)
    }
}
inline fun <reified T : Parcelable>Bundle.getParcelableArrayCompat(key:String): Array<T>? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelableArray(key, T::class.java)
    } else ({
        getParcelableArray(key)
    }) as Array<T>?
}
