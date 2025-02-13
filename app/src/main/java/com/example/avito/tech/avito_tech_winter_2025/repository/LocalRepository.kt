package com.example.avito.tech.avito_tech_winter_2025.repository

import android.app.Application
import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import com.example.avito.tech.avito_tech_winter_2025.service.dto.model.Album
import com.example.avito.tech.avito_tech_winter_2025.service.dto.model.Artist
import com.example.avito.tech.avito_tech_winter_2025.service.dto.model.Track
import javax.inject.Inject

class LocalRepository @Inject constructor(private val app: Application) {

    fun loadAudio(): List<Track> {
        val projection =
            arrayOf(
                MediaStore.Audio.ArtistColumns.ARTIST,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.ALBUM
            )
        val cursor = app.contentResolver.query(
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
                    val title =
                        getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
                    val artist =
                        getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))

                    val albumTitle = getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM))
                    val albumId =
                        getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID))
                    val albumArtUri = Uri.parse("content://media/external/audio/albumart")
                    val uri = ContentUris.withAppendedId(albumArtUri, albumId).toString()
                    tracks.add(Track(title, uri, Artist(artist), Album(uri,albumTitle)))
                }
            }
        }
        return tracks
    }
}