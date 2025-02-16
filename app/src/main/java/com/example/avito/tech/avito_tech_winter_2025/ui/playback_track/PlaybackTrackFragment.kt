package com.example.avito.tech.avito_tech_winter_2025.ui.playback_track

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.media3.common.Player
import androidx.media3.common.Tracks
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.avito.tech.avito_tech_winter_2025.R
import com.example.avito.tech.avito_tech_winter_2025.databinding.FragmentPlaybackTrackBinding
import com.example.avito.tech.avito_tech_winter_2025.notification.NotificationHelper
import com.example.avito.tech.avito_tech_winter_2025.page_transformer.ZoomOutPageTransformer
import com.example.avito.tech.avito_tech_winter_2025.receiver.InternetReceiver
import com.example.avito.tech.avito_tech_winter_2025.receiver.PlayerManager
import com.example.avito.tech.avito_tech_winter_2025.ui.playback_track.cover.CoverFragment
import com.example.avito.tech.internship.utils.appComponent
import com.example.avito.tech.internship.utils.getParcelableArrayCompat
import com.example.avito.tech.internship.utils.millisecondFormat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.slider.Slider
import com.squareup.picasso.Picasso


class PlaybackTrackFragment : Fragment() {
    companion object {
        const val ARG_ID_TRACK = "id"
        const val ARG_TRACKS = "tracks"
        const val ARG_POSITION = "position"
        const val ARG_INTERNET = "isInternet"
    }

    private val viewModel by viewModels<PlaybackTracksViewModel> {
        appComponent.playbackTrackFactory.create(
            arguments?.getLong(ARG_ID_TRACK) ?: 0L,
            arguments?.getParcelableArrayCompat(ARG_TRACKS) ?: arrayOf(),
            arguments?.getInt(ARG_POSITION) ?: 0,
            arguments?.getBoolean(ARG_INTERNET) ?: false
        )
    }

    private lateinit var binding: FragmentPlaybackTrackBinding
    private lateinit var player: ExoPlayer
    private lateinit var mediaSession: MediaSession

    private val handler: Handler = Handler(Looper.getMainLooper())
    private val updateSongTimeHandler = object : Runnable {
        override fun run() {
            Log.d("TAG", "updateSongTimeHandler start")
            if (player.currentPosition < player.duration && player.currentPosition >= 0) {
                binding.currentTimeTextView.text =
                    millisecondFormat(player.currentPosition)
                Log.d(
                    "TAG",
                    "updateSongTimeHandler slider = ${player.currentPosition.toFloat() / player.duration}"
                )
                binding.slider.value = player.currentPosition.toFloat() / player.duration
            }
            Log.d(
                "TAG",
                "updateSongTimeHandler currentTime = ${millisecondFormat(player.currentPosition)} currentPosition = ${player.currentPosition} duration = ${player.duration}"
            )

            handler.postDelayed(this, 500)
        }
    }


    private val pageChange = object : OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            if (position > viewModel.position) {
                Log.d("TAG", "onPageSelected next")
                player.seekToNext()
                binding.slider.value = 0F
            } else if (position < viewModel.position) {
                Log.d("TAG", "onPageSelected Previous")
                player.seekToPreviousMediaItem()
                binding.slider.value = 0F
            }
        }
    }


    private val sliderTouchListener = object : Slider.OnSliderTouchListener {
        override fun onStartTrackingTouch(slider: Slider) {
            handler.removeCallbacks(updateSongTimeHandler)
            Log.d("TAG", "onStartTrackingTouch ")
        }

        override fun onStopTrackingTouch(slider: Slider) {
            val currentTime = (player.duration * slider.value).toLong()
            Log.d(
                "TAG",
                "onStopTrackingTouch currentTime = ${millisecondFormat(currentTime)} slider =  ${slider.value} duration = ${player.duration}"
            )
            if (slider.value == 1F) {
                player.seekTo(viewModel.position, player.duration - 100)
            } else {
                player.seekTo(viewModel.position, currentTime)
            }
            handler.removeCallbacks(updateSongTimeHandler)
            handler.postDelayed(updateSongTimeHandler, 100)

        }
    }


    private val sliderChangeListener =
        Slider.OnChangeListener { slider, value, fromUser ->
            if (player.duration < 0)
                return@OnChangeListener
            Log.d(
                "TAG",
                "sliderChangeListener currentTime = ${millisecondFormat((player.duration * slider.value).toLong())} duration = ${player.duration}"
            )
            binding.currentTimeTextView.text =
                millisecondFormat((player.duration * slider.value).toLong())
        }


    private val playerListener = object : Player.Listener {
        override fun onEvents(player: Player, events: Player.Events) {
            Player.EVENT_AUDIO_SESSION_ID
            super.onEvents(player, events)
            for (i in 0..events.size() - 1)
                Log.d("TAG", "events ${events.get(i)}")
            Log.d("TAG", "______")
        }

        override fun onTracksChanged(tracks: Tracks) {
            super.onTracksChanged(tracks)
            visibilityTrack(player.currentMediaItemIndex)
        }
    }

    private lateinit var internetReceiver: InternetReceiver


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeMediaPlayer()
        requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).visibility = View.GONE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaybackTrackBinding.inflate(inflater, container, false)
        return binding.root
    }
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (!isGranted) {
            }
        }
    private fun checkPermission() {
        val notifPermission = Manifest.permission.POST_NOTIFICATIONS
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                notifPermission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(notifPermission)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()

        binding.viewPager?.run {
            offscreenPageLimit = 1
            setPadding(95, 0, 95, 0)
            setPageTransformer(ZoomOutPageTransformer())
            adapter = CoverSlidePagerAdapter(this@PlaybackTrackFragment)
            setCurrentItem(viewModel.position, false)
        }
        binding.closeButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.albumTextView.isSelected = true
        binding.titleTextView.isSelected = true
        binding.artistTextView.isSelected = true

        binding.apply {
            binding.viewPager?.registerOnPageChangeCallback(pageChange)
            slider.addOnSliderTouchListener(sliderTouchListener)
            slider.addOnChangeListener(sliderChangeListener)
            player.addListener(playerListener)
            playPauseButton.setOnCheckedChangeListener { _, f ->
                if (f) player.play() else player.pause()
                Log.d("TAG", "playPauseButton play - $f")
            }
            prevButtom.setOnClickListener {
                player.seekToPrevious()
            }
            nextButtom.setOnClickListener {
                player.seekToNext()
            }
            visibilityTrack(viewModel.position)
        }
        player.setMediaItems(viewModel.mediaItems)
        player.prepare()
        player.seekToDefaultPosition(viewModel.position)
        Log.d("TAG", "trackPosition ${viewModel.trackPosition} duration ${player.duration}")
        player.seekTo(viewModel.trackPosition)
        Log.d(
            "TAG",
            " play - ${viewModel.isPlaying},playPauseButton.isChecked ${binding.playPauseButton.isChecked}"
        )
        registerReceiver()
        createNotification()
    }

    private fun createNotification() {
        val notification = NotificationHelper.createNotification(requireContext())
        val nf =
            requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nf.notify(NotificationHelper.NOTIFICATION_ID, notification)
    }

    private fun registerReceiver() {
        PlayerManager.player = player
        internetReceiver = InternetReceiver(player, binding, viewModel)
        requireActivity().registerReceiver(
            internetReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )


    }

    private fun unregisterReceiver() {
        PlayerManager.player = null
        requireActivity().unregisterReceiver(internetReceiver)
    }

    override fun onStart() {
        super.onStart()
        handler.removeCallbacks(updateSongTimeHandler)
        handler.postDelayed(updateSongTimeHandler, 100)
    }

    override fun onStop() {
        handler.removeCallbacks(updateSongTimeHandler)
        super.onStop()

    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver()
        requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).visibility =
            View.VISIBLE
        viewModel.isPlaying = player.isPlaying

        viewModel.trackPosition = if (player.currentPosition >= player.duration) {
            player.duration
        } else {
            player.currentPosition
        }
        Log.d("TAG", "onDestroy currentPosition ${player.currentPosition}")
        Log.d("TAG", "onDestroy duration ${player.duration}")
        Log.d("TAG", "onDestroy trackPosition ${viewModel.trackPosition}")
        mediaSession.release()
        player.release()
    }

    private fun visibilityTrack(position: Int) {
        Log.d("TAG", "visibilityTrack position = $position")
        binding.viewPager?.unregisterOnPageChangeCallback(pageChange)
        binding.viewPager?.setCurrentItem(position, true)
        binding.viewPager?.registerOnPageChangeCallback(pageChange)
        viewModel.tracks[position].run {
            binding.titleTextView.text = title
            binding.artistTextView.text = artist.name
            binding.albumTextView.text = getString(R.string.album, album.title)

            binding.imageView?.let {
                Picasso.get().load(album.cover).placeholder(R.drawable.default_track)
                    .into(binding.imageView)

            }
        }
        if (player.currentPosition >= 0 && player.duration >= 0) {
            binding.durationTextView.text = millisecondFormat(player.duration)
        } else {
            binding.durationTextView.text = millisecondFormat(0)
        }
        handler.removeCallbacks(updateSongTimeHandler)
        handler.postDelayed(updateSongTimeHandler, 100)
        Log.d("TAG", "visibilityTrack currentPosition ${player.currentPosition}")
        Log.d("TAG", "visibilityTrack duration ${player.duration}")
        viewModel.position = position
        if (binding.playPauseButton.isChecked == viewModel.isPlaying) {
            if (viewModel.isPlaying)
                player.play()
            else
                player.pause()
        } else {
            binding.playPauseButton.isChecked = viewModel.isPlaying
        }


    }

    private fun initializeMediaPlayer() {
        player = ExoPlayer.Builder(requireContext()).build()
        mediaSession = MediaSession.Builder(requireContext(), player).build()
    }


    private inner class CoverSlidePagerAdapter(f: Fragment) : FragmentStateAdapter(f) {
        override fun getItemCount(): Int = viewModel.tracks.size

        override fun createFragment(position: Int): Fragment =
            CoverFragment.newInstance(viewModel.tracks[position].album.cover)


    }
}