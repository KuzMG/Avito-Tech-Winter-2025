package com.example.avito.tech.avito_tech_winter_2025.repository

import android.app.Application
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.example.avito.tech.avito_tech_winter_2025.ui.downloaded_tracks.data.Track
import javax.inject.Inject

class LocalRepository @Inject constructor(private val app: Application) {

    fun loadAudio(): MutableList<Track> {
        val projection =
            arrayOf(
                MediaStore.Audio.ArtistColumns.ARTIST,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ALBUM_ID
            )
        val cursor = app.contentResolver.query(
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