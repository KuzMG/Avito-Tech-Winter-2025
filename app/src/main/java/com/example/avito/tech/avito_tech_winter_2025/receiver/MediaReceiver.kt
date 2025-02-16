package com.example.avito.tech.avito_tech_winter_2025.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.media3.exoplayer.ExoPlayer

class MediaReceiver : BroadcastReceiver() {

    companion object{
        const val ACTION_PLAY = "PLAY"
        const val ACTION_PAUSE = "PAUSE"
        const val ACTION_NEXT = "NEXT"
        const val ACTION_PREV = "PREV"
    }

    override fun onReceive(context: Context?, intent: Intent) {
        val action = intent.action
        val player = PlayerManager.player
        if (action != null) {
            when (action) {
                ACTION_NEXT -> {
                    player?.seekToNext()

                }
                ACTION_PREV -> {
                    player?.seekToPrevious()
                }
            }
        }
    }
}

object PlayerManager {
    var player: ExoPlayer? = null
}