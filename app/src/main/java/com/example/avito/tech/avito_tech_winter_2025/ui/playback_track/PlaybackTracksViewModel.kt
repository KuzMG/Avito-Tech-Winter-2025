package com.example.avito.tech.avito_tech_winter_2025.ui.playback_track

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.media3.common.MediaItem
import com.example.avito.tech.avito_tech_winter_2025.repository.AvitoRepository
import com.example.avito.tech.avito_tech_winter_2025.api.dto.model.Track
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class PlaybackTracksViewModel(
    var id: Long,
    var tracks: Array<Track>,
    var position: Int,
    val isInternet:Boolean,
    private val avitoRepository: AvitoRepository
) :
    ViewModel() {
    var trackPosition = 0L
    var isPlaying = true
    val mediaItems: List<MediaItem> = tracks.map {
        MediaItem.fromUri(it.preview)
    }


    class ViewModelFactory @AssistedInject constructor(
        @Assisted private val id: Long,
        @Assisted private val tracks: Array<Track>,
        @Assisted private val position: Int,
        @Assisted private val isInternet: Boolean,
        private val repository: AvitoRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass == PlaybackTracksViewModel::class.java)
            @Suppress("UNCHECKED_CAST")
            return PlaybackTracksViewModel(id, tracks, position,isInternet, repository) as T
        }
    }

    @AssistedFactory
    interface FactoryHelper {
        fun create(
            @Assisted id: Long,
            @Assisted tracks: Array<Track>,
            @Assisted position: Int,
            @Assisted isInternet: Boolean
        ): ViewModelFactory
    }

}