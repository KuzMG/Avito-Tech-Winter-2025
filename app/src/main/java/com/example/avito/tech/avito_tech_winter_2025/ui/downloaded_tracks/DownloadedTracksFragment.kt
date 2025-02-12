package com.example.avito.tech.avito_tech_winter_2025.ui.downloaded_tracks


import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.example.avito.tech.avito_tech_winter_2025.ui.downloaded_tracks.data.Track
import com.example.avito.tech.internship.utils.appComponent
import com.example.avito.tech.ui.RecyclerViewFragment
import com.example.avito.tech.ui.TrackViewHolder
import com.example.avito.tech.ui.TracksAdapter
import com.squareup.picasso.Picasso

class DownloadedTracksFragment : RecyclerViewFragment() {
    private val viewModel by viewModels<DownloadedTracksViewModel>() {
        appComponent.multiViewModelFactory
    }
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getTracks()
            }
        }

    override fun searchListener(searchView: SearchView) {
        searchView.setQuery(viewModel.query, false)
        searchView.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String): Boolean {
                    return true
                }

                override fun onQueryTextChange(p0: String): Boolean {
                    viewModel.query = p0
                    return true
                }
            })
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
    }

    private fun getTracks() {
        viewModel.tracksLiveData.observe(viewLifecycleOwner) {
            binding.recyclerView.adapter =
                DownloadedTrackAdapter(it)
        }
    }

    private fun checkPermission() {
        val readImagePermission =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                Manifest.permission.READ_MEDIA_AUDIO else Manifest.permission.READ_EXTERNAL_STORAGE
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                readImagePermission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(readImagePermission)
        } else {
            getTracks()
        }
    }


    class DownloadedTrackAdapter(private val tracks: List<Track>) :
        TracksAdapter<Track>(tracks) {
        override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            holder.binding.run {
                Picasso.get().load(tracks[position].pic).into(imageView)
                titleTextView.text = tracks[position].title
                authorTextView.text = tracks[position].author
            }
        }
    }
}