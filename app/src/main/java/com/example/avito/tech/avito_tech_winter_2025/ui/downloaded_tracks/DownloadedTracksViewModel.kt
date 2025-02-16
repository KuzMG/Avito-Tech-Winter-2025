package com.example.avito.tech.avito_tech_winter_2025.ui.downloaded_tracks

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.example.avito.tech.avito_tech_winter_2025.api.dto.model.Album
import com.example.avito.tech.avito_tech_winter_2025.api.dto.model.Artist
import com.example.avito.tech.avito_tech_winter_2025.api.dto.model.Track
import javax.inject.Inject

class DownloadedTracksViewModel() :
    ViewModel() {
    private lateinit var tracks: List<Track>
    private val mutableSearchQuery = MutableLiveData<String>()
    lateinit var tracksLiveData: LiveData<List<Track>>
    var query: String = ""
        set(value) {
            field = value
            mutableSearchQuery.value = value
        }
    fun loadAudio(context:Context): List<Track> {
        val projection =
            arrayOf(
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.ArtistColumns.ARTIST,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.ALBUM
            )
        val cursor = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null
        )
        val tracks = arrayListOf<Track>()
        cursor?.use {
            it.run {
                while (moveToNext()) {
                    val data =
                        getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
                    val title =
                        getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
                    val artist =
                        getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))

                    val albumTitle = getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM))
                    val albumId =
                        getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID))
                    val albumArtUri = Uri.parse("content://media/external/audio/albumart")
                    val uri = ContentUris.withAppendedId(albumArtUri, albumId).toString()

                    tracks.add(Track(title, data, Artist(artist), Album(uri,albumTitle)))
                }
            }
        }
        return tracks
    }
    fun load(context: Context){
        tracks = loadAudio(context)
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