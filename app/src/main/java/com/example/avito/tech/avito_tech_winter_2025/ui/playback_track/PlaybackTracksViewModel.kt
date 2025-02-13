package com.example.avito.tech.avito_tech_winter_2025.ui.playback_track

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.avito.tech.avito_tech_winter_2025.repository.AvitoRepository
import com.example.avito.tech.avito_tech_winter_2025.service.dto.model.Track
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class PlaybackTracksViewModel(
    var id: Long,
    var tracks: Array<Track>,
    var position: Int,
    private val avitoRepository: AvitoRepository
) :
    ViewModel() {
    init {
        println(tracks)
    }
    class ViewModelFactory @AssistedInject constructor(
        @Assisted private val id: Long,
        @Assisted private val tracks: Array<Track>,
        @Assisted private val position: Int,
        private val repository: AvitoRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass == PlaybackTracksViewModel::class.java)
            @Suppress("UNCHECKED_CAST")
            return PlaybackTracksViewModel(id,tracks,position,repository) as T
        }
    }

    @AssistedFactory
    interface FactoryHelper {
        fun create(
            @Assisted id: Long,
            @Assisted tracks: Array<Track>,
            @Assisted position: Int
        ): ViewModelFactory
    }

}