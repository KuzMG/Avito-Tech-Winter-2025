package com.example.avito.tech.avito_tech_winter_2025.di.module

import com.example.avito.tech.avito_tech_winter_2025.service.api.AvitoApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class ServiceModule {

    @Singleton
    @Provides
    fun provideAvitoApi() = Retrofit
        .Builder()
        .baseUrl(URL)
        .client(OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS).build())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(AvitoApi::class.java)

    companion object {
        const val URL = "https://api.deezer.com/"
    }
}