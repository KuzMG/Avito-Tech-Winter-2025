package com.example.avito.tech.avito_tech_winter_2025.ui.downloaded_tracks

import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import com.example.avito.tech.avito_tech_winter_2025.ui.downloaded_tracks.data.Track
import java.io.IOException

class DownloadedTracksViewModel : ViewModel() {

    var query: String = ""
        private set

    fun setQuery(query: String = "") {

    }
    fun loadAudio(context: Context): MutableList<Track> {
        val projection =
            arrayOf(
                MediaStore.Audio.ArtistColumns.ARTIST,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ALBUM_ID
            )
        val cursor = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null
        )
        val tracks = mutableListOf<Track>()
        cursor?.use {
            it.run {
                while (moveToNext()) {
                    val title =getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
                    val author =getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
                    val albumId =getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID))
                    val albumArtUri = Uri.parse("content://media/external/audio/albumart")
                    val uri = ContentUris.withAppendedId(albumArtUri, albumId)
                    tracks.add(Track(uri, title, author))
                }
            }
        }
        return tracks
    }
}