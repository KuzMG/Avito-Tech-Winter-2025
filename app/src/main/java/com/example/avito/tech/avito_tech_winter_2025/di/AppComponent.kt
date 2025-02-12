package com.example.avito.tech.avito_tech_winter_2025.di

import android.app.Application
import com.example.avito.tech.avito_tech_winter_2025.di.module.ServiceModule
import com.example.avito.tech.avito_tech_winter_2025.di.module.ViewModelModule
import com.example.avito.tech.avito_tech_winter_2025.viewModel.MultiViewModelFactory
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ViewModelModule::class,ServiceModule::class])
interface AppComponent {
    val multiViewModelFactory: MultiViewModelFactory

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }
}