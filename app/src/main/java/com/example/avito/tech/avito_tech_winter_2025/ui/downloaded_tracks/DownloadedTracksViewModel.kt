package com.example.avito.tech.avito_tech_winter_2025.ui.downloaded_tracks

import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import com.example.avito.tech.avito_tech_winter_2025.repository.LocalRepository
import com.example.avito.tech.avito_tech_winter_2025.ui.downloaded_tracks.data.Track
import java.io.IOException
import javax.inject.Inject

class DownloadedTracksViewModel @Inject constructor(localRepository: LocalRepository) : ViewModel() {
    private val tracks: List<Track>
    private val mutableSearchQuery = MutableLiveData<String>()
    val tracksLiveData: LiveData<List<Track>>
    var query: String = ""
        set(value) {
            field = value
            mutableSearchQuery.value = value
        }
    init {
        tracks = localRepository.loadAudio()
        mutableSearchQuery.value = query
        tracksLiveData = mutableSearchQuery.map { query ->
            if (query.isBlank()) {
                tracks
            } else {
                tracks.filter {
                    it.title.lowercase().contains(query)
                }
            }
        }
    }


}