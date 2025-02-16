package com.example.avito.tech.avito_tech_winter_2025.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.util.Log
import android.view.View
import androidx.media3.exoplayer.ExoPlayer
import com.example.avito.tech.avito_tech_winter_2025.databinding.FragmentPlaybackTrackBinding
import com.example.avito.tech.avito_tech_winter_2025.ui.playback_track.PlaybackTracksViewModel


class InternetReceiver(private var player: ExoPlayer,private var binding: FragmentPlaybackTrackBinding,private var viewModel: PlaybackTracksViewModel) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        if (viewModel.isInternet) {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = connectivityManager.activeNetworkInfo
            val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting
            Log.d("TAG", "onReceive $isConnected")
            if (isConnected) {
                binding.nextButtom.isEnabled = true
                binding.prevButtom.isEnabled = true
                binding.viewPager?.isEnabled = true
                binding.viewPager?.isUserInputEnabled = true
                binding.internetExceptionTextView.visibility = View.GONE

                if (!player.isPlaying) {
                    player.prepare()
                    player.playWhenReady = viewModel.isPlaying
                    Log.d("TAG", "onReceive viewModel connection ${viewModel.isPlaying}")

                }
            } else {
                Log.d("TAG", "onReceive player notConnection ${player.isPlaying}")
                binding.internetExceptionTextView.visibility = View.VISIBLE
                viewModel.isPlaying = player.isPlaying
                binding.nextButtom.isEnabled = false
                binding.prevButtom.isEnabled = false
                binding.viewPager?.isUserInputEnabled = false
            }
        }

    }
}