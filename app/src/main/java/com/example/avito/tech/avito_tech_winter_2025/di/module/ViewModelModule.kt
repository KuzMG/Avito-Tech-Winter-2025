package com.example.avito.tech.avito_tech_winter_2025.di.module

import androidx.lifecycle.ViewModel
import com.example.avito.tech.avito_tech_winter_2025.ui.api_tracks.ApiTracksViewModel
import com.example.avito.tech.avito_tech_winter_2025.ui.downloaded_tracks.DownloadedTracksViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

@Module
interface ViewModelModule {
    @[Binds IntoMap ViewModelKey(DownloadedTracksViewModel::class)]
    fun provideDownloadedTracksViewModel(downloadedTracksViewModel: DownloadedTracksViewModel): ViewModel

    @[Binds IntoMap ViewModelKey(ApiTracksViewModel::class)]
    fun provideApiTracksViewModel(apiTracksViewModel: ApiTracksViewModel): ViewModel
}


@MustBeDocumented
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)